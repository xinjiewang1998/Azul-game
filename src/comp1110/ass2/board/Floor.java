package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Deque;

public class Floor {

    ArrayList<Tile> tiles;

    public Floor() {
        tiles = new ArrayList<>();
    }

    public void putTiles(Deque tiles) {
        this.tiles.addAll(tiles);
        tiles.clear();
    }

    public int score() {
        return switch(tiles.size()) {
            case 0 -> 0;
            case 1 -> -1;
            case 2 -> -2;
            case 3 -> -4;
            case 4 -> -6;
            case 5 -> -8;
            case 6 -> -11;
            default -> -14;
        };
    }
}
