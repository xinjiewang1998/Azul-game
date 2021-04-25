package comp1110.ass2;

import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrawTileTest {

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
    public void testRefillFactoryNoEnoughTiles() {
        Bag bag = new Bag();
        Discard discard = new Discard();
        ArrayList<Tile> D = new ArrayList<>();
        ArrayList<Tile> B = new ArrayList<>();
        discard.setTiles(D);
        for (int i = 0; i < 10; i++) {
            B.add(Tile.from('a'));
        }
        bag.setTiles(B);
        Factory[] factories = new Factory[5];
        for (int i = 0; i < 5; i++) {
            factories[i] = new Factory(i);
        }
        for (Factory factory : factories) {
            factory.refillTiles(bag, discard);
        }
        int total = 0;
        for (Factory factory : factories) {
            total += factory.getTiles().size();
        }
        assertEquals(10, total);
    }


}
