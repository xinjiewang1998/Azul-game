package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


/**
 * Test tiling when Factory or center is not empty.
 * Author: Xinjie Wang
 */

public class TilingTest {
    @Test
    public void testTilingWhenFacAndCenAreNotEmpty() {
        Game game = new Game();
        String[] gameState = new String[2];
        gameState[0] = "AF0bcddCabbccdddefB1913171417D0000000000";
        gameState[1] = "A0MS1e23b2FB0MS1b22d1F";
        String move = "A11";
        Boolean a = game.isMoveValid(gameState, move);
        assertFalse(a);

    }


}
