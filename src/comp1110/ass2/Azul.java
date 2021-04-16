package comp1110.ass2;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Factory;

import java.util.ArrayList;
import java.util.Random;
>>>>>>> a91385b2ffdf018acb34b1448499b292d5f24f2f

public class Azul {
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
     */
    public static boolean isSharedStateWellFormed(String sharedState) {
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
                if(!isWellFormedCentreString(chars)) {
                    return false;
                }
                chars.clear();
            } else if (i > indexB && i < indexD) {
                chars.add(sharedState.charAt(i));
            } else if (i == indexD) {
                if(!isWellFormedBagString(chars)) {
                    return false;
                }
                chars.clear();
            } else if (i > indexD) {
                chars.add(sharedState.charAt(i));
            }
        }
        if(!isWellFormedDiscardString(chars)){
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
    private static boolean isWellFormedFactoryString(ArrayList<Character> chars) {
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

    /**
     * 2. [centre] The centre substring starts with a 'C'
     * This is followed by *up to* 15 characters.
     * Each character is 'a' to 'e', alphabetically - representing a tile
     * in the centre.
     * The centre string is sorted alphabetically.
     * For example: "Caaabcdde" The Centre contains three 'a' tiles, one 'b'
     * tile, one 'c' tile, two 'd' tile and one 'e' tile.
     */
    private static boolean isWellFormedCentreString(ArrayList<Character> chars) {
        if (chars.size() > 15) {
            return false;
        }
        int fCount = 0;
        for (int i = 0; i < chars.size(); i++) {
            char c = chars.get(i);
            char prevChar, currChar;
            if (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e') {
                if (i != 0 && (c - chars.get(i-1) < 0)) {
                        return false;
                }
            } else if (c == 'f') {
                fCount++;
                if(fCount > 1) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 3. [bag] The bag substring starts with a 'B'
     * and is followed by 5 2-character substrings
     * 1st substring represents the number of 'a' tiles, from 0 - 20.
     * 2nd substring represents the number of 'b' tiles, from 0 - 20.
     * 3rd substring represents the number of 'c' tiles, from 0 - 20.
     * 4th substring represents the number of 'd' tiles, from 0 - 20.
     * 5th substring represents the number of 'e' tiles, from 0 - 20.
     * For example: "B0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     */
    private static boolean isWellFormedBagString(ArrayList<Character> chars) {
        if (chars.size() != 10) {
            return false;
        } else {
            for(int i = 0; i < 10; i++) {
                String s = chars.get(i).toString() + chars.get(++i).toString();
                try {
                    int num = Integer.parseInt(s);
                    if (num < 0 || num > 20) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }
        }
        return true;
<<<<<<< HEAD
    }

    /**
     * 4. [discard] The discard substring starts with a 'D'
     * and is followed by 5 2-character substrings defined the same as the
     * bag substring.
     * For example: "D0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     */
    private static boolean isWellFormedDiscardString(ArrayList<Character> chars) {
        if (chars.size() != 10) {
            return false;
        } else {
            for(int i = 0; i < 10; i++) {
                String s = chars.get(i).toString() + chars.get(++i).toString();
                try {
                    int num = Integer.parseInt(s);
                    if (num < 0 || num > 20) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }
        }
        return true;
=======
>>>>>>> a91385b2ffdf018acb34b1448499b292d5f24f2f
    }


    /**
     * 4. [discard] The discard substring starts with a 'D'
     * and is followed by 5 2-character substrings defined the same as the
     * bag substring.
     * For example: "D0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     */
    private static boolean isWellFormedDiscardString(ArrayList<Character> chars) {
        if (chars.size() != 10) {
            return false;
        } else {
            for(int i = 0; i < 10; i++) {
                String s = chars.get(i).toString() + chars.get(++i).toString();
                try {
                    int num = Integer.parseInt(s);
                    if (num < 0 || num > 20) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }
        }
        return true;
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
     */
    public static boolean isPlayerStateWellFormed(String playerState) {
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
                    if (!isMosaicWellFormedString(chars)) {
                        return false;
                    }
                    chars.clear();
                } else if (i > indexS && i < indexF) {
                    chars.add(sub.charAt(i));
                } else if (i == indexF) {
                    if (!isStorageWellFormedString(chars)) {
                        return false;
                    }
                    chars.clear();
                } else if (i > indexF) {
                    chars.add(sub.charAt(i));
                }
            }
            if (!isFloorWellFormedString(chars)) {
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
    public static boolean isPlayerAndScoreWellFormedString(ArrayList<Character> playerWithScore) {
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

    /**
     * 3. [mosaic] The Mosaic substring begins with a 'M'
     * Which is followed by *up to* 25 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is 'a' to 'e' - representing the tile colour.
     * 2nd character is '0' to '4' - representing the row.
     * 3rd character is '0' to '4' - representing the column.
     * The Mosaic substring is ordered first by row, then by column.
     * That is, "a01" comes before "a10".
     */
    public static boolean isMosaicWellFormedString(ArrayList<Character> mosaic) {
        if(mosaic.size() % 3 != 0 || mosaic.size() > 75) {
            return false;
        }

        for (int i = 0; i < mosaic.size(); i++) {
            char first = mosaic.get(i);
            char second = mosaic.get(++i);
            char third = mosaic.get(++i);
            if (first != 'a' && first != 'b' && first !='c' && first != 'd' && first != 'e') {
                return false;
            }
            if (second != '0' && second != '1' && second !='2' && second != '3' && second != '4') {
                return false;
            }
            if (third != '0' && third != '1' && third !='2' && third != '3' && third != '4') {
                return false;
            }
            if (i > 3 && (second - mosaic.get(i-4) < 0)) {
                 return false;
            }
            if (i > 3 && second == mosaic.get(i-4) && third - mosaic.get(i-3) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 4. [storage] The Storage substring begins with an 'S'
     * and is followed by *up to* 5 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is '0' to '4' - representing the row - each row number must only appear once.
     * 2nd character is 'a' to 'e' - representing the tile colour.
     * 3rd character is '0' to '5' - representing the number of tiles stored in that row.
     * Each 3-character string is ordered by row number.
     */
    public static boolean isStorageWellFormedString(ArrayList<Character> storage) {
        if(storage.size() % 3 != 0 || storage.size() > 15) {
            return false;
        }

        for (int i = 0; i < storage.size(); i++) {
            char first = storage.get(i);
            char second = storage.get(++i);
            char third = storage.get(++i);
            if (first != '0' && first != '1' && first !='2' && first != '3' && first != '4') {
                return false;
            }
            if (second != 'a' && second != 'b' && second !='c' && second != 'd' && second != 'e') {
                return false;
            }
            if (third != '0' && third != '1' && third !='2' && third != '3' && third != '4' && third != '5') {
                return false;
            }
            if (i > 3 && (first - storage.get(i-5) <= 0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 5. [floor] The Floor substring begins with an 'F'
     * and is followed by *up to* 7 characters in alphabetical order.
     * Each character is 'a' to 'f' - where 'f' represents the first player token.
     * There is only one first player token.
     */
    public static boolean isFloorWellFormedString(ArrayList<Character> floor) {
        if (floor.size() > 7) {
            return false;
        }
        int countF = 0;
        for (int i = 0; i < floor.size(); i++) {
            char c = floor.get(i);
            if (c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e' && c != 'f') {
                return false;
            }
            if(i != 0 && (c - floor.get(i-1) < 0)) {
                return false;
            }
            if(c == 'f') {
                countF += 1;
            }
        }
        if(countF > 1) {
            return false;
        }
        return true;
    }

    /**
     * Given the gameState, draw a *random* tile from the bag.
     * If the bag is empty, refill the the bag with the discard pile and then draw a tile.
     * If the discard pile is also empty, return 'Z'.
     *
     * @param gameState the current game state
     * @return the tile drawn from the bag, or 'Z' if the bag and discard pile are empty.
     * TASK 5
     */
    public static char drawTileFromBag(String[] gameState) {
        Bag bag = new Bag();
        return bag.drawTile(gameState);
    }

    /**
     * Given a state, refill the factories with tiles.
     * If the factories are not all empty, return the given state.
     *
     * @param gameState the state of the game.
     * @return the updated state after the factories have been filled or
     * the given state if not all factories are empty.
     * TASK 6
     */
    public static String[] refillFactories(String[] gameState) {
       Factory factory = new Factory();

        return factory.addTilesFromBag(gameState);
    }

    /**
     * Given a gameState for a completed game,
     * return bonus points for rows, columns, and sets.
     *
     * @param gameState a completed game state
     * @param player    the player for whom the score is to be returned
     * @return the number of bonus points awarded to this player for rows,
     * columns, and sets
     * TASK 7
     */
    public static int getBonusPoints(String[] gameState, char player) {
        Mosaic M = new Mosaic();
        return M.calculateBonusScore(gameState,player);
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
     */
    public static String[] nextRound(String[] gameState) {
        // FIXME TASK 8


        return null;
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
     */
    public static boolean isStateValid(String[] gameState) {
        // FIXME Task 9
        return false;
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
     * TASK 10
     */
    public static boolean isMoveValid(String[] gameState, String move) {
        // FIXME Task 10
        return false;
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
     * swapped with the last tile in the floor, when the floor is sorted
     * alphabetically.
     *
     * @param gameState the game state.
     * @param move      A string representing a move.
     * @return the updated gameState after the move has been applied.
     * TASK 11
     */
    public static String[] applyMove(String[] gameState, String move) {
        // FIXME Task 11
        return null;
    }

    /**
     * Given a valid game state, return a valid move.
     *
     * @param gameState the game state
     * @return a move for the current game state.
     * TASK 13
     */
    public static String generateAction(String[] gameState) {
        // FIXME Task 13
        return null;
        // FIXME Task 15 Implement a "smart" generateAction()
    }
}
