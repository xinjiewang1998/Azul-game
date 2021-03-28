package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayList;

public class Discard {
    private ArrayList<Tile> discard = new ArrayList<>();

    public ArrayList<Tile> getDiscard() {
        return discard;
    }

    public void setDiscard(ArrayList<Tile> discard) {
        this.discard = discard;
    }

    //constructor
    public Discard() {

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
