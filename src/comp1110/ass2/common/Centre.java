package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;

public class Centre {

    ArrayList<Tile> tile;

    public Centre() {
        tile = new ArrayList<>(tile);
    }

    public ArrayList<Tile> getTiles() {
        return this.tile;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tile = tiles;
    }


    public void addTileToCentre(Tile tile) {
        this.tile.add(tile);
    }
}
