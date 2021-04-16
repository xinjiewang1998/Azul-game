package comp1110.ass2.board;

import comp1110.ass2.Tile;

import comp1110.ass2.common.Discard;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Storage {


    private final int NUM_ROWS = 5;
    private final int[] MAX_LENGTH = new int[]{1, 2, 3, 4, 5};


    final int STORAGE_LENGTH = 5;

    List<Deque<Tile>> ladder;

    Mosaic mosaic;
    Floor floor;

    Discard discard;

    public Storage(Mosaic mosaic, Floor floor, Discard discard) {
        ladder = new ArrayList<>();
        for (int i = 0; i < STORAGE_LENGTH; i++)  {
            ladder.add(new ArrayDeque<>());
        }

        this.mosaic = mosaic;
        this.floor = floor;
        this.discard = discard;
    }

    // put tiles on the ladder
    // tiles
    // color
    // row
    // num : the num of tiles user would like to put on the ladder,
    //       if num == 0; all tiles will throw to floor.
    public boolean putTiles(Deque<Tile> tiles, String color, int num, int rowNum) {
        Deque<Tile> row = ladder.get(rowNum);
        Tile firstTile = row.getFirst();
        if (firstTile != null) {
            if (!firstTile.getColor().equals(color)) {
                return false;
            }
        } else {
            if (this.mosaic.hasColor(color, rowNum)) {
                return false;
            }
        }

        int totalPossible = num + row.size();
        int maxPossible = Math.min(totalPossible, MAX_LENGTH[rowNum]);
        for (int j = row.size(); j < maxPossible; j++) {
            row.push(tiles.pop());
        }
        if (tiles.size() != 0) {
            this.floor.putTiles(tiles);
        }
        return true;
    }

    // return score
    public int tileAndScore() {
        int total = 0;
        // tiling
        for (int i = 0; i < STORAGE_LENGTH; i++) {
            Deque<Tile> row = ladder.get(i);
            if (MAX_LENGTH[i] == row.size()) {

                // tiling
                int score = mosaic.putTile(row.pop(), i);
                discard.putTiles(row);

                // scoring
                total += score;
            }
        }
        return total;
    }


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

    /**
     * Rules to place the tiles.
     * 1. If a row already contains tiles, you may only add tiles of the same colour to it.
     * 2. If you have more tiles than can fit in your chosen row, then you must place the excess
     * tiles on the floor.
     * 3. You are not allowed to place tiles of a certain colour in a row if the corresponding
     * mosaic row already contains a tile of that colour.
     * 4. If you cannot or do not want to place tiles on a row, you may place them directly onto
     * the floor.
     * @param tiles the tiles to be placed
     * @param color the tiles' color
     * @param tilesNum the number of tiles to be placed on mosaic (not floor!)
     * @param rowNum the row number to locate tiles
     * @param floor the floor to have the remaining tiles.
     */
    public void addTiles(ArrayDeque<Tile> tiles, String color, int tilesNum, int rowNum,
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
    public Score tileAndScore(Mosaic mosaic) {
        // FIXME
        return new Score(0);
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
