package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static comp1110.ass2.ExampleGames.FULL_GAME_WITH_MOVES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class ApplyMoveTest {

    private void test(String[] gameState, String move, String[] expected) {
        String[] out = Azul.applyMove(gameState, move);
        assertEquals(Arrays.toString(expected), Arrays.toString(out), "applyMove for input state: " + Arrays.toString(gameState) + ", and move: '" + move + "'");
    }

    @Test
    public void testValid() {
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] previous = FULL_GAME_WITH_MOVES[i];
            String[] next = FULL_GAME_WITH_MOVES[i + 1];
            if (previous.length == 2) {
                continue;
            }
            String[] prevState = {previous[0], previous[1]};
            String[] nextState = {next[0], next[1]};
            String move = previous[2];
            test(prevState, move, nextState);
        }
    }

    @Test
    public void testFloorFull() {
        String[] test = new String[]{"AF4bbccCbbbcdeeeeeeefB1615171913D0000000000", "A0MS2a2FB0MS2a2F", "ACeF"};
        String[] next = new String[]{"BF4bbccCbbbcdB1615171913D0000000001", "A0MS2a2FeeeeeefB0MS2a2F"};
        test(test, test[2], next);

    }
}
