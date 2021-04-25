package comp1110.ass2;

import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Discard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DrawTilesTest {

    @Test
    public void testDraw() {
        Bag bag = new Bag();
        Discard discard = new Discard();
        bag.init();
        ArrayList<Tile> D = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++)
                D.add(Tile.from((char) ('a' + i)));
        }
        discard.setTiles(D);
        int total = discard.getTiles().size() + bag.getTiles().size();
        int total1 = total;
        for (int i = 0; i < total; i++) {
            Tile result = bag.drawTile(discard);
            total1--;
            assertEquals(total1, discard.getTiles().size() + bag.getTiles().size());
        }
    }

    @Test
    public void testDrawTileWithNullDiscard() {
        Bag bag = new Bag();
        Discard discard = new Discard();
        Tile tile = bag.drawTile(discard);
        assertNull(tile);
    }

    @Test
    public void testDrawTileCode() {
        Bag bag = new Bag();
        Discard discard = new Discard();
        bag.setTiles(new ArrayList<Tile>(Collections.singleton(Tile.Blue)));
        Tile tile = bag.drawTile(discard);
        assertEquals(Tile.Blue, tile);
    }


}
