package comp1110.ass2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import comp1110.ass2.board.Floor;
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class ReconstructFromStringTest {

    ////////////////////////////////////     FLOOR     //////////////////////////////////
    @Test
    public void testReconstructFloorFromNullString() {
        Floor floor = new Floor();
        floor.reconstructFromString(null);
        assertEquals(0, floor.getTiles().size());
        assertNull(floor.getFirstPlayerTile());
    }

    @Test
    public void testReconstructFloorFromIncorrectString() {
        Floor floor = new Floor();
        floor.reconstructFromString("aaaaaaaaaa");
        assertEquals(0, floor.getTiles().size());
        assertNull(floor.getFirstPlayerTile());
    }

    @Test
    public void testReconstructFloorFromString() {
        Floor floor = new Floor();
        floor.reconstructFromString("abcdef");
        assertEquals(5, floor.getTiles().size());
        assertNotNull(floor.getFirstPlayerTile());
        assertEquals('f', floor.getFirstPlayerTile().getColorCode());
        for (int i = 0; i < 5; i++) {
            Tile tile = floor.getTiles().pop();
            assertEquals('a' + i, tile.getColorCode());
        }
    }

    ////////////////////////////////////     MOSAIC     //////////////////////////////////
    @Test
    public void testReconstructMosaicFromNullString() {
        Mosaic mosaic = new Mosaic();
        mosaic.reconstructFromString(null);
        assertEquals(5, mosaic.getSquare().length);
        for (Tile[] tiles : mosaic.getSquare()) {
            assertEquals(5, tiles.length);
            for (Tile tile : tiles) {
                assertNull(tile);
            }
        }
    }

    @Test
    public void testReconstructMosaicFromIncorrectString() {
        Mosaic mosaic = new Mosaic();
        mosaic.reconstructFromString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42c43d44a44");
        assertEquals(5, mosaic.getSquare().length);
        for (Tile[] tiles : mosaic.getSquare()) {
            assertEquals(5, tiles.length);
            for (Tile tile : tiles) {
                assertNull(tile);
            }
        }
    }

    @Test
    public void testReconstructMosaicFromString() {
        Mosaic mosaic = new Mosaic();
        mosaic.reconstructFromString(
                "a00b01c02d03e04a10b11c12d13e14a20b21c22d23e24a30b31c32d33e34a40b41c42d43e44");
        assertEquals(5, mosaic.getSquare().length);
        for (Tile[] tiles : mosaic.getSquare()) {
            assertEquals(5, tiles.length);
            for (int i = 0; i < 5; i++) {
                assertNotNull(tiles[i]);
                assertEquals('a' + i, tiles[i].getColorCode());
            }
        }
    }

    ////////////////////////////////////     STORAGE     //////////////////////////////////
    @Test
    public void testReconstructStorageFromNullString() {
        Storage storage = new Storage();
        storage.reconstructFromString(null);
        assertEquals(5, storage.getTriangle().size());
        for (ArrayDeque<Tile> row : storage.getTriangle()) {
            assertEquals(0, row.size());
        }
    }

    @Test
    public void testReconstructStorageFromIncorrectString() {
        Storage storage = new Storage();
        storage.reconstructFromString("f");
        assertEquals(5, storage.getTriangle().size());
        for (ArrayDeque<Tile> row : storage.getTriangle()) {
            assertEquals(0, row.size());
        }
    }

    @Test
    public void testReconstructStorageFromString() {
        Storage storage = new Storage();
        storage.reconstructFromString("0a11b22c33d44e5");
        assertEquals(5, storage.getTriangle().size());
        for (int i = 0; i < 5; i++) {
            ArrayDeque<Tile> row = storage.getTriangle().get(i);
            assertEquals(i + 1, row.size());
            assertEquals('a' + i, row.pop().getColorCode());
        }
    }

    ////////////////////////////////////     CENTRE     //////////////////////////////////
    @Test
    public void testReconstructCentreFromNullString() {
        Centre centre = new Centre();
        centre.reconstructFromString(null);
        assertEquals(0, centre.getTiles().size());
        assertNull(centre.getFirstPlayerTile());
    }

    @Test
    public void testReconstructCentreFromIncorrectString() {
        Centre centre = new Centre();
        centre.reconstructFromString("aaabbbcccdddggg");
        assertEquals(0, centre.getTiles().size());
        assertNull(centre.getFirstPlayerTile());
    }

    @Test
    public void testReconstructCentreFromString() {
        Centre centre = new Centre();
        centre.reconstructFromString("abcdef");
        assertEquals(5, centre.getTiles().size());
        assertNotNull(centre.getFirstPlayerTile());
        assertEquals('f', centre.getFirstPlayerTile().getColorCode());
        for (int i = 0; i < 5; i++) {
            Tile tile = centre.getTiles().get(i);
            assertEquals('a' + i, tile.getColorCode());
        }
    }

    ////////////////////////////////////     BAG     //////////////////////////////////
    @Test
    public void testReconstructBagFromNullString() {
        Bag bag = new Bag();
        bag.reconstructFromString(null);
        assertEquals(0, bag.getTiles().size());
    }

    @Test
    public void testReconstructBagFromIncorrectString() {
        Bag bag = new Bag();
        bag.reconstructFromString("000000000000000");
        assertEquals(0, bag.getTiles().size());
    }

    @Test
    public void testReconstructBagFromString() {
        Bag bag = new Bag();
        bag.reconstructFromString("0408121620");
        assertEquals(60, bag.getTiles().size());
        int end = 0;
        int[] count = new int[]{0, 0, 0, 0, 0};
        for (int i = 0; i < 60; i++) {
            count[bag.getTiles().get(i).getColorCode() - 'a'] += 1;
        }
        assertArrayEquals(new int[]{4, 8, 12, 16, 20}, count);
    }

    ////////////////////////////////////     DISCARD     //////////////////////////////////
    @Test
    public void testReconstructDiscardFromNullString() {
        Discard discard = new Discard();
        discard.reconstructFromString(null);
        assertEquals(0, discard.getTiles().size());
    }

    @Test
    public void testReconstructDiscardFromIncorrectString() {
        Discard discard = new Discard();
        discard.reconstructFromString("000000000000000");
        assertEquals(0, discard.getTiles().size());
    }

    @Test
    public void testReconstructDiscardFromString() {
        Discard discard = new Discard();
        discard.reconstructFromString("0408121620");
        assertEquals(60, discard.getTiles().size());
        int end = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4 * (i + 1); j++) {
                assertEquals('a' + i, discard.getTiles().get(end + j).getColorCode());
            }
            end += 4 * (i + 1);
        }
    }

    ////////////////////////////////////     FACTORY     //////////////////////////////////
    @Test
    public void testReconstructFactoryFromNullString() {
        Factory factory = new Factory();
        factory.reconstructFromString(null);
        assertEquals(0, factory.getTiles().size());
    }

    @Test
    public void testReconstructFactoryFromIncorrectString() {
        Factory factory = new Factory();
        factory.reconstructFromString("1abcde");
        assertEquals(0, factory.getTiles().size());
    }

    @Test
    public void testReconstructFactoryFromString() {
        Factory factory = new Factory();
        factory.reconstructFromString("1abcd");
        assertEquals(4, factory.getTiles().size());
        for (int i = 0; i < 4; i++) {
            assertEquals('a' + i, factory.getTiles().get(i).getColorCode());
        }
    }
}
