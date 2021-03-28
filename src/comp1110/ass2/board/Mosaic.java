package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Arrays;

public class Mosaic {

    private final int NUM_ROWS = 5;
    private final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(
            Arrays.asList("blue", "green", "orange", "purple", "red"));

    // simple [][] is enough for use.
    private Tile[][] square;

    public Mosaic () {
        this.square = new Tile[NUM_ROWS][NUM_ROWS];
    }

    /**
     * calculate the column number according to the color and row number.
     * @param color the target color
     * @param row the target row number
     * @return the column number
     */
    public int calculateColumn(String color, int row) {
        // FIXME
        return 0;
    }

    /**
     * tells whether a colored tile has occupied that row.
     * if it is true, cannot put tile with same color on that row again.
     * @param color the target color
     * @param row the target row
     * @return true if the color exists in mosaic
     */
    public boolean hasColor(String color, int row) {
        // FIXME
        return true;
    }

    /**
     * find the correct position and place the tile
     * @param tile the tile to be placed
     * @param row the target row
     */
    public void addTile(Tile tile, int row) {
        // FIXME
    }

    /**
     * check if there is a complete row in our mosaic
     * if it has, then end the game.
     * @return true if exists a complete row
     */
    public boolean hasCompleteRow() {
        // FIXME
        return true;
    }

    /**
     * Each player gains additional bonus points if they satisfy the following conditions:
     *
     * Gain 2 points for each complete row of your mosaic (5 consecutive horizontal tiles).
     * Gain 7 points for each complete column of your mosaic (5 consecutive vertical tiles).
     * Gain 10 points for each colour of tile for which you have placed all 5 tiles on your mosaic.
     * @return the Bonus Score
     */
    public int calculateBonusScore() {
        // FIXME
        return 0;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }

}
