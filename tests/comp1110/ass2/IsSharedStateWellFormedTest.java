package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static comp1110.ass2.ExampleGames.NOT_WELL_FORMED_SHARED_STATE;
import static comp1110.ass2.ExampleGames.VALID_STATES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsSharedStateWellFormedTest {
    private void test(String in, boolean expected) {
        boolean out = Azul.isSharedStateWellFormed(in);
        assertEquals(expected, out, "isSharedStateWellFormed for input state: \"" + in + "\"");
    }

    @Test
    public void testValidState() {
        for (int i = 0; i < VALID_STATES.length; i++) {
            test(VALID_STATES[i][0], true);
        }
    }

    @Test
    public void testIncorrectDelimiters() {
        test(VALID_STATES[0][0], true); // test for known well-formed state
        for (int i = 0; i < VALID_STATES.length; i++) {
            String replaceDelim = VALID_STATES[i][0].replace('F', '!');
            test(replaceDelim, false);
            String moveDelim = VALID_STATES[i][0].replace("0", "B");
            test(moveDelim, false);
        }
    }

    @Test
    public void testInvalidStates() {
        test(VALID_STATES[0][0], true); // test for known well-formed state
        for (int i = 0; i < NOT_WELL_FORMED_SHARED_STATE.length; i++) {
            String state = NOT_WELL_FORMED_SHARED_STATE[i];
            test(state, false);
        }
    }
}


