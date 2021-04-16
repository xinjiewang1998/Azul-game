package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Deque;

public class Discard {

    ArrayList<Tile> tiles;

    public Discard() {
        tiles = new ArrayList<>();
    }

    public int refillFromDiscard(ArrayList<Tile> to) {
        int total = tiles.size();
        to.addAll(tiles);
        tiles.clear();
        return total;
    }

    public void putTiles(Deque<Tile> tiles) {
        tiles.addAll(tiles);
        tiles.clear();
    }

    /**
     * Empty any row that no longer has a tile in the rightmost space and place all remaining tiles in the discard pile.
     * Any tiles that remain in incomplete rows on your board remain for the next round.
     *
     * @param leftTiles tiles left in storage.
     */
    public void addTilesToDiscard(ArrayList<Tile> leftTiles) {
        //FIXME
    }

    @Override
    public String toString() {
        return "";
    }
}
