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

    Mosaic mosaic;
    Floor floor;

    Discard discard;

    public Storage(Mosaic mosaic, Floor floor, Discard discard) {
        triangle = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++)  {
            triangle.add(new ArrayDeque<>());
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
        Deque<Tile> row = triangle.get(rowNum);
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
        for (int i = 0; i < NUM_ROWS; i++) {
            Deque<Tile> row = triangle.get(i);
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


    public boolean hasCompleteRow() {
        for (int i  = 0; i< triangle.size(); i++) {
            if (triangle.get(i).size() == i + 1) {
                return true;
            }
        }
        return false;
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

    public void fillFrom(String storageToken) {
        for(int i = 0; i < storageToken.length(); i+=3) {
            int row = storageToken.charAt(i) - 48;
            int num = storageToken.charAt(i+2) - 48;
            for(int j = 0; j < num; j++) {
                triangle.get(row).add(Tile.from(storageToken.charAt(i+1)));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < NUM_ROWS; i++) {
            if(triangle.get(i).size() != 0) {
                stringBuilder.append(i);
                stringBuilder.append(triangle.get(i).getFirst().getColorCode());
                stringBuilder.append(triangle.get(i).size());
            }
        }
        return stringBuilder.toString();
    }
}
