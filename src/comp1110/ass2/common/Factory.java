package comp1110.ass2.common;


import comp1110.ass2.Tile;
import java.util.ArrayList;

public class Factory {

    Bag bag;

    ArrayList<Tile> tiles;

    public Factory (Bag bag) {
        this.bag = bag;
        tiles = new ArrayList<>();
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    public void drawTiles(){
        if(tiles != null && tiles.size() != 0 ) {

        } else {
            tiles = bag.drawTiles();
        }
    }


}
