package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static comp1110.ass2.ExampleGames.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsStateValidTest {

    private void test(String[] in, boolean expected) {
        boolean out = Azul.isStateValid(in);
        assertEquals(expected, out, "isStateValid for input state: " + in[0] + "', " + in[1] + "'");
    }

    @Test
    public void testValid() {
        for (String[] validState : VALID_STATES) {
            test(validState, true);
        }
        String[] invalid = new String[]{NOT_WELL_FORMED_SHARED_STATE[0], NOT_WELL_FORMED_PLAYER_STATE[1]};
        test(invalid, false);
    }

    @Test
    public void testNotWellFormed() {
        test(VALID_STATES[0], true); // test for known well-formed state
        for (int i = 0; i < NOT_WELL_FORMED_SHARED_STATE.length; i++) {
            String[] state = {VALID_STATES[i][1], NOT_WELL_FORMED_SHARED_STATE[i]};
            test(state, false);
        }
        for (int i = 0; i < NOT_WELL_FORMED_PLAYER_STATE.length; i++) {
            String[] state = {VALID_STATES[i][0], NOT_WELL_FORMED_PLAYER_STATE[i]};
            test(state, false);
        }
    }

    @Test
    public void testCorrectTiles() {
        String[] state = {"AFCB0610110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"};
        test(state, true); // correct number of tiles
        for (String[] valid : VALID_STATES) {
            String add = valid[0].replace('0', '1');
            String[] updated = {add, state[1]};
            test(updated, false); // extra tiles
            String remove = valid[0].replace('1', '0');
            updated = new String[]{remove, state[1]};
            test(updated, false); // missing tiles
        }

        state = new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"};
        test(state, false); //correct number but incorrect colours
        state = new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FfB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccdf"};
        test(state, false); // more than 1 first player tile
        state = new String[]{"AFCB0511110905D0804020113", "A3Ma00d02e03d13c21b24a31c42S2d13d34a3FB5Mb00c01a03d04d21b31e41S1d12b14b1Fbccccd"};
        test(state, false); // no first player tile

    }

    @Test
    public void testSameLocation() {
        for (int i = 15; i < FULL_GAME_WITH_MOVES.length; i++) {
            String[] valid = FULL_GAME_WITH_MOVES[i];
            String[] split = valid[1].split("(?=[MSF])");
            System.out.println(Arrays.toString(split));
            for (int j = 0; j < 3; j++) {
                if (split[(2 * j) + 1].length() > 2) {
                    String[] tiles = split[2 * j + 1].substring(1).split("(?<=\\G.{3})");
                    ArrayList<String> test = new ArrayList<>(Arrays.asList(tiles.clone()));
                    System.out.println(Arrays.toString(tiles));
                    System.out.println(test.toString());
                    test.add(tiles[0]);
                    test(test.toArray(tiles), false);
                    System.out.println(Arrays.toString(FULL_GAME_WITH_MOVES[i]));
                    test(FULL_GAME_WITH_MOVES[i], true);
                }
            }
        }
    }

    @Test
    public void testTilesInStorage() {
        String[] test = {"BFCB1016131715D0000000001", "A1Mb41S0a11c22a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"};
        test(test, true);
        String[][] extraTiles = {
                new String[]{"BFCB0916131715D0000000001", "A1Mb41S0a21c32a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016121715D0000000001", "A1Mb41S0a11c32a33c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB0916131715D0000000001", "A1Mb41S0a11c22a43c24d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016101715D0000000001", "A1Mb41S0a11c22a33c54d1FaaaccfB1Mc13S0b11b12a33e44d2Fb"},
                new String[]{"BFCB1016131215D0000000001", "A1Mb41S0a11c22a33c24d6FaaaccfB1Mc13S0b11b12a33e44d2Fb"}
        };
        for (String[] state : extraTiles) {
            test(state, false);
        }
    }

    @Test
    public void testRowColours() {
        String[] allValid = {"BFCB1412141614D0000000000", "A0MS0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"};
        test(allValid, true);
        String[][] invalid = {
                new String[]{"BFCB1312141614D0000000000", "A0Ma00S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1412131614D0000000000", "A0Mc13S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1312141614D0000000000", "A0Ma22S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1412131614D0000000000", "A0Mc30S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"},
                new String[]{"BFCB1411141614D0000000000", "A0Mb40S0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff"}
        };
        for (String[] states : invalid) {
            test(states, false);
        }
    }


// AFCfB1515151515D0000000000, A0Ma00b01c02d03e04e10a11b12c13d14d20e21a22b23c24c30d31e32a33b34b40c41d42e43a44SFB0MSF full mosaic example.

// full storage rows, all valid.
// BFCB1412141614D0000000000, A0MS0a11c22a33c44b5FB0MS0e11a22b33d44e5Ff
}


