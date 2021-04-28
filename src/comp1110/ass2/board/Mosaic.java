package comp1110.ass2.board;

import comp1110.ass2.Tile;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Mosaic {

    private final int NUM_ROWS = 5;
    private final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(
            Arrays.asList("blue", "green", "orange", "purple", "red"));


    private Tile[][] square;
    private int[] colorCount;


    public Mosaic() {
        square = new Tile[NUM_ROWS][NUM_ROWS];
        colorCount = new int[]{0, 0, 0, 0, 0};
    }

    public Tile[][] getSquare() {
        return square;
    }

    public void setSquare(Tile[][] square) {
        this.square = square;
    }

    public int[] getColorCount() {
        return colorCount;
    }

    public void setColorCount(int[] colorCount) {
        this.colorCount = colorCount;
    }

    /**
     * calculate the column number according to the color and row number.
     *
     * @param color the target color
     * @param row   the target row number
     * @return the column number
     */
    public int calculateColumn(String color, int row) {
        return (row + DEFAULT_COLORS.indexOf(color)) % NUM_ROWS;
    }

    /**
     * tells whether a colored tile has occupied that row. if it is true, cannot put tile with same
     * color on that row again.
     *
     * @param color the target color
     * @param row   the target row
     * @return true if the color exists in mosaic
     */
    public boolean hasColor(String color, int row) {
        int column = (row + DEFAULT_COLORS.indexOf(color)) % NUM_ROWS;
        return (square[row][column] != null);
    }

    public boolean columnHasSameColor(char code,int col){
        for (int i = 0; i < 5; i++) {
            if (this.getSquare()[i][col] != null) {
                if (this.getSquare()[i][col].getColorCode() == code) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * check if has tile at specific position
     *
     * @param rowNum    row number
     * @param columnNum column number
     * @return true if it has tile at that position
     */
    public boolean hasTile(int rowNum, int columnNum) {
        return square[rowNum][columnNum] != null;
    }

    /**
     * find the correct position and place the tile
     *
     * @param tile the tile to be placed
     * @param row  the target row
     * @return the score earned for this move
     */
    public Score placeTile(Tile tile, int row) {
        int index = DEFAULT_COLORS.indexOf(tile.getColor());
        return placeTile(tile, row, (row + index) % NUM_ROWS);
    }


    public Score placeTile(Tile tile, int row, int column) {

        square[row][column] = tile;
//        colorCount[index]++;

        int count = 1;
        boolean hasHorizontallyLinkedTiles = false;
        boolean hasVerticallyLinkedTiles = false;
        // up
        for (int i = row - 1; i >= 0; i--) {
            if (square[i][column] != null) {
                hasVerticallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // down
        for (int i = row + 1; i < NUM_ROWS; i++) {
            if (square[i][column] != null) {
                hasVerticallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // left
        for (int i = column - 1; i >= 0; i--) {
            if (square[row][i] != null) {
                hasHorizontallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // right
        for (int i = column + 1; i < NUM_ROWS; i++) {
            if (square[row][i] != null) {
                hasHorizontallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        return (hasHorizontallyLinkedTiles && hasVerticallyLinkedTiles) ? new Score(count + 1) :
                new Score(count);
    }

    /**
     * check if there is a complete row in our mosaic if it has, then end the game.
     *
     * @return true if exists a complete row
     */
    public boolean hasCompleteRow() {
        for (int i = 0; i < NUM_ROWS; i++) {
            boolean allFilled = true;
            for (int j = 0; j < NUM_ROWS; j++) {
                if (square[i][j] == null) {
                    allFilled = false;
                    break;
                }
            }
            if (allFilled) {
                return true;
            }
        }
        return false;
    }

    /**
     * Each player gains additional bonus points if they satisfy the following conditions:
     * <p>
     * Gain 2 points for each complete row of your mosaic (5 consecutive horizontal tiles). Gain 7
     * points for each complete column of your mosaic (5 consecutive vertical tiles). Gain 10 points
     * for each colour of tile for which you have placed all 5 tiles on your mosaic.
     *
     * @return the Bonus Score
     */
    public Score calculateBonusScore() {
        int total = 0;
        for (int i = 0; i < NUM_ROWS; i++) {
            boolean rowFilled = true;
            boolean colFilled = true;
            for (int j = 0; j < NUM_ROWS; j++) {
                if (square[i][j] == null) {
                    rowFilled = false;
                    break;
                }
            }
            if (rowFilled) {
                total += 2;
            }
            for (int j = 0; j < NUM_ROWS; j++) {
                if (square[j][i] == null) {
                    colFilled = false;
                    break;
                }
            }
            if (colFilled) {
                total += 7;
            }
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                Tile tile = square[i][j];
                if (tile != null) {
                    colorCount[DEFAULT_COLORS.indexOf(tile.getColor())] += 1;
                }
            }
        }
        for (int count : colorCount) {
            if (count == NUM_ROWS) {
                total += 10;
            }
        }

        return new Score(total);
    }


    /**
     * The Mosaic substring begins with a 'M' Which is followed by *up to* 25 3-character strings.
     * Each 3-character string is defined as follows: 1st character is 'a' to 'e' - representing the
     * tile colour. 2nd character is '0' to '4' - representing the row. 3rd character is '0' to '4'
     * - representing the column. The Mosaic substring is ordered first by row, then by column. That
     * is, "a01" comes before "a10".
     *
     * @param token the mosaic string
     * @return true if is well formed mosaic string
     */
    public static boolean isWellFormedMosaicString(String token) {
        if (token == null || token.length() % 3 != 0 || token.length() > 25 * 3) {
            return false;
        }

        for (int i = 0; i < token.length(); i++) {
            char color = token.charAt(i);
            char row = token.charAt(++i);
            char column = token.charAt(++i);
            // only five different colors
            if (color != 'a' && color != 'b' && color != 'c' && color != 'd' && color != 'e') {
                return false;
            }
            // only five possible rows
            if (row != '0' && row != '1' && row != '2' && row != '3' && row != '4') {
                return false;
            }
            // only five possible columns
            if (column != '0' && column != '1' && column != '2' && column != '3' && column != '4') {
                return false;
            }
            // order first by row
            if (i > 3 && (row - token.charAt(i - 4) < 0)) {
                return false;
            }
            // order second by column
            if (i > 3 && row == token.charAt(i - 4) && column - token.charAt(i - 3) < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMosaicValid(String mosaic,String storage) {
        // Example "A0Ma00S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff",mosaic = a00, storage = 0a11c22a33c44b5
        boolean mosaicValid = true;
        for (int i = 0;i<mosaic.length();i=i+3){
            //Record the color, rows, column of the mosaic
            char mosaicColor = mosaic.charAt(i);
            int mosaicRow = Integer.parseInt(mosaic.substring(i+1,i+2));
            int mosaicColumn = Integer.parseInt(mosaic.substring(i+2,i+3));
            for (int k = 3 ; k<mosaic.length(); k = k+3){
                char mosaicColor2 = mosaic.charAt(k);
                int mosaicRow2 = Integer.parseInt(mosaic.substring(k+1,k+2));
                int mosaicColumn2 = Integer.parseInt(mosaic.substring(k+2,k+3));
                if(mosaicColor == mosaicColor2 && (mosaicRow == mosaicRow2 || mosaicColumn == mosaicColumn2)){
                    mosaicValid = false;
                }
            }
            //Record the color and rows of the storage, mosaic compares with storage color and rows.
            for (int j = 0 ;j<storage.length();j=j+3){
                int storageRow = Integer.parseInt(storage.substring(j,j+1));
                char storageColor = storage.charAt(j+1);
                if (mosaicColor == storageColor && mosaicRow == storageRow){
                    mosaicValid = false;
                }
            }
        }
        return mosaicValid;
    }

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of mosaic state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedMosaicString(token)) {
            return;
        }

        square = new Tile[NUM_ROWS][NUM_ROWS];
        colorCount = new int[]{0, 0, 0, 0, 0};

        for (int i = 0; i < token.length(); i += 3) {
            Tile tile = Tile.from(token.charAt(i));
            int row = token.charAt(i + 1) - 48;
            int col = token.charAt(i + 2) - 48;
            square[row][col] = tile;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_ROWS; j++) {
                Tile tile = square[i][j];
                if (tile != null) {
                    stringBuilder.append(tile.getColorCode());
                    stringBuilder.append(i);
                    stringBuilder.append(j);
                }
            }
        }
        return stringBuilder.toString();
    }
}
