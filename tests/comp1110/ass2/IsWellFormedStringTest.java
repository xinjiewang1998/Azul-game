package comp1110.ass2;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import comp1110.ass2.board.Floor;
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import org.junit.jupiter.api.Test;

public class IsWellFormedStringTest {

    ////////////////////////////////////     FLOOR     //////////////////////////////////
    @Test
    public void testCorrectFloorString() {
        assertTrue(Floor.isWellFormedFloorString("aaaaaaa"));
    }

    @Test
    public void testCorrectFloorString1() {
        assertTrue(Floor.isWellFormedFloorString("aabcdef"));
    }

    @Test
    public void testCorrectFloorString2() {
        assertTrue(Floor.isWellFormedFloorString("abcde"));
    }

    @Test
    public void testCorrectFloorString3() {
        assertTrue(Floor.isWellFormedFloorString("abcdef"));
    }

    @Test
    public void testEmptyFloorString() {
        assertTrue(Floor.isWellFormedFloorString(""));
    }

    @Test
    public void testTooManyFloorString() {
        assertFalse(Floor.isWellFormedFloorString("aaaaaaaa"));
    }

    @Test
    public void testMisSpelledFloorString() {
        assertFalse(Floor.isWellFormedFloorString("abcdefg"));
    }

    @Test
    public void testTooManyFFloorString() {
        assertFalse(Floor.isWellFormedFloorString("abcdeff"));
    }

    @Test
    public void testNotAlphabeticalOrderFloorString() {
        assertFalse(Floor.isWellFormedFloorString("fabcde"));
    }

    @Test
    public void testNullFloorString() {
        assertFalse(Floor.isWellFormedFloorString(null));
    }

    ////////////////////////////////////     MOSAIC     //////////////////////////////////
    @Test
    public void testCorrectMosaicString() {
        assertTrue(Mosaic.isWellFormedMosaicString("a00"));
    }

    @Test
    public void testCorrectMosaicString1() {
        assertTrue(Mosaic.isWellFormedMosaicString("b00c11d22"));
    }

    @Test
    public void testCorrectMosaicString2() {
        assertTrue(Mosaic.isWellFormedMosaicString("a00b11c22d33e44"));
    }

    @Test
    public void testCorrectMosaicString3() {
        assertTrue(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42c43d44"));
    }

    @Test
    public void testEmptyMosaicString() {
        assertTrue(Mosaic.isWellFormedMosaicString(""));
    }

    @Test
    public void testTooManyMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42c43d44a44"));
    }

    @Test
    public void testMisSpelledColorMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42444"));
    }

    @Test
    public void testMisSpelledRowMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42ca4"));
    }

    @Test
    public void testMisSpelledColumnMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42c4a"));
    }

    @Test
    public void testHasFMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e40a41b42f44"));
    }

    @Test
    public void testNotOrderByRowMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e41a32b42e44"));
    }

    @Test
    public void testNotOrderByColumnMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e41a40b42e44"));
    }

    @Test
    public void testNullMosaicString() {
        assertFalse(Mosaic.isWellFormedMosaicString(null));
    }

    ////////////////////////////////////     STORAGE     //////////////////////////////////
    @Test
    public void testCorrectStorageString() {
        assertTrue(Storage.isWellFormedStorageString("0a0"));
    }

    @Test
    public void testCorrectStorageString1() {
        assertTrue(Storage.isWellFormedStorageString("0b01c12d2"));
    }

    @Test
    public void testCorrectStorageString2() {
        assertTrue(Storage.isWellFormedStorageString("0a01b12c23d34e4"));
    }

    @Test
    public void testEmptyStorageString() {
        assertTrue(Storage.isWellFormedStorageString(""));
    }

    @Test
    public void testTooManyStorageString() {
        assertFalse(Storage.isWellFormedStorageString(
                "0a01b12c23d34e40a01b12c23d34e4"));
    }

    @Test
    public void testMisSpelledColorStorageString() {
        assertFalse(Storage.isWellFormedStorageString(
                "0a01b12c23d34f4"));
    }

    @Test
    public void testMisSpelledRowStorageString() {
        assertFalse(Storage.isWellFormedStorageString(
                "0a01b12c23d35e4"));
    }

    @Test
    public void testMisSpelledColumnStorageString() {
        assertFalse(Storage.isWellFormedStorageString(
                "0a01b12c23d34e6"));
    }

    @Test
    public void testNotOrderByRowStorageString() {
        assertFalse(Storage.isWellFormedStorageString(
                "0a01b12c24d33e4"));
    }

    @Test
    public void testNullStorageString() {
        assertFalse(Storage.isWellFormedStorageString(null));
    }

    ////////////////////////////////////     CENTRE     //////////////////////////////////
    @Test
    public void testCorrectCentreString() {
        assertTrue(Centre.isWellFormedCentreString("a"));
    }

    @Test
    public void testCorrectCentreString1() {
        assertTrue(Centre.isWellFormedCentreString("abcdef"));
    }

    @Test
    public void testCorrectCentreString2() {
        assertTrue(Centre.isWellFormedCentreString("aaabbbcccdddeee"));
    }

    @Test
    public void testEmptyCentreString() {
        assertTrue(Centre.isWellFormedCentreString(""));
    }

    @Test
    public void testTooManyCentreString() {
        assertFalse(Centre.isWellFormedCentreString(
                "aaabbbcccdddeeee"));
    }

    @Test
    public void testMisSpelledCentreString() {
        assertFalse(Centre.isWellFormedCentreString(
                "aaabbbcccdddggg"));
    }

    @Test
    public void testMultiFCentreString() {
        assertFalse(Centre.isWellFormedCentreString(
                "aaabbbcccdddff"));
    }

    @Test
    public void testNotOrderByAlphabeticCentreString() {
        assertFalse(Centre.isWellFormedCentreString(
                "eeedddcccbbbaaa"));
    }

    @Test
    public void testNullCentreString() {
        assertFalse(Centre.isWellFormedCentreString(null));
    }

    ////////////////////////////////////     BAG     //////////////////////////////////
    @Test
    public void testCorrectBagString() {
        assertTrue(Bag.isWellFormedBagString("1011121314"));
    }

    @Test
    public void testCorrectBagString1() {
        assertTrue(Bag.isWellFormedBagString("0000000000"));
    }

    @Test
    public void testCorrectBagString2() {
        assertTrue(Bag.isWellFormedBagString("2020202020"));
    }

    @Test
    public void testEmptyBagString() {
        assertFalse(Bag.isWellFormedBagString(""));
    }

    @Test
    public void testTooManyBagString() {
        assertFalse(Bag.isWellFormedBagString(
                "00000000000"));
    }

    @Test
    public void testMisSpelledBagString() {
        assertFalse(Bag.isWellFormedBagString(
                "0000000030"));
    }

    @Test
    public void testNullBagString() {
        assertFalse(Bag.isWellFormedBagString(null));
    }

    ////////////////////////////////////     DISCARD     //////////////////////////////////
    @Test
    public void testCorrectDiscardString() {
        assertTrue(Discard.isWellFormedDiscardString("1011121314"));
    }

    @Test
    public void testCorrectDiscardString1() {
        assertTrue(Discard.isWellFormedDiscardString("0000000000"));
    }

    @Test
    public void testCorrectDiscardString2() {
        assertTrue(Discard.isWellFormedDiscardString("2020202020"));
    }

    @Test
    public void testEmptyDiscardString() {
        assertFalse(Discard.isWellFormedDiscardString(""));
    }

    @Test
    public void testTooManyDiscardString() {
        assertFalse(Discard.isWellFormedDiscardString(
                "00000000000"));
    }

    @Test
    public void testMisSpelledDiscardString() {
        assertFalse(Discard.isWellFormedDiscardString(
                "0000000030"));
    }

    @Test
    public void testNullDiscardString() {
        assertFalse(Discard.isWellFormedDiscardString(null));
    }

    ////////////////////////////////////     FACTORY     //////////////////////////////////
    @Test
    public void testCorrectFactoryString() {
        assertTrue(Factory.isWellFormedFactoryString("0aaaa"));
    }

    @Test
    public void testCorrectFactoryString1() {
        assertTrue(Factory.isWellFormedFactoryString("0abcd"));
    }

    @Test
    public void testCorrectFactoryString2() {
        assertTrue(Factory.isWellFormedFactoryString("1abcd"));
    }

    @Test
    public void testEmptyFactoryString() {
        assertTrue(Factory.isWellFormedFactoryString(""));
    }

    @Test
    public void testTooManyFactoryString() {
        assertFalse(Factory.isWellFormedFactoryString(
                "1abcde"));
    }

    @Test
    public void testMisSpelledColorFactoryString() {
        assertFalse(Factory.isWellFormedFactoryString(
                "0aaaf"));
    }

    @Test
    public void testMisSpelledFactoryNumFactoryString() {
        assertFalse(Factory.isWellFormedFactoryString(
                "5aaaf"));
    }

    @Test
    public void testNotOrderByColorFactoryString() {
        assertFalse(Factory.isWellFormedFactoryString(
                "a00b01c02d03e04b10c11d12e13a14c20d21e22a23b24d30e31a32b33c34e41a32b42e44"));
    }

    @Test
    public void testNullFactoryString() {
        assertFalse(Factory.isWellFormedFactoryString(null));
    }
}
