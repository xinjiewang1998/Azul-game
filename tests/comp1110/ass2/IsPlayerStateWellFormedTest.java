package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static comp1110.ass2.ExampleGames.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsPlayerStateWellFormedTest {
    private void test(String in, boolean expected) {
        boolean out = Azul.isPlayerStateWellFormed(in);
        assertEquals(expected, out, "isPlayerStateWellFormed for input state: \"" + in + "\"");
    }

    @Test
    public void testWellFormed() {
        for (int i = 0; i < WELL_FORMED_PLAYER_STATE.length; i++) {
            test(WELL_FORMED_PLAYER_STATE[i], true);
        }
        for (int i = 0; i < VALID_STATES.length; i++) {
            test(VALID_STATES[i][1], true);
        }
    }

    @Test
    public void testInvalidStates() {
        test(EMPTY_PLAYER_STATE, true); // trivially well-formed
        for (int i = 0; i < NOT_WELL_FORMED_PLAYER_STATE.length; i++) {
            String state = NOT_WELL_FORMED_PLAYER_STATE[i];
            test(state, false);
        }
    }
}
