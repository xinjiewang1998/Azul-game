package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static comp1110.ass2.ExampleGames.FULL_GAME_WITH_MOVES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class ApplyMoveTest {

    private String errorPrefix(String[] inputState, String move) {
        return "Azul.applyMove({\"" + inputState[0] + "\", \"" + inputState[1] + "\", \"" + move + "\"}) returned invalid result ";
    }

    @Test
    public void testFloorFull() {
        String[] gameState = {"AF4bbccCbbbcdeeeeeeefB1615171913D0000000000", "A0MS2a2FB0MS2a2F"};
        String move = "ACeF";
        String[] next = {"BF4bbccCbbbcdB1615171913D0000000001", "A0MS2a2FeeeeeefB0MS2a2F"};
        String[] out = Azul.applyMove(gameState, move);
        String errorMessagePrefix = errorPrefix(gameState, move);
        assertEquals(Arrays.toString(next), Arrays.toString(out), errorMessagePrefix + "expected 'e' tile to overflow to discard");
    }

    @Test
    public void testStorageFull() {
        String[] gameState = {"AFCddddeeeB0504060401D0409050713", "A0Mb00e04d10b11c21a32S1a22a23b24a5FfB3Mc02e03d04b14e24b31d33S0b11d22c33c14a1Fccc"};
        String move = "ACd0";
        String[] next = {"BFCeeeB0504060401D0409050713", "A0Mb00e04d10b11c21a32S0d11a22a23b24a5FdddfB3Mc02e03d04b14e24b31d33S0b11d22c33c14a1Fccc"};
        String[] out = Azul.applyMove(gameState, move);
        String errorMessagePrefix = errorPrefix(gameState, move);
        assertEquals(Arrays.toString(next), Arrays.toString(out), errorMessagePrefix + "expected move to overflow onto floor");
    }

    @Test
    public void testTilingMove() {
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] previous = FULL_GAME_WITH_MOVES[i];
            if (previous.length == 2) {
                continue;
            }
            String move = previous[2];
            if (previous[2].length() == 3) {
                String[] next = FULL_GAME_WITH_MOVES[i + 1];
                String[] prevState = {previous[0], previous[1]};
                String[] nextState = {next[0], next[1]};
                String[] out = Azul.applyMove(prevState.clone(), move);
                String errorMessagePrefix = errorPrefix(prevState, move);
                assertEquals(Arrays.toString(nextState), Arrays.toString(out), errorMessagePrefix);
            }
        }
    }

    @Test
    public void testDraftingMove() {
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] previous = FULL_GAME_WITH_MOVES[i];
            if (previous.length == 2) {
                continue;
            }
            String move = previous[2];
            if (previous[2].length() == 4) {
                String[] next = FULL_GAME_WITH_MOVES[i + 1];
                String[] prevState = {previous[0], previous[1]};
                String[] nextState = {next[0], next[1]};
                String[] out = Azul.applyMove(prevState.clone(), move);
                String errorMessagePrefix = errorPrefix(prevState, move);
                assertEquals(Arrays.toString(nextState), Arrays.toString(out), errorMessagePrefix);
            }
        }
    }

}
