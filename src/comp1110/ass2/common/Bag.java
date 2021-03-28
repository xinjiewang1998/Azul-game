package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {

    private final int COLOR_LIMIT = 20;
    private ArrayList<Tile> bag = new ArrayList<>();

    public ArrayList<Tile> getBag() {
        return bag;
    }

    public void setBag(ArrayList<Tile> bag) {
        this.bag = bag;
    }

    //constructor
    public Bag() {
        for (int i = 0; i < COLOR_LIMIT; i++) {
            bag.add(Tile.Blue);
            bag.add(Tile.Green);
            bag.add(Tile.Orange);
            bag.add(Tile.Purple);
            bag.add(Tile.Red);
        }
        Collections.shuffle(bag);
    }

    /**
     * Each factory is filled with exactly four tiles drawn randomly from the bag
     * <p>
     * take four tiles from bag randomly.
     *
     * @return a Array contains four tiles.
     */
    public ArrayList<Tile> takeTiles() {
        return new ArrayList<>();
    }

    /**
     * If at any point you run out of tiles in the bag,
     * you should move all the tiles from the discard pile into the bag
     * and then continue filling the factories.
     * <p>
     * refill from discard.
     *
     * @param discard a array contains tiles in discard
     */
    public void refill(Discard discard) {

    }

    @Override
    public String toString() {
        return "";
    }
}
