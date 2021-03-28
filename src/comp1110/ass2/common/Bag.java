package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Bag {

    ArrayList<Tile> tiles;
    int start;
    int end;

    Discard discard;

    public Bag(Discard discard) {
        this.discard = discard;

        tiles = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < 20; i++) {
            tiles.add(Tile.Blue);
            tiles.add(Tile.Green);
            tiles.add(Tile.Orange);
            tiles.add(Tile.Purple);
            tiles.add(Tile.Red);
        }
        Collections.shuffle(tiles);

        start = 0;
        end = 100;

    }

    public ArrayList drawTiles() {

//        start = 96end = 100
//        if (start + 4) % 100 <
        if(start + 4 > end) {
            int amount = discard.refillFromDiscard(this.tiles);
            end += amount;
            if(start + 4 > end) {
                // still does not fill enough tiles
                // do something
            }
        }

        // assume there is enough tiles
        // TODO cannot do this assume, fix it above
        Tile first = tiles.get(start);
        Tile second = tiles.get(start + 1);
        Tile third = tiles.get(start + 2);
        Tile forth = tiles.get(start + 3);

        start = (start + 4) % 100;
        return new ArrayList<>(Arrays.asList(
                first, second, third, forth
        ));
    }

}
