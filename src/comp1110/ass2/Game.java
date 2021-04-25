package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Floor;
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Score;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    private final String COMMON_REGEX = "^([A-D])F((\\d\\w{4}){0,5})C([a-e]{0,15}f?)B((\\d{2}){5})D((\\d{2}){5})$";
    private final String PLAYER_REGEX = "([AB])(\\d{0,3})M(([a-e]\\d{2})*)S((\\d[a-e]\\d)*)F([a-f]*)";
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
        //task 2
        Pattern pattern = Pattern.compile(COMMON_REGEX);
        Matcher matcher = pattern.matcher(sharedState);
        boolean matchFound = matcher.find();
        if (matchFound) {
            String factoriesToken = matcher.group(2);
            String centreToken = matcher.group(4);
            String bagToken = matcher.group(5);
            String discardToken = matcher.group(7);

            if (factoriesToken.length() % 5 != 0) {
                return false;
            }
            for (int i = 0; i < factoriesToken.length(); i += 5) {
                String factoryToken = factoriesToken.substring(i, i + 5);
                if (!Factory.isWellFormedFactoryString(factoryToken)) {
                    return false;
                }
            }
            return Centre.isWellFormedCentreString(centreToken) &&
                    Bag.isWellFormedBagString(bagToken) &&
                    Discard.isWellFormedDiscardString(discardToken);
        } else {
            return false;
        }
    }

    public void reconstructCommonFrom(String sharedState) {
        Pattern pattern = Pattern.compile(COMMON_REGEX);
        Matcher matcher = pattern.matcher(sharedState);
        boolean matchFound = matcher.find();
        if (matchFound) {
//            System.out.println("Found Common: " + matcher.group(0));

            this.turn = matcher.group(1);

            String factoryStates = matcher.group(2);
            String centreState = matcher.group(4);
            String bagState = matcher.group(5);
            String discardState = matcher.group(7);

            for (int i = 0; i < factoryStates.length(); i += 5) {
                String factoryState = factoryStates.substring(i, i + 5);
                this.common.getFactories()[i / 5].reconstructFromString(factoryState);
            }
            this.common.getCentre().reconstructFromString(centreState);
            this.common.getBag().reconstructFromString(bagState);
            this.common.getDiscard().reconstructFromString(discardState);

//            System.out.println("Build Common: " + this.turn + this.common.toString());

        } else {
//            System.out.println("Not Found Common: " + sharedState);
        }
    }

    public boolean isPlayerStateWellFormed(String playerState) {
        //task 3
        Pattern pattern = Pattern.compile(PLAYER_REGEX);
        boolean matchFound = true;
        int fullLength = playerState.length();
        int length = 0;
        while (matchFound && !playerState.equals("")) {
            Matcher matcher = pattern.matcher(playerState);
            matchFound = matcher.find();
            if (matchFound) {
                String matchToken = matcher.group(0);
                String mosaicToken = matcher.group(3);
                String storageToken = matcher.group(5);
                String floorToken = matcher.group(7);

                System.out.println(matchToken);
                if (!Mosaic.isWellFormedMosaicString(mosaicToken) ||
                        !Storage.isWellFormedStorageString(storageToken) ||
                        !Floor.isWellFormedFloorString(floorToken)) {
                    return false;
                }
                length += matchToken.length();
                playerState = playerState.substring(matcher.end());
            }
        }
        return length == fullLength;
    }

    public void reconstructBoardsFrom(String playerState) {
        Pattern pattern = Pattern.compile(PLAYER_REGEX);
        boolean matchFound = true;
        while (matchFound && !playerState.equals("")) {
            Matcher matcher = pattern.matcher(playerState);
            matchFound = matcher.find();
            if (matchFound) {
//                System.out.println("Found Player: " + matcher.group(0));

                String playerToken = matcher.group(1);
                String scoreToken = matcher.group(2);
                String mosaicToken = matcher.group(3);
                String storageToken = matcher.group(5);
                String floorToken = matcher.group(7);

                Player player = players[playerToken.charAt(0) - 'A'];

                player.getBoard().getScore().reconstructFromString(scoreToken);
                player.getBoard().getMosaic().reconstructFromString(mosaicToken);
                player.getBoard().getStorage().reconstructFromString(storageToken);
                player.getBoard().getFloor().reconstructFromString(floorToken);

//                System.out.println("Build Player: " + player.toString());
                playerState = playerState.substring(matcher.end());
            } else {
//                System.out.println("Not Found Player: " + playerState);
            }
        }
    }

    public char drawTileFromBag(String[] gameState) {
        //task 5
        this.reconstructCommonFrom(gameState[0]);
        Tile tile = common.getBag().drawTile(common.getDiscard());
        if (tile == null) {
            return 'Z';
        } else {
            return tile.getColorCode();
        }
    }

    public String[] refillFactories(String[] gameState) {
        //task 6
        this.reconstructCommonFrom(gameState[0]);
        Factory[] factories = common.getFactories();
        ArrayList<Tile> centreTiles = common.getCentre().getTiles();
        if (centreTiles.size() > 0 &&
                !(centreTiles.size() == 1 && centreTiles.get(0).getColorCode() == 'f')) {
            return gameState;
        }

        for (Factory factory : factories) {
            if (factory.getTiles().size() != 0) {
                return gameState;
            }
        }
        for (Factory factory : factories) {
            factory.refillTiles(common.getBag(), common.getDiscard());
        }
        gameState[0] = turn + common.toString();

        return gameState;
    }

    public int getBonusPoints(String[] gameState, char player) {
        //task 7
        this.reconstructCommonFrom(gameState[0]);
        this.reconstructBoardsFrom(gameState[1]);
        return players[player - 'A'].getBoard().getMosaic().calculateBonusScore().getScore();
    }

    public String[] nextRound(String[] gameState) {
        //task 8
        reconstructCommonFrom(gameState[0]);
        reconstructBoardsFrom(gameState[1]);

        Factory[] factories = common.getFactories();
        ArrayList<Tile> centreTiles = common.getCentre().getTiles();
        if (centreTiles.size() > 0 &&
                !(centreTiles.size() == 1 && centreTiles.get(0).getColorCode() == 'f')) {
            return gameState;
        }

        for (Factory factory : factories) {
            if (factory.getTiles().size() != 0) {
                return gameState;
            }
        }

        for (Player player : players) {
            if (player.getBoard().getStorage().hasCompleteRow()) {
                return gameState;
            }
        }

        for (Player player : players) {
            if (player.getBoard().getFloor().hasFirstPlayerTile()) {
                turn = String.valueOf(player.getId());
            }
        }

        for (Player player : players) {
            Board playerBoard = player.getBoard();
            playerBoard.getScore().addScore(playerBoard.getFloor().calculatePenalty());
            playerBoard.getScore().addScore(playerBoard.getMosaic().calculateBonusScore());
            playerBoard.getFloor().clearTiles(common.getDiscard(), common.getCentre());
        }

        gameState[0] = turn + common.toString();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (Player value : players) {
            stringBuilder1.append(value.toString());
        }
        gameState[1] = stringBuilder1.toString();

        for (Player value : players) {
            if (value.getBoard().getMosaic().hasCompleteRow()) {
                return gameState;
            }
        }

        for (Factory factory : factories) {
            factory.refillTiles(common.getBag(), common.getDiscard());
        }

        gameState[0] = turn + common.toString();
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : players) {
            stringBuilder.append(player.toString());
        }
        gameState[1] = stringBuilder.toString();
        return gameState;
    }

    /**
     * Given a gameState and a move, apply the move to the gameState. If the move is a Tiling move,
     * you must also update the player's score. If the move is a Tiling move, you must also empty
     * the remaining tiles into the discard.
     * <p>
     * If the move is a Drafting move, you must also move any remaining tiles from the specified
     * factory into the centre. If the move is a Drafting move and you must put tiles onto the
     * floor, any tiles that cannot fit on the floor are placed in the discard with the following
     * exception: If the first player tile would be placed into the discard, it is instead swapped
     * with the last tile in the floor, when the floor is sorted alphabetically.
     *
     * @param gameState the game state.
     * @param move      A string representing a move.
     * @return the updated gameState after the move has been applied. TASK 11
     */
    //The specified row in the Storage area is full.
    //The specified column does not already contain a tile of the same colour.
    //you must also update the player's score.
    //you must also empty the remaining tiles.
    public String[] applyMove(String[] gameState, String move) {

        this.reconstructCommonFrom(gameState[0]);
        this.reconstructBoardsFrom(gameState[1]);

        if (move.length() == 3) {
            Board board = this.players[move.charAt(0) - 'A'].getBoard();
            int row = move.charAt(1) - '0';
            int column = move.charAt(2) - '0';
            Score score =
                    board.getStorage().tileAndScore(
                            row,
                            column,
                            board.getMosaic(),
                            common.getDiscard());
            board.getScore().addScore(score);
        } else if (move.length() == 4) {
            Player player = this.players[move.charAt(0) - 'A'];
            Board board = player.getBoard();
            char colorCode = move.charAt(2);
            String color = Tile.from(colorCode).getColor();
            int row = move.charAt(3) - '0';
            if (move.charAt(1) == 'C') {
                // draw tiles from centre
                ArrayDeque<Tile> tiles = player.drawTiles(color, false, 0, common);
                if (move.charAt(3) == 'F') {
                    // place tiles on floor
                    board.getStorage().placeTiles(tiles, color, 0, row
                            , board.getMosaic(), board.getFloor()
                            , common.getDiscard());

                    // transfer first player tile
                    board.getFloor().placeFirstPlayerTile(common.getCentre().getFirstPlayerTile(),
                            common.getDiscard());
                    common.getCentre().setFirstPlayerTile(null);
                } else {
                    // place tiles on storage
                    board.getStorage().placeTiles(tiles, color, tiles.size(), row
                            , board.getMosaic(), board.getFloor()
                            , common.getDiscard());
                }
            } else {
                // draw tiles from factory
                int factoryNum = move.charAt(1) - '0';
                ArrayDeque<Tile> tiles = player.drawTiles(color, true, factoryNum, common);
                if (move.charAt(3) == 'F') {
                    // place tiles on floor
                    board.getStorage().placeTiles(tiles, color, 0, row
                            , board.getMosaic(), board.getFloor()
                            , common.getDiscard());

                    // transfer first player tile
                    board.getFloor().placeFirstPlayerTile(common.getCentre().getFirstPlayerTile(),
                            common.getDiscard());
                    common.getCentre().setFirstPlayerTile(null);
                } else {
                    // place tiles on storage
                    board.getStorage().placeTiles(tiles, color, tiles.size(), row
                            , board.getMosaic(), board.getFloor()
                            , common.getDiscard());
                }
            }
        }
        turn = (turn.equals("A")) ?"B" : "A";
        // rebuild state
        gameState = new String[2];
        gameState[0] = turn + common.toString();
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : players) {
            stringBuilder.append(player.toString());
        }
        gameState[1] = stringBuilder.toString();
        return gameState;
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.reconstructCommonFrom("AFCB1207080506D0107030805");
        game.reconstructBoardsFrom(
                "A21Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FeeeefB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2Fcc");
    }


}
