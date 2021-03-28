package comp1110.ass2.board;

import comp1110.ass2.Tile;
import comp1110.ass2.common.Centre;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Floor {

    private ArrayList<Tile> tiles;

    public Floor() {
        this.tiles = new ArrayList<>();
    }

    /**
     * Add all other tiles to our tiles.
     * @param otherTiles the other tiles
     */
    public void addTiles(ArrayDeque<Tile> otherTiles) {
        // FIXME
    }

    /**
     * Clear all tiles and return them.
     * @return the tiles
     */
    public ArrayList<Tile> clearTiles() {
        // FIXME
        return tiles;
    }

    /**
     * calculate the total penalty of the floor.
     * @return the penalty score
     */
    public Score calculatePenalty() {
        // FIXME
        return new Score(0);
    }

    /**
     * check if floor contains first player tile
     * @return true if has first player tile
     */
    public boolean hasFirstPlayerTile() {
        return false;
    }

    /**
     * return first player tile to centre
     * @param centre the shared centre
     */
    public void returnFirstPlayerTile(Centre centre) {

    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
