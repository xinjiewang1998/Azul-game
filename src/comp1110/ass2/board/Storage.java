package comp1110.ass2.board;

import comp1110.ass2.Tile;

import comp1110.ass2.common.Discard;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Storage {


    private final int NUM_ROWS = 5;
    private final int[] MAX_LENGTH = new int[]{1, 2, 3, 4, 5};


    // ArrayList is better for access each elements,
    // and ArrayDeque has convenient pop methods.
    // better use ArrayDeque when all elements has same color.
    private ArrayList<ArrayDeque<Tile>> triangle;

    public Storage() {
        triangle = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            triangle.add(new ArrayDeque<>());
        }
    }

    public ArrayList<ArrayDeque<Tile>> getTriangle() {
        return triangle;
    }

    public void setTriangle(ArrayList<ArrayDeque<Tile>> triangle) {
        this.triangle = triangle;
    }

    /**
     * Check if has complete row.
     *
     * @return if has complete row
     */
    public boolean hasCompleteRow() {
        for (int i = 0; i < triangle.size(); i++) {
            if (triangle.get(i).size() == i + 1) {
                return true;
            }
        }
        return false;
    }


    /**
     * Rules to place the tiles. 1. If a row already contains tiles, you may only add tiles of the
     * same colour to it. 2. If you have more tiles than can fit in your chosen row, then you must
     * place the excess tiles on the floor. 3. You are not allowed to place tiles of a certain
     * colour in a row if the corresponding mosaic row already contains a tile of that colour. 4. If
     * you cannot or do not want to place tiles on a row, you may place them directly onto the
     * floor.
     *
     * @param tiles    the tiles to be placed
     * @param color    the tiles' color
     * @param tilesNum the number of tiles to be placed on mosaic (not floor!)
     * @param rowNum   the row number to place tiles
     * @param mosaic   the mosaic to check allowance
     * @param floor    the floor to have the remaining tiles.
     * @return true is successfully place tiles
     */
    public boolean placeTiles(ArrayDeque<Tile> tiles, String color, int tilesNum, int rowNum,
            Mosaic mosaic, Floor floor) {
        ArrayDeque<Tile> row = triangle.get(rowNum);
        Tile firstTile = row.peekFirst();
        if (firstTile != null) {
            if (!firstTile.getColor().equals(color)) {
                return false;
            }
        } else {
            if (mosaic.hasColor(color, rowNum)) {
                return false;
            }
        }

        int totalPossible = tilesNum + row.size();
        int maxPossible = Math.min(totalPossible, MAX_LENGTH[rowNum]);
        for (int j = row.size(); j < maxPossible; j++) {
            row.push(tiles.pop());
        }
        if (tiles.size() != 0) {
            floor.placeTiles(tiles);
        }
        return true;
    }

    /**
     * Tile and Score Tile rules 1. Go through your storage rows from row 0 to row 4 and move the
     * rightmost tile of each complete row to the space of the same colour in the corresponding row
     * of the mosaic. 2. Empty any row that no longer has a tile in the rightmost space and place
     * all remaining tiles in the discard pile. Any tiles that remain in incomplete rows on your
     * board remain for the next round.
     *
     * @return the score
     */
    public Score tileAndScore(Mosaic mosaic, Discard discard) {
        Score total = new Score(0);
        for (int i = 0; i < NUM_ROWS; i++) {
            ArrayDeque<Tile> row = triangle.get(i);
            if (MAX_LENGTH[i] == row.size()) {

                // tiling
                Score score = mosaic.placeTile(row.pop(), i);
                discard.placeTiles(row);
                row.clear();

                // scoring
                total.addScore(score);
            }
        }
        return total;
    }


    /**
     * The Storage substring begins with an 'S' and is followed by *up to* 5 3-character strings.
     * Each 3-character string is defined as follows: 1st character is '0' to '4' - representing the
     * row - each row number must only appear once. 2nd character is 'a' to 'e' - representing the
     * tile colour. 3rd character is '0' to '5' - representing the number of tiles stored in that
     * row. Each 3-character string is ordered by row number.
     *
     * @param token the storage string
     * @return true if is well formed storage string
     */
    public static boolean isWellFormedStorageString(String token) {
        if (token == null || token.length() % 3 != 0 || token.length() > 5 * 3) {
            return false;
        }

        for (int i = 0; i < token.length(); i++) {
            char row = token.charAt(i);
            char color = token.charAt(++i);
            char num = token.charAt(++i);
            // only five possible rows
            if (row != '0' && row != '1' && row != '2' && row != '3' && row != '4') {
                return false;
            }
            // only five different colors
            if (color != 'a' && color != 'b' && color != 'c' && color != 'd' && color != 'e') {
                return false;
            }
            // only six possible numbers
            if (num != '0' && num != '1' && num != '2' && num != '3' && num != '4'
                    && num != '5') {
                return false;
            }
            // ordered by row number
            if (i > 3 && (row - token.charAt(i - 5) <= 0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of storage state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedStorageString(token)) {
            return;
        }
        triangle = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            triangle.add(new ArrayDeque<>());
        }

        for (int i = 0; i < token.length(); i += 3) {
            int row = token.charAt(i) - 48;
            int num = token.charAt(i + 2) - 48;
            for (int j = 0; j < num; j++) {
                triangle.get(row).add(Tile.from(token.charAt(i + 1)));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < NUM_ROWS; i++) {
            if (triangle.get(i).size() != 0) {
                stringBuilder.append(i);
                stringBuilder.append(triangle.get(i).getFirst().getColorCode());
                stringBuilder.append(triangle.get(i).size());
            }
        }
        return stringBuilder.toString();
    }
}
