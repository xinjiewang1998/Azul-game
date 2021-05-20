package comp1110.ass2;

import comp1110.ass2.board.*;
import comp1110.ass2.common.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 */
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

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }





    /**
     * Turn state string to common objects
     *
     * @param sharedState the state string
     * @Author:  Jiaan Guo
     */
    public void reconstructCommonFrom(String sharedState) {
        Pattern pattern = Pattern.compile(COMMON_REGEX);
        Matcher matcher = pattern.matcher(sharedState);
        boolean matchFound = matcher.find();
        if (matchFound) {
//            System.out.println("Found Common: " + matcher.group(0));

            this.turn = matcher.group(1);

            String factoryStates = matcher.group(2);
            String centreState = matcher.group(4);
            String bagState = matcher.group(5);//2020202020
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

    /**
     * Turn player string to boards objects
     *
     * @param playerState
     * @Author:  Jiaan Guo
     */
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

    /**
     * Turn objects back to string
     *
     * @return State string
     * @Author:  Jiaan Guo
     */
    public String[] rebuildStateString() {
        String[] gameState = new String[2];
        gameState[0] = turn + common.toString();
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : players) {
            stringBuilder.append(player.toString());
        }
        gameState[1] = stringBuilder.toString();
        return gameState;
    }
    /**
     * Given a shared state string, determine if it is well-formed.
     * Note: you don't need to consider validity for this task.
     * A sharedState is well-formed if it satisfies the following conditions.
     * <p>
     * [turn][factories][centre][bag][discard]
     * where [turn][factories], [centre], [bag] and [discard] are replaced by the
     * corresponding small string as described below.
     * <p>
     * 0. [turn] The Turn substring is one character 'A'-'D' representing a
     * player, which indicates that it is this player's turn to make the next
     * drafting move. (In a two-player game, the turn substring can only take
     * the values 'A' or 'B').
     * <p>
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
     * <p>
     * 2. [centre] The centre substring starts with a 'C'
     * This is followed by *up to* 15 characters.
     * Each character is 'a' to 'e', alphabetically - representing a tile
     * in the centre.
     * The centre string is sorted alphabetically.
     * For example: "Caaabcdde" The Centre contains three 'a' tiles, one 'b'
     * tile, one 'c' tile, two 'd' tile and one 'e' tile.
     * <p>
     * 3. [bag] The bag substring starts with a 'B'
     * and is followed by 5 2-character substrings
     * 1st substring represents the number of 'a' tiles, from 0 - 20.
     * 2nd substring represents the number of 'b' tiles, from 0 - 20.
     * 3rd substring represents the number of 'c' tiles, from 0 - 20.
     * 4th substring represents the number of 'd' tiles, from 0 - 20.
     * 5th substring represents the number of 'e' tiles, from 0 - 20.
     * <p>
     * For example: "B0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     * 4. [discard] The discard substring starts with a 'D'
     * and is followed by 5 2-character substrings defined the same as the
     * bag substring.
     * For example: "D0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     *
     * @param sharedState the shared state - factories, bag and discard.
     * @return true if sharedState is well-formed, otherwise return false
     * TASK 2
     * @Author:  Jiaan Guo
     */
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
    /**
     * Given a playerState, determine if it is well-formed.
     * Note: you don't have to consider validity for this task.
     * A playerState is composed of individual playerStrings.
     * A playerState is well-formed if it satisfies the following conditions.
     * <p>
     * A playerString follows this pattern: [player][score][mosaic][storage][floor]
     * where [player], [score], [mosaic], [storage] and [floor] are replaced by
     * a corresponding substring as described below.
     * Each playerString is sorted by Player i.e. Player A appears before Player B.
     * <p>
     * 1. [player] The player substring is one character 'A' to 'D' -
     * representing the Player
     * <p>
     * 2. [score] The score substring is one or more digits between '0' and '9' -
     * representing the score
     * <p>
     * 3. [mosaic] The Mosaic substring begins with a 'M'
     * Which is followed by *up to* 25 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is 'a' to 'e' - representing the tile colour.
     * 2nd character is '0' to '4' - representing the row.
     * 3rd character is '0' to '4' - representing the column.
     * The Mosaic substring is ordered first by row, then by column.
     * That is, "a01" comes before "a10".
     * <p>
     * 4. [storage] The Storage substring begins with an 'S'
     * and is followed by *up to* 5 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is '0' to '4' - representing the row - each row number must only appear once.
     * 2nd character is 'a' to 'e' - representing the tile colour.
     * 3rd character is '0' to '5' - representing the number of tiles stored in that row.
     * Each 3-character string is ordered by row number.
     * <p>
     * 5. [floor] The Floor substring begins with an 'F'
     * and is followed by *up to* 7 characters in alphabetical order.
     * Each character is 'a' to 'f' - where 'f' represents the first player token.
     * There is only one first player token.
     * <p>
     * An entire playerState for 2 players might look like this:
     * "A20Ma02a13b00e42S2a13e44a1FaabbeB30Mc01b11d21S0e12b2F"
     * If we split player A's string into its substrings, we get:
     * [A][20][Ma02a13b00e42][S2a13e44a1][Faabbe].
     *
     * @param playerState the player state string
     * @return True if the playerState is well-formed,
     * false if the playerState is not well-formed
     * TASK 3
     * @Author:  Jiaan Guo
     */
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

                //System.out.println(matchToken);
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

    /**
     * Given the gameState, draw a *random* tile from the bag.
     * If the bag is empty, refill the the bag with the discard pile and then draw a tile.
     * If the discard pile is also empty, return 'Z'.
     *
     * @param gameState the current game state
     * @return the tile drawn from the bag, or 'Z' if the bag and discard pile are empty.
     * @Author: Xinjie Wang
     * TASK 5
     */
    public char drawTileFromBag(String[] gameState) {
        //task 5
        this.reconstructCommonFrom(gameState[0]);
        // draw tile
        Tile tile = common.getBag().drawTile(common.getDiscard());
        if (tile == null) {
            return 'Z';
        } else {
            return tile.getColorCode();
        }
    }
    /**
     * Given a state, refill the factories with tiles.
     * If the factories are not all empty, return the given state.
     *
     * @param gameState the state of the game.
     * @return the updated state after the factories have been filled or
     * the given state if not all factories are empty.
     * @Author: Xinjie Wang
     * TASK 6
     */
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
    /**
     * Given a gameState for a completed game,
     * return bonus points for rows, columns, and sets.
     *
     * @param gameState a completed game state
     * @param player    the player for whom the score is to be returned
     * @return the number of bonus points awarded to this player for rows,
     * columns, and sets
     * @Author: Xinjie Wang
     * TASK 7
     */
    public int getBonusPoints(String[] gameState, char player) {
        //task 7
        this.reconstructCommonFrom(gameState[0]);
        this.reconstructBoardsFrom(gameState[1]);
        return players[player - 'A'].getBoard().getMosaic().calculateBonusScore().getScore();
    }
    /**
     * Given a valid gameState prepare for the next round.
     * 1. Empty the floor area for each player and adjust their score accordingly (see the README).
     * 2. Refill the factories from the bag.
     * * If the bag is empty, refill the bag from the discard pile and then
     * (continue to) refill the factories.
     * * If the bag and discard pile do not contain enough tiles to fill all
     * the factories, fill as many as possible.
     * * If the factories and centre contain tiles other than the first player
     * token, return the current state.
     *
     * @param gameState the game state
     * @return the state for the next round.
     * TASK 8
     * @Author: Xinjie Wang
     */
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

        // prepare next round
        // change turn
        for (Player player : players) {
            if (player.getBoard().getFloor().hasFirstPlayerTile()) {
                turn = String.valueOf(player.getId());
            }
        }

        // calculate score
        for (Player player : players) {
            Board playerBoard = player.getBoard();
            playerBoard.getScore().addScore(playerBoard.getFloor().calculatePenalty());
            playerBoard.getScore().addScore(playerBoard.getMosaic().calculateBonusScore());
            playerBoard.getFloor().clearTiles(common.getDiscard(), common.getCentre());
        }

        gameState = rebuildStateString();
        // terminate game
        for (Player value : players) {
            if (value.getBoard().getMosaic().hasCompleteRow()) {
                return gameState;
            }
        }

        // refill tiles
        for (Factory factory : factories) {
            factory.refillTiles(common.getBag(), common.getDiscard());
        }

        gameState = rebuildStateString();
        return gameState;
    }
    /**
     * Given an entire game State, determine whether the state is valid.
     * A game state is valid if it satisfies the following conditions.
     * <p>
     * [General]
     * 1. The game state is well-formed.
     * 2. There are no more than 20 of each colour of tile across all player
     * areas, factories, bag and discard
     * 3. Exactly one first player token 'f' must be present across all player
     * boards and the centre.
     * <p>
     * [Mosaic]
     * 1. No two tiles occupy the same location on a single player's mosaic.
     * 2. Each row contains only 1 of each colour of tile.
     * 3. Each column contains only 1 of each colour of tile.
     * [Storage]
     * 1. The maximum number of tiles stored in a row must not exceed (row_number + 1).
     * 2. The colour of tile stored in a row must not be the same as a colour
     * already found in the corresponding row of the mosaic.
     * <p>
     * [Floor]
     * 1. There are no more than 7 tiles on a single player's floor.
     * [Centre]
     * 1. The number of tiles in the centre is no greater than 3 * the number of empty factories.
     * [Factories]
     * 1. At most one factory has less than 4, but greater than 0 tiles.
     * Any factories with factory number greater than this factory must contain 0 tiles.
     *
     * @param gameState array of strings representing the game state.
     *                  state[0] = sharedState
     *                  state[1] = playerStates
     * @return true if the state is valid, false if it is invalid.
     * TASK 9
     * @Author: Xinjie Wang
     */
    public boolean isStateValid(String[] gameState) {
        // task 9

        //1.The game state is well-formed.
        if (!isSharedStateWellFormed(gameState[0]) || !isPlayerStateWellFormed(gameState[1])) {
            return false;
        }
        this.reconstructCommonFrom(gameState[0]);
        this.reconstructBoardsFrom(gameState[1]);

        //There are no more than 20 of each colour of tile across all player areas, factories, bag and discard
        for (int i = 0; i < 5; i++) {
            int count = 0;
            char colorCode = (char) ('a' + i);
            for (Factory factory : common.getFactories()) {
                count += factory.countTile(colorCode);
            }
            count += this.common.getBag().countTile(colorCode) + this.common.getCentre()
                    .countTile(colorCode) +
                    this.common.getDiscard().countTile(colorCode);
            for (Player player : players) {
                Board board = player.getBoard();
                count += board.getFloor().countTile(colorCode)
                        + board.getMosaic().countTile(colorCode)
                        + board.getStorage().countTile(colorCode);
            }
            if (count != 20) {
                return false;
            }
        }
        //check the Mosaic and Storage whether has a same colour
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                for (Player player : players) {
                    Board board = player.getBoard();
                    if (board.getStorage().getTriangle().get(row).size() != 0 &&
                            board.getMosaic().getSquare()[row][column] != null) {
                        if (board.getStorage().getTriangle().get(row).getFirst().getColorCode()
                                == board.getMosaic().getSquare()[row][column].getColorCode()) {
                            return false;
                        }
                    }
                }
            }
        }
        //The maximum number of tiles stored in a row must not exceed (row_number + 1).
        for (int row = 0; row < 5; row++) {
            for (Player player : players) {
                if (player.getBoard().getStorage().getTriangle().get(row).size() > row + 1) {
                    return false;
                }
            }
        }

        return true;
    }
    /**
     * Given a valid gameState and a move, determine whether the move is valid.
     * A Drafting move is a 4-character String.
     * A Drafting move is valid if it satisfies the following conditions:
     * <p>
     * 1. The specified factory/centre contains at least one tile of the specified colour.
     * 2. The storage row the tile is being placed in does not already contain a different colour.
     * 3. The corresponding mosaic row does not already contain a tile of that colour.
     * Note that the tile may be placed on the floor.
     * </p>
     * <p>
     * A Tiling move is a 3-character String.
     * A Tiling move is valid if it satisfies the following conditions:
     * 1. The specified row in the Storage area is full.
     * 2. The specified column does not already contain a tile of the same colour.
     * 3. The specified location in the mosaic is empty.
     * 4. If the specified column is 'F', no valid move exists from the
     * specified row into the mosaic.
     * </p>
     *
     * @param gameState the game state.
     * @param move      A string representing a move.
     * @return true if the move is valid, false if it is invalid.
     * @Author: Xinjie Wang
     * TASK 10
     */
    public boolean isMoveValid(String[] gameState, String move) {
        //task 10
        reconstructCommonFrom(gameState[0]);
        reconstructBoardsFrom(gameState[1]);
        //it is not your turn.
        if (!String.valueOf(move.charAt(0)).equals(turn)) {
            return false;
        }
        //Drafting move
        if (move.length() == 4) {
            //1. The specified factory/centre contains at least one tile of the specified colour.
            if (move.charAt(1) == 'C') {
                if (!common.getCentre().hasTile(move.charAt(2))) {
                    return false;
                }
            } else {
                //1. The specified factory/centre contains at least one tile of the specified colour.
                if (!common.getFactories()[move.charAt(1) - 48].hasTile(move.charAt(2))) {
                    return false;
                }

            }
            if (move.charAt(3) != 'F') {
                //2. The storage row the tile is being placed in does not already contain a different colour.
                int row = Integer.parseInt(String.valueOf(move.charAt(3)));
                if (players[move.charAt(0) - 'A'].getBoard().getStorage()
                        .getTriangle().get(row).size() != 0) {
                    if (!players[move.charAt(0) - 'A'].getBoard().getStorage()
                            .rowHasSameColor(row, move.charAt(2))) {
                        return false;
                    }
                }
                //3. The corresponding mosaic row does not already contain a tile of that colour.
                for (int i = 0; i < 5; i++) {
                    if (players[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .getSquare()[row][i] != null) {
                        if (players[move.charAt(0) - 'A'].getBoard().getMosaic()
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
            for (Factory factory : common.getFactories()) {
                if (!factory.getTiles().isEmpty()) {
                    return false;
                }
            }
            if (!common.getCentre().getTiles().isEmpty()) {
                return false;
            }
            //1. The specified row in the Storage area is full.
            int row = Integer.parseInt(String.valueOf(move.charAt(1)));
            if (move.charAt(2) != 'F') {
                int col = Integer.parseInt(String.valueOf(move.charAt(2)));

                if (players[move.charAt(0) - 'A'].getBoard().getStorage()
                        .getTriangle().get(row).size() != 0) {
                    char code = getPlayers()[move.charAt(0) - 'A'].getBoard()
                            .getStorage().getTriangle().get(row).getFirst().getColorCode();
                    // The row of the Storage is full.
                    if (!players[move.charAt(0) - 'A'].getBoard().getStorage()
                            .hasCompleteRow()) {
                        return false;
                    }
                    //2. The specified column does not already contain a tile of the same colour.
                    if (players[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .columnHasSameColor(code, col)) {
                        return false;
                    }
                    //3. The specified location in the mosaic is empty.
                    return !players[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .hasTile(row, col);


                } else {
                    return false;
                }
            }
            //4. If the specified column is 'F', no valid move exists from the specified row into the mosaic.
            else {
                for (int i = 0; i < 5; i++) {
                    // The row of Storage has Tiles.
                    if (players[move.charAt(0) - 'A'].getBoard().getStorage()
                            .getTriangle().get(row).size() == 0) {
                        return false;
                    }
                    char code = players[move.charAt(0) - 'A'].getBoard()
                            .getStorage().getTriangle().get(row).getFirst().getColorCode();
                    // The Mosaic has the same color.
                    if (players[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .getSquare()[row][i] != null) {
                        if (players[move.charAt(0) - 'A'].getBoard().getMosaic()
                                .getSquare()[row][i].getColorCode() == code) {
                            return true;
                        }
                    }
                    // If the position has no tile determine whether column of it has tile with the same color.
                    if (!players[move.charAt(0) - 'A'].getBoard().getMosaic()
                            .hasTile(row, i)) {
                        int time = 0;
                        int num = 0;
                        for (int j = 0; j < 5; j++) {

                            if (players[move.charAt(0) - 'A'].getBoard()
                                    .getMosaic().getSquare()[j][i] != null) {
                                num++;
                                if (players[move.charAt(0) - 'A'].getBoard()
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
    /**
     * Given a gameState and a move, apply the move to the gameState.
     * If the move is a Tiling move, you must also update the player's score.
     * If the move is a Tiling move, you must also empty the remaining tiles
     * into the discard.
     * If the move is a Drafting move, you must also move any remaining tiles
     * from the specified factory into the centre.
     * If the move is a Drafting move and you must put tiles onto the floor,
     * any tiles that cannot fit on the floor are placed in the discard with
     * the following exception:
     * If the first player tile would be placed into the discard, it is instead
     * If the first player tile would be placed into the discard, it is instead
     * alphabetically.
     *
     * @param gameState the game state.
     * @param move      A string representing a move.
     * @return the updated gameState after the move has been applied.
     * TASK 11
     * @Author:  Jiaan Guo
     */
    public String[] applyMove(String[] gameState, String move) {
        // task 11
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
            if (!board.getStorage().hasCompleteRow()) {
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
            for (Factory factory : common.getFactories()) {
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
    /**
     * Given a valid game state, return a valid move.
     *
     * @param gameState the game state
     * @return a move for the current game state.
     * TASK 13 @Author: Xinjie Wang
     */
    public String generateAction(String[] gameState) {
        // task 13
        Game game = new Game();
        game.reconstructCommonFrom(gameState[0]);
        game.reconstructBoardsFrom(gameState[1]);
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (game.getCommon().getFactories()[i].getTiles().size() == 0) {
                count++;
            }
        }

        if (count != 5 || !(game.getCommon().getCentre().getTiles().size() == 0 || (
                game.getCommon().getCentre().getTiles().size() == 1
                        && game.getCommon().getCentre().getTiles().get(0).getColorCode() == 'f'))) {
            Random r = new Random();

            if (count == 5) {
                int num = r.nextInt(game.getCommon().getCentre().getTiles().size());
                Tile tile = game.getCommon().getCentre().getTiles().get(num);
                ArrayList<Integer> numRow = new ArrayList<Integer>();
                numRow = game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                        .rowsCanBePlacedOn(game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                .getMosaic(), tile.getColorCode());
                if (numRow.size() != 0) {
                    int row = r.nextInt(numRow.size());
                    return game.turn + "C" + tile.getColorCode() + numRow.get(row);
                } else {
                    return game.turn + "C" + tile.getColorCode() + "F";
                }
            } else if (game.getCommon().getCentre().getTiles().size() == 0 || (
                    game.getCommon().getCentre().getTiles().size() == 1
                            && game.getCommon().getCentre().getTiles().get(0).getColorCode()
                            == 'f')) {
                int num = 0;
                int i = 0;
                while (i < 1000) {
                    num = r.nextInt(game.getCommon().getFactories().length);
                    if ((game.getCommon().getFactories()[num].getTiles().size() != 0)) {
                        break;
                    }
                    i++;
                }
                int num1 = r.nextInt(game.getCommon().getFactories()[num].getTiles().size());
                Tile tile = game.getCommon().getFactories()[num].getTiles().get(num1);
                ArrayList<Integer> numRow = new ArrayList<Integer>();
                numRow = game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                        .rowsCanBePlacedOn(game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                .getMosaic(), tile.getColorCode());
                if (numRow.size() != 0) {
                    int row = r.nextInt(numRow.size());
                    return game.turn + num + tile.getColorCode() + numRow.get(row);
                } else {
                    return game.turn + num + tile.getColorCode() + "F";
                }
            } else {
                int choose = r.nextInt(2);
                if (choose == 0) {
                    int num = 0;
                    int i = 0;
                    while (i < 1000) {
                        num = r.nextInt(game.getCommon().getFactories().length);
                        if ((game.getCommon().getFactories()[num].getTiles().size() != 0)) {
                            break;
                        }
                        i++;
                    }
                    int num1 = r.nextInt(game.getCommon().getFactories()[num].getTiles().size());
                    Tile tile = game.getCommon().getFactories()[num].getTiles().get(num1);
                    ArrayList<Integer> numRow = new ArrayList<Integer>();
                    numRow = game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                            .rowsCanBePlacedOn(
                                    game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                            .getMosaic(), tile.getColorCode());
                    if (numRow.size() != 0) {
                        int row = r.nextInt(numRow.size());
                        return game.turn + num + tile.getColorCode() + numRow.get(row);
                    } else {
                        return game.turn + num + tile.getColorCode() + "F";
                    }
                } else {
                    int num = r.nextInt(game.getCommon().getCentre().getTiles().size());
                    Tile tile = game.getCommon().getCentre().getTiles().get(num);
                    ArrayList<Integer> numRow = new ArrayList<Integer>();
                    numRow = game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                            .rowsCanBePlacedOn(
                                    game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                            .getMosaic(), tile.getColorCode());
                    if (numRow.size() != 0) {
                        int row = r.nextInt(numRow.size());
                        return game.turn + "C" + tile.getColorCode() + numRow.get(row);
                    } else {
                        return game.turn + "C" + tile.getColorCode() + "F";
                    }
                }
            }
        } else if (game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                .hasCompleteRow()) {
            for (int i = 0; i < 5; i++) {
                if (game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getStorage()
                        .getTriangle().get(i).size() == i + 1) {
                    int col = 0;
                    for (int j = 0; j < 5; j++) {
                        char code = game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                .getStorage().getTriangle().get(i).getFirst().getColorCode();
                        if (game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard().getMosaic()
                                .getSquare()[i][j] == null) {
                            if (!game.getPlayers()[(game.turn.charAt(0) - 'A')].getBoard()
                                    .getMosaic().columnHasSameColor(code, j)) {
                                col = j;
                                break;
                            }
                        }
                    }
                    return game.turn + i + col;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
         Game game = new Game();
         game.reconstructCommonFrom("BFCfB0000000000D1112141413");
         game.reconstructBoardsFrom("A164159Mc00d01b02e03a04a10c11d12b13e14d20c23b30e31c32a42S3a24b2FB369270Md00e01a02b03a10c11d12e13b14e20b21c22d31a41S2a14e1F");
         System.out.println(game.generateAction(game.rebuildStateString()));
        System.out.println(game.isStateValid(game.rebuildStateString()));

    }
}
