package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Floor;
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Score;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Common;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    //shared state
    Common common;

    //player state
    Player[] players;

    String turn;

    public Game() {
        this.common = new Common();
        this.players = new Player[2];
        this.turn = "A";

        for (int i = 0; i < this.players.length; i++) {
            this.players[i] = new Player((char) ('A' + i));
        }

    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public boolean isSharedStateWellFormed(String sharedState) {
        int indexF = sharedState.indexOf('F', 0);
        int indexC = sharedState.indexOf('C', indexF + 1);
        int indexB = sharedState.indexOf('B', indexC + 1);
        int indexD = sharedState.indexOf('D', indexB + 1);

        if (indexF == -1 || indexC == -1 || indexB == -1 || indexD == -1) {
            return false;
        }

        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < sharedState.length(); i++) {
            if (i > indexF && i < indexC) {
                chars.add(sharedState.charAt(i));
            } else if (i == indexC) {
                if(!isWellFormedFactoryString(chars)) {
                    return false;
                }
                chars.clear();
            } else if (i > indexC && i < indexB){
                chars.add(sharedState.charAt(i));
            } else if (i == indexB) {
                if(!Centre.isWellFormedCentreString(chars)) {
                    return false;
                }
                chars.clear();
            } else if (i > indexB && i < indexD) {
                chars.add(sharedState.charAt(i));
            } else if (i == indexD) {
                if(!Bag.isWellFormedBagString(chars)) {
                    return false;
                }
                chars.clear();
            } else if (i > indexD) {
                chars.add(sharedState.charAt(i));
            }
        }
        if(!Discard.isWellFormedDiscardString(chars)){
            return false;
        }

        return true;
    }

    /**
     * 1. [factories] The factories substring begins with an 'F'
     * and is followed by a collection of *up to* 5 5-character factory strings
     * representing each factory.
     * Each factory string is defined in the following way:
     * 1st character is a sequential digit '0' to '4' - representing the
     * factory number.
     * 2nd - 5th characters are 'a' to 'e', alphabetically - representing
     * the tiles.
     * A factory may have between 0 and 4 tiles. If a factory has 0 tiles,
     * it does not appear in the factories string.
     * Factory strings are ordered by factory number.
     * For example: given the string "F1aabc2abbb4ddee": Factory 1 has tiles
     * 'aabc', Factory 2 has tiles 'abbb', Factory 4 has tiles 'ddee', and
     * Factories 0 and 4 are empty.
     */
    public boolean isWellFormedFactoryString(ArrayList<Character> chars) {
        ArrayList<Character> factories = new ArrayList<>();
        ArrayList<Character> tiles = new ArrayList<>();

        if(chars.size() == 0) {
            return true;
        }

        for (int i = 0; i < chars.size(); i++) {
            char c = chars.get(i);

            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4') {
                factories.add(c);
                if (i == 0) {
                    continue;
                }
                if(tiles.size() == 0 || tiles.size() != 4) {
                    return false;
                }
                tiles.clear();
            } else if (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e') {
                if (i == 0) {
                    return false;
                }
                tiles.add(c);
            } else {
                return false;
            }
        }

        // check the remaining;
        if (tiles.size() == 0 || tiles.size() != 4) {
            return false;
        }

        // check unique and strictly increase
        for (int i = 0; i < factories.size() - 1; i++) {
            int curr = factories.get(i);
            int next = factories.get(i+1);
            if (next <= curr) {
                return false;
            }
        }
        return true;
    }



    //"[A-D]F((\\d\\w{4}){0,5})C([abcde]{0,15}f?)B((\\d{2}){5})D((\\d{2}){5})"
    public void reconstructCommonFrom(String sharedState) {
        // refill common
        String regex = "^([A-D])F((\\d\\w{4}){0,5})C([a-e]{0,15}f?)B((\\d{2}){5})D((\\d{2}){5})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sharedState);
        boolean matchFound = matcher.find();
        if(matchFound) {
            System.out.println("Found Common: " + matcher.group(0));
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
//            System.out.println(matcher.group(3));
//            System.out.println(matcher.group(4));
//            System.out.println(matcher.group(5));
//            System.out.println(matcher.group(6));
//            System.out.println(matcher.group(7));
//            System.out.println(matcher.group(8));

            String turnState = matcher.group(1);
            this.turn = turnState;

            String factoryStates = matcher.group(2);
            String centreState = matcher.group(4);
            String bagState = matcher.group(5);
            String discardState = matcher.group(7);

            Factory[] factories = new Factory[5];
            for (int i = 0; i < 5; i++) {
                Factory factory = new Factory();
                factory.setId(i);
                factories[i] = factory;
            }

//            System.out.println("///////////////");
            for (int i = 0; i < factoryStates.length(); i+=5) {
                String factoryState = factoryStates.substring(i, i+5);
                Factory factory = new Factory();
                factory.fillFrom(factoryState);
                factories[factory.getId()] = factory;
            }
            this.common.setFactories(factories);

            Centre centre = new Centre();
            centre.fillFrom(centreState);
            this.common.setCentre(centre);

            Bag bag = new Bag();
            bag.fillFrom(bagState);
            this.common.setBag(bag);

            Discard discard = new Discard();
            discard.fillFrom(discardState);
            this.common.setDiscard(discard);

//            System.out.println(matcher.group(0).substring(1));
//            System.out.println(this.common.toString());

        } else {
            System.out.println("Not Found Common: " + sharedState);
        }
    }

    public boolean isPlayerStateWellFormed(String playerState) {
        int[] indexes = new int[4];
        indexes[0] = playerState.indexOf('A', 0);
        indexes[1] = playerState.indexOf('B', indexes[0] + 1);
        indexes[2] = playerState.indexOf('C', indexes[1] + 1);
        indexes[3] = playerState.indexOf('D', indexes[2] + 1);
        int starting = 0;
        int ending = playerState.length();
        ArrayList<String> subStrings = new ArrayList<>();
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] != -1) {
                String sub = playerState.substring(starting, indexes[i]);
                subStrings.add(sub);
                starting = indexes[i];
            }
        }
        subStrings.add(playerState.substring(starting, ending));
        subStrings.removeIf(sub -> sub.equals(""));

        for(String sub: subStrings) {

            int indexM = sub.indexOf('M', 1);
            int indexS = sub.indexOf('S', indexM + 1);
            int indexF = sub.indexOf('F', indexS + 1);

            if (indexM == -1 || indexS == -1 || indexF == -1) {
                return false;
            }

            ArrayList<Character> chars = new ArrayList<>();
            for (int i = 0; i < sub.length(); i++) {
                if (i < indexM) {
                    chars.add(sub.charAt(i));
                } else if (i == indexM) {
                    if (!isPlayerAndScoreWellFormedString(chars)) {
                        return false;
                    }
                    chars.clear();
                } else if (i > indexM && i < indexS) {
                    chars.add(sub.charAt(i));
                } else if (i == indexS) {
                    if (!Mosaic.isMosaicWellFormedString(chars)) {
                        return false;
                    }
                    chars.clear();
                } else if (i > indexS && i < indexF) {
                    chars.add(sub.charAt(i));
                } else if (i == indexF) {
                    if (!Storage.isStorageWellFormedString(chars)) {
                        return false;
                    }
                    chars.clear();
                } else if (i > indexF) {
                    chars.add(sub.charAt(i));
                }
            }
            if (!Floor.isFloorWellFormedString(chars)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 1. [player] The player substring is one character 'A' to 'D' -
     * representing the Player
     * <p>
     * 2. [score] The score substring is one or more digits between '0' and '9' -
     * representing the score
     */
    private boolean isPlayerAndScoreWellFormedString(ArrayList<Character> playerWithScore) {
        if (playerWithScore.size() < 2 ) {
            return false;
        }
        StringBuilder score = new StringBuilder();
        for(int i = 0; i < playerWithScore.size(); i++) {
            char c = playerWithScore.get(i);
            if (i == 0 && c != 'A' && c != 'B' && c != 'C' && c != 'D') {
                return false;
            }
            if (i != 0){
                score.append(c);
            }
        }

        try {
            Integer.parseInt(score.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }


    public void reconstructBoardsFrom(String playerState) {
        // refill boards
        String regex = "([AB])(\\d{0,3})M(([a-e]\\d{2})*)S((\\d[a-e]\\d)*)F([a-f]*)";
        Pattern pattern = Pattern.compile(regex);
        boolean matchFound = true;
        while(matchFound) {
            Matcher matcher = pattern.matcher(playerState);
            matchFound = matcher.find();
            if (matchFound) {
                System.out.println("Found Player: " + matcher.group(0));
//                System.out.println(matcher.group(1));
//                System.out.println(matcher.group(2));
//                System.out.println(matcher.group(3));
//                System.out.println(matcher.group(4));
//                System.out.println(matcher.group(5));
//                System.out.println(matcher.group(6));
//                System.out.println(matcher.group(7));
//                System.out.println();

                String playerToken = matcher.group(1);
                String scoreToken = matcher.group(2);
                String mosaicToken = matcher.group(3);
                String storageToken = matcher.group(5);
                String floorToken = matcher.group(7);

                Player player = players[playerToken.charAt(0) - 'A'];
//
                Score score = new Score();
                score.fillFrom(scoreToken);
                player.getBoard().setScore(score);

                Mosaic mosaic = new Mosaic();
                mosaic.fillFrom(mosaicToken);
                player.getBoard().setMosaic(mosaic);

                Storage storage = new Storage();
                storage.fillFrom(storageToken);
                player.getBoard().setStorage(storage);

                Floor floor = new Floor();
                floor.fillFrom(floorToken);
                player.getBoard().setFloor(floor);

                playerState = playerState.substring(matcher.group(0).length());
            }
        }
    }

    public void toggleTurn() {
        if (turn.equals("A")) {
            this.turn = "B";
        } else {
            this.turn = "A";
        }
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.reconstructCommonFrom("AFCB1207080506D0107030805");
        game.reconstructBoardsFrom("A21Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FeeeefB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2Fcc");
        int result = game.getPlayers()[0].getBoard().getMosaic().getBonus();
        System.out.println(result);
        // run first turn
        // run second turn
        // ...
    }


}
