package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;

public class Storage {

    private final int NUM_ROWS = 5;
    private final int[] MAX_LENGTH = new int[]{1, 2, 3, 4, 5};

    private ArrayList<ArrayList<Tile>> triangle;

    public Storage() {
        triangle = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            triangle.add(new ArrayList<>());
        }
    }

    /**
     * Rules to place the tiles.
     * 1. If a row already contains tiles, you may only add tiles of the same colour to it.
     * 2. If you have more tiles than can fit in your chosen row, then you must place the excess
     * tiles on the floor.
     * 3. You are not allowed to place tiles of a certain colour in a row if the corresponding
     * mosaic row already contains a tile of that colour.
     * 4. If you cannot or do not want to place tiles on a row, you may place them directly onto
     * the floor.
     * @param tiles
     * @param color
     * @param tilesNum
     * @param rowNum
     * @param floor
     */
    public void addTiles(ArrayList<Tile> tiles, String color, int tilesNum, int rowNum,
            Floor floor) {
        // FIXME
    }

    /**
     * Tile and Score
     * Tile rules
     * 1. Go through your storage rows from row 0 to row 4 and move the rightmost tile of each
     * complete row to the space of the same colour in the corresponding row of the mosaic.
     * 2. Empty any row that no longer has a tile in the rightmost space and place all remaining
     * tiles in the discard pile. Any tiles that remain in incomplete rows on your board remain for
     * the next round.
     * @return the score
     */
    public int tileAndScore(Mosaic mosaic) {
        // FIXME
        return 0;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
