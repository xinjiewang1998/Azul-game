package comp1110.ass2.board;

import comp1110.ass2.Tile;
<<<<<<< HEAD
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
=======
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
>>>>>>> a91385b2ffdf018acb34b1448499b292d5f24f2f
    }
}
