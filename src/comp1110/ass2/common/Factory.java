package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayList;

public class Factory {
    private ArrayList<Tile> factory = new ArrayList<Tile>();

    public Factory() {

    }

    public ArrayList<Tile> getFactory() {
        return factory;
    }

    public void setFactory(ArrayList<Tile> factory) {
        this.factory = factory;
    }

    /**
     * Each factory is filled with exactly four tiles drawn randomly from the bag.
     *
     * @param fourRandomTiles a array contains four tiles.
     */
    public void addTilesFromBag(ArrayList<Tile> fourRandomTiles) {
        //FIXME
    }

    /**
     * Pick all tiles of the same colour from one factory.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the color in this factory.
     */
    public ArrayList<Tile> takeTilesFromFactory(String color) {
        //FIXME
        return new ArrayList<Tile>();
    }

    @Override
    public String toString() {
        //FIXME
        return "";
    }
}
