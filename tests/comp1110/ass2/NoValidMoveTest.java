package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * No valid move available return null.
 * Author: Xinjie Wang
 */
public class NoValidMoveTest {
    @Test
    public void noValidMoveTest() {
        Game game = new Game();
        String[] gameState = new String[2];
        gameState[0] = "AFCB1913171417D0000000000";
        gameState[1] = "A0MS1e13b2FB0MS1b22d1F";
        assertNull(game.generateAction(gameState));
    }

}
