package comp1110.ass2.board;

import comp1110.ass2.Tile;
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
    public void addTiles(ArrayList<Tile> otherTiles) {
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
    public int calculatePenalty() {
        // FIXME
        return 0;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
