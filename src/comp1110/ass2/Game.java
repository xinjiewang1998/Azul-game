package comp1110.ass2;

import comp1110.ass2.board.*;
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
                this.common.getFactories()[factoryState.charAt(0) - 48]
                        .reconstructFromString(factoryState);
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
        Factory[] factories = this.getCommon().getFactories();
        ArrayList<Tile> centreTiles = this.getCommon().getCentre().getTiles();
        if (centreTiles.size() > 0 && !(centreTiles.size() == 1
                && centreTiles.get(0).getColorCode() == 'f')) {
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

    public boolean isMoveValid(String[] gameState, String move) {
        Game game = new Game();
        game.reconstructCommonFrom(gameState[0]);
        game.reconstructBoardsFrom(gameState[1]);
        //it is not your turn.
        if (!String.valueOf(move.charAt(0)).equals(game.turn)) {
            return false;
        }
        //Drafting move
        if (move.length() == 4) {
            //1. The specified factory/centre contains at least one tile of the specified colour.
            if (move.charAt(1) == 'C') {
                if (!game.getCommon().getCentre().hasTile(move.charAt(2))) {
                    return false;
                }
            } else {
                //1. The specified factory/centre contains at least one tile of the specified colour.
                if (!game.getCommon().getFactories()[move.charAt(1) - 48].hasTile(move.charAt(2))) {
                    return false;
                }

            }
            if (move.charAt(3) != 'F') {
                //2. The storage row the tile is being placed in does not already contain a different colour.
                int row = Integer.parseInt(String.valueOf(move.charAt(3)));
                if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getStorage()
                        .getTriangle().get(row).size() != 0) {
                    if (!game.getPlayers()[move.charAt(0) - 'A'].getBoard().getStorage()
                            .hasTileSameColor(row, move.charAt(2))) {
                        return false;
                    }
                }
                //3. The corresponding mosaic row does not already contain a tile of that colour.
                for (int i = 0; i < 5; i++) {
                    if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .getSquare()[row][i] != null) {
                        if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                                .getSquare()[row][i].getColorCode() == move.charAt(2)) {
                            return false;
                        }
                    }
                }
            }
            //the tile may be placed on the floor.
            else {
                return true;
            }
        }
        //Tiling move 3 chars
        if (move.length() == 3) {
            //1. The specified row in the Storage area is full.
            int row = Integer.parseInt(String.valueOf(move.charAt(1)));
            if (move.charAt(2) != 'F') {
                int col = Integer.parseInt(String.valueOf(move.charAt(2)));

                if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getStorage()
                        .getTriangle().get(row).size() != 0) {
                    char code = game.getPlayers()[move.charAt(0) - 'A'].getBoard()
                            .getStorage().getTriangle().get(row).getFirst().getColorCode();
                    // The row of the Storage is full.
                    if (!game.getPlayers()[move.charAt(0) - 'A'].getBoard().getStorage()
                            .hasCompleteRow()) {
                        return false;
                    }
                    //2. The specified column does not already contain a tile of the same colour.
                    if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .columnHasSameColor(code, col)) {
                        return false;
                    }
                    //3. The specified location in the mosaic is empty.
                    return !game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .hasTile(row, col);


                } else {
                    return false;
                }
            }
            //4. If the specified column is 'F', no valid move exists from the specified row into the mosaic.
            else {
                for (int i = 0; i < 5; i++) {
                    // The row of Storage has Tiles.
                    if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getStorage()
                            .getTriangle().get(row).size() == 0) {
                        return false;
                    }
                    char code = game.getPlayers()[move.charAt(0) - 'A'].getBoard()
                            .getStorage().getTriangle().get(row).getFirst().getColorCode();
                    // The Mosaic has the same color.
                    if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .getSquare()[row][i] != null) {
                        if (game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                                .getSquare()[row][i].getColorCode() == code) {
                            return true;
                        }
                    }
                    // If the position has no tile determine whether column of it has tile with the same color.
                    if (!game.getPlayers()[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .hasTile(row, i)) {
                        int time = 0;
                        int num = 0;
                        for (int j = 0; j < 5; j++) {

                            if (game.getPlayers()[move.charAt(0) - 'A'].getBoard()
                                    .getMosaic().getSquare()[j][i] != null) {
                                num++;
                                if (game.getPlayers()[move.charAt(0) - 'A'].getBoard()
                                        .getMosaic().getSquare()[j][i].getColorCode() != code) {
                                    time++;
                                }
                            }

                        }
                        if (num == time) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isStateValid(String[] gameState) {
        boolean isStateValid = true;
        if (!isSharedStateWellFormed(gameState[0]) || !isPlayerStateWellFormed(gameState[1])) {
            isStateValid = false;
        }
        Pattern patternCommon = Pattern.compile(COMMON_REGEX);
        Matcher matcher = patternCommon.matcher(gameState[0]);
        Pattern patternPlayer = Pattern.compile(COMMON_REGEX);
        Matcher matcherPlayer = patternPlayer.matcher(gameState[1]);

        boolean matchFoundCommon = matcher.find();
        boolean matchFoundPlayer = matcherPlayer.find();
        if (matchFoundCommon && matchFoundPlayer) {
            String factoriesToken = matcher.group(2);
            String centreToken = matcher.group(4);
            String bagToken = matcher.group(5);
            String discardToken = matcher.group(7);

            String playerToken = matcherPlayer.group(1);
            String scoreToken = matcherPlayer.group(2);
            String mosaicToken = matcherPlayer.group(3);
            String storageToken = matcherPlayer.group(5);
            String floorToken = matcherPlayer.group(7);

            int aNum= 0; int bNum = 0; int cNum = 0; int dNum =0; int eNum =0; int fNum =0;
            aNum = Tile.tileNum(factoriesToken+centreToken,'a')+Tile.tileNum(bagToken,0)+Tile.tileNum(discardToken,0)
                    +Tile.tileNum(mosaicToken,'a')+Tile.tileNum(storageToken,'a',0)+Tile.tileNum(floorToken,'a');
            bNum = Tile.tileNum(factoriesToken+centreToken,'b')+Tile.tileNum(bagToken,2)+Tile.tileNum(discardToken,2)
                    +Tile.tileNum(mosaicToken,'b')+Tile.tileNum(storageToken,'b',0)+Tile.tileNum(floorToken,'b');
            cNum = Tile.tileNum(factoriesToken+centreToken,'c')+Tile.tileNum(bagToken,4)+Tile.tileNum(discardToken,4)
                    +Tile.tileNum(mosaicToken,'c')+Tile.tileNum(storageToken,'c',0)+Tile.tileNum(floorToken,'c');
            dNum = Tile.tileNum(factoriesToken+centreToken,'d')+Tile.tileNum(bagToken,6)+Tile.tileNum(discardToken,6)
                    +Tile.tileNum(mosaicToken,'d')+Tile.tileNum(storageToken,'d',0)+Tile.tileNum(floorToken,'d');
            eNum = Tile.tileNum(factoriesToken+centreToken,'e')+Tile.tileNum(bagToken,8)+Tile.tileNum(discardToken,8)
                    +Tile.tileNum(mosaicToken,'e')+Tile.tileNum(storageToken,'e',0)+Tile.tileNum(floorToken,'e');
            fNum = Tile.tileNum(centreToken+floorToken,'f');

            if (aNum >20 || bNum >20 || cNum>20 || dNum >20 || eNum>20|| fNum>1){
                isStateValid = false;
            }else if (!Mosaic.isMosaicValid(mosaicToken, storageToken)) {
                isStateValid = false;
            } else if (!Storage.isStorageValid(storageToken)) {
                isStateValid = false;
            } else if (Floor.isFloorValid(floorToken)) {
                isStateValid = false;
            } else if (!Centre.isCentreValid(centreToken, factoriesToken)) {
                isStateValid = false;
            }else if (!Factory.factoryValid(factoriesToken)) {
                isStateValid = false;
            }
        }
        return isStateValid;
    }

    // A1M e04    S 1b2 2c1 3a3 4a1 Fbeeee B0MS0c11b12e13d4Ff
    // A2M e04b11 S     2c1 3a3 4a1 Fbeeee B0MS0c11b12e13d4Ff


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
            if(!board.getStorage().hasCompleteRow()) {
                turn = (turn.equals("A")) ? "B" : "A";
            }
        } else if (move.length() == 4) {
            Player player = this.players[move.charAt(0) - 'A'];
            Board board = player.getBoard();
            char colorCode = move.charAt(2);
            String color = Tile.from(colorCode).getColor();
            int row = move.charAt(3) - '0';
            if (move.charAt(1) == 'C') {
                // draw tiles from centre
                ArrayDeque<Tile> tiles = player.drawTiles(color, false, 0, common);
                Tile firstPlayerTile = common.getCentre().getFirstPlayerTile();
                if (firstPlayerTile != null) {
                    // transfer first player tile
                    board.getFloor().placeFirstPlayerTile(firstPlayerTile, common.getDiscard());
                    common.getCentre().setFirstPlayerTile(null);
                }
                int tilesNum = (move.charAt(3) == 'F') ? 0 : tiles.size();
                board.getStorage().placeTiles(tiles, color, tilesNum, row
                        , board.getMosaic(), board.getFloor()
                        , common.getDiscard());

            } else {
                // draw tiles from factory
                int factoryNum = move.charAt(1) - '0';
                ArrayDeque<Tile> tiles = player.drawTiles(color, true, factoryNum, common);
                int tilesNum = (move.charAt(3) == 'F') ? 0 : tiles.size();
                board.getStorage().placeTiles(tiles, color, tilesNum, row
                        , board.getMosaic(), board.getFloor()
                        , common.getDiscard());
            }

            // check if goes to next phase
            int remaining = 0;
            for (Factory factory: common.getFactories()) {
                remaining += factory.getTiles().size();
            }
            remaining += common.getCentre().getTiles().size();
            if (remaining != 0) {
                turn = (turn.equals("A")) ? "B" : "A";
            }
        }

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
        game.reconstructCommonFrom("AF0CB1207080506D0107030805");
        game.reconstructBoardsFrom(
                "A21Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FeeeefB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2Fcc");
    }
}
