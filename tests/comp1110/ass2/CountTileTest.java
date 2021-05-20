package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CountTileTest {
//    Author: Xiang Lu
    @Test
    public void countTile() {
        Game game = new Game();
        String[] gameState = {"AF0cdde1bbbe2abde3cdee4bcceCfB1915161614D0000000000", "A0MSFB0MSF"};
        game.reconstructCommonFrom(gameState[0]);
        int a = 80;
        boolean tileEqual = false;
        if (a == (int)(game.getCommon().getBag().countTile('P'))) {
            tileEqual = true;
        }
        assertFalse(tileEqual);
    }
}