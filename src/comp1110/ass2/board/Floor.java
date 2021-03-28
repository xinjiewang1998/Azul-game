package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Floor {

    private ArrayList<Tile> tiles;

    public Floor() {
        this.tiles = new ArrayList<>();
    }

    /**
     * Add all other tiles to our tiles.
     * @param otherTiles
     */
    public void addTiles(ArrayDeque<Tile> otherTiles) {
        // FIXME
    }

    /**
     * Clear all tiles and return them.
     * @return
     */
    public ArrayList<Tile> clearTiles() {
        // FIXME
        return tiles;
    }

    /**
     * calculate the total penalty of the floor.
     * @return
     */
    public Score calculatePenalty() {
        // FIXME
        return new Score(0);
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
