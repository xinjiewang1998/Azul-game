package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayList;

public class Centre {
    private final int PLAYER_LIMIT = 1;
    private ArrayList<Tile> centre = new ArrayList<>();

    //constructor
    public Centre() {
        centre.add(Tile.FirstPlayer);
    }

    public ArrayList<Tile> getCentre() {
        return centre;
    }

    public void setCentre(ArrayList<Tile> centre) {
        this.centre = centre;
    }


    /**
     * move the remaining tiles on this factory to the centre.
     *
     * @param otherTiles a array contains remaining tiles on this factory
     */
    public void addTilesToCentre(ArrayList<Tile> otherTiles) {
        //FIXME
    }


    /**
     * Pick all tiles of the same colour from the centre.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the same color player chose.
     */
    public ArrayList<Tile> takeTilesFromCentre(String color) {
        //FIXME
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "";
    }
}
