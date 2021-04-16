package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static comp1110.ass2.ExampleGames.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsStateValidTest {

    private String errorPrefix(String[] inputState) {
        return "Azul.isStateValid({\"" + inputState[0] + "\", \"" + inputState[1] + "\"})"
                + System.lineSeparator();
    }

    @Test
    public void testValid() {
        for (String[] validState : VALID_STATES) {
            boolean out = Azul.isStateValid(validState);
            String errorMessagePrefix = errorPrefix(validState);
            assertTrue(out, errorMessagePrefix);
        }
        String[] invalid = new String[]{NOT_WELL_FORMED_SHARED_STATE[0], NOT_WELL_FORMED_PLAYER_STATE[1]};
        boolean out = Azul.isStateValid(invalid);
        assertFalse(out, errorPrefix(invalid) + " state is not well formed.");
    }

    @Test
    public void testNotWellFormed() {
        boolean out = Azul.isStateValid(VALID_STATES[0]);
        String errorMessagePrefix = errorPrefix(VALID_STATES[0]);
        assertTrue(out, errorMessagePrefix);
        for (int i = 0; i < NOT_WELL_FORMED_SHARED_STATE.length; i++) {
            String[] state = {VALID_STATES[i][1], NOT_WELL_FORMED_SHARED_STATE[i]};
            errorMessagePrefix = errorPrefix(state);
            out = Azul.isStateValid(state);
            assertFalse(out, errorMessagePrefix + " state is not well formed.");
        }
        for (int i = 0; i < NOT_WELL_FORMED_PLAYER_STATE.length; i++) {
            String[] state = {VALID_STATES[i][0], NOT_WELL_FORMED_PLAYER_STATE[i]};
            errorMessagePrefix = errorPrefix(state);
            out = Azul.isStateValid(state);
            assertFalse(out, errorMessagePrefix + " state is not well formed.");
        }
    }

    @Test
    public void testCorrectTiles() {
        String[] state = {"AFCB0610110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"};
        boolean out = Azul.isStateValid(state);
        String errorMessagePrefix = errorPrefix(state);
        assertTrue(out, errorMessagePrefix);
        for (String[] valid : VALID_STATES) {
            String add = valid[0].replace('0', '1');
            String[] updated = {add, state[1]};
            out = Azul.isStateValid(updated);
            errorMessagePrefix = errorPrefix(updated);
            assertFalse(out, errorMessagePrefix + "state contains extra tiles.");
            String remove = valid[0].replace('1', '0');
            updated = new String[]{remove, state[1]};
            errorMessagePrefix = errorPrefix(updated);
            out = Azul.isStateValid(updated);
            assertFalse(out, errorMessagePrefix + "state is missing tiles.");
        }
        String[][] states = {
                new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"},
                new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FfB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"},
                new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccd"},
        };
        String[] errorSuffix = {
                "incorrect distribution of tile colours.",
                "more than 1 first player tile.",
                "no first player tile",
        };

        for (int i = 0; i < states.length; i++) {
            out = Azul.isStateValid(states[i]);
            errorMessagePrefix = errorPrefix(states[i]);
            assertFalse(out, errorMessagePrefix + errorSuffix[i]);
        }
    }

    @Test
    public void testSameLocation() {
        for (int i = 15; i < FULL_GAME_WITH_MOVES.length; i++) {
            String[] valid = FULL_GAME_WITH_MOVES[i];
            String[] split = valid[1].split("(?=[MSF])");
            for (int j = 0; j < 3; j++) {
                if (split[(2 * j) + 1].length() > 2) {
                    String[] tiles = split[2 * j + 1].substring(1).split("(?<=\\G.{3})");
                    ArrayList<String> test = new ArrayList<>(Arrays.asList(tiles.clone()));
                    test.add(tiles[0]);
                    boolean out = Azul.isStateValid(test.toArray(tiles));
                    String errorMessagePrefix = errorPrefix(test.toArray(tiles));
                    assertFalse(out, errorMessagePrefix + "multiple tiles in the same location");
                    out = Azul.isStateValid(FULL_GAME_WITH_MOVES[i]);
                    errorMessagePrefix = errorPrefix(FULL_GAME_WITH_MOVES[i]);
                    assertTrue(out, errorMessagePrefix);
                }
            }
        }
    }

    @Test
    public void testExtraTilesInStorage() {
        String[] test = {"BFCB1016131715D0000000001", "A1Mb41S0a11c22a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"};
        String errorMessagePrefix = errorPrefix(test);
        boolean out = Azul.isStateValid(test);
        assertTrue(out, errorMessagePrefix);
        String[][] extraTiles = {
                new String[]{"BFCB0916131715D0000000001", "A1Mb41S0a21c32a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016121715D0000000001", "A1Mb41S0a11c32a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB0916131715D0000000001", "A1Mb41S0a11c22a43c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016101715D0000000001", "A1Mb41S0a11c22a33c54d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016131215D0000000001", "A1Mb41S0a11c22a33c24d6FaaaccfB1Mc13S0b11b12a33e44d2Fb"}
        };
        for (String[] state : extraTiles) {
            errorMessagePrefix = errorPrefix(state);
            out = Azul.isStateValid(state);
            assertFalse(out, errorMessagePrefix + "A storage row contains more tiles than the limit.");
        }
    }

    @Test
    public void testRowColoursInMosaic() {
        String[] allValid = {"BFCB1412141614D0000000000", "A0MS0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"};
        boolean out = Azul.isStateValid(allValid);
        String errorMessagePrefix = errorPrefix(allValid);
        assertTrue(out, errorMessagePrefix);
        String[][] invalid = {
                new String[]{"BFCB1312141614D0000000000", "A0Ma00S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1412131614D0000000000", "A0Mc13S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1312141614D0000000000", "A0Ma22S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1412131614D0000000000", "A0Mc30S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1411141614D0000000000", "A0Mb40S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"}
        };
        for (String[] state : invalid) {
            out = Azul.isStateValid(state);
            errorMessagePrefix = errorPrefix(state);
            assertFalse(out, errorMessagePrefix + "mosaic row and storage row contain the same colour.");
        }
    }

    @Test
    public void testTilesInCentre() {
        String[] invalid = {"AFCaaabccccdddeeeeefB1215131109D0002000304", "A0Me04b11S1d12c23a34a1FB1Mc02d33S0d12e13b14a1F"};
        boolean out = Azul.isStateValid(invalid);
        String errorMessagePrefix = errorPrefix(invalid);
        assertFalse(out, errorMessagePrefix + "more than (3 * number of empty factories) + first player tile in centre.");
        for (String[] gameState : FULL_GAME_WITH_MOVES) {
            String[] state = new String[2];
            state[0] = gameState[0];
            state[1] = gameState[1];
            out = Azul.isStateValid(state);
            errorMessagePrefix = errorPrefix(state);
            assertTrue(out, errorMessagePrefix);
        }
    }

    @Test
    public void testTilesOnFloor() {
        String[][] invalid = {
                {"BFCB0805111004D0306050709", "A6Mb00e04d10b11c21a32S2a24a4FabbeeeefB7Mc02e03d04b14e24b31d33S2c24a1Fbbb"},
                {"BFCB0204060401D0909070813", "A5Mb00d03e04d10b11a14c21a32a43S2a23b2FdddfB8Mb00c02e03d04d12b14c21e24b31d33S3c14a1Faaaccceee"}
        };
        for (String[] state : invalid) {
            boolean out = Azul.isStateValid(state);
            String errorMessagePrefix = errorPrefix(state);
            assertFalse(out, errorMessagePrefix + "More than 7 tiles on floor");
        }
        for (String[] gameState : FULL_GAME_WITH_MOVES) {
            String[] state = new String[2];
            state[0] = gameState[0];
            state[1] = gameState[1];
            boolean out = Azul.isStateValid(state);
            String errorMessagePrefix = errorPrefix(state);
            assertTrue(out, errorMessagePrefix);
        }
    }
}


