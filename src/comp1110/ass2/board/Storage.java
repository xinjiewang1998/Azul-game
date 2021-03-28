package comp1110.ass2.board;

import comp1110.ass2.Tile;


import comp1110.ass2.common.Discard;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Storage {

    final int STORAGE_LENGTH = 5;
    final int[] MAX_LENGTH = new int[]{1, 2, 3, 4, 5};

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
        for(int i = 0; i < STORAGE_LENGTH; i++) {
            Deque<Tile> row = ladder.get(i);
            if(MAX_LENGTH[i] == row.size()) {

                // tiling
                int score = mosaic.putTile(row.pop(), i);
                discard.putTiles(row);

                // scoring
                total += score;
            }
        }
        return total;
    }
}
