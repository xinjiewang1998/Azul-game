package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 5000, unit = MILLISECONDS)
public class DrawTileFromBagTest {

    private static int BASE_ITERATIONS = 10000;

    private String errorPrefix(String[] inputState) {
        return "Azul.drawFromBag({\"" + inputState[0] + "\", \"" + inputState[1] + "\"})"
                + System.lineSeparator();
    }

    @Test
    public void testDistribution() {
        int[] count = new int[5];
        String[] state = {"AFCB2020202020D0000000000", "A0MSFB0MSF"};
        String errorMessagePrefix = errorPrefix(state);
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char c = Azul.drawTileFromBag(state);
            assertFalse(c < 'a' || c > 'e', errorMessagePrefix + "expected a char between 'a' and 'e', but you drew " + c);
            count[c - 'a']++;
        }
        assertTrue(Arrays.stream(count).min().getAsInt() > 0, errorMessagePrefix + "expected you to draw at least one of each value from 'a' - 'e' in 10,000 draws, but you missed a value");

        double[] expectedProbs = new double[]{1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0};
        double chi = chiSquared(expectedProbs, BASE_ITERATIONS, count);
        assertTrue(chi < 9.5, errorMessagePrefix + "Distribution of tiles drawn don't appear to be uniform (chi squared value of " + chi + ")");
    }

    private static double chiSquared(double[] expectedProbs, int samples, int[] counts) {
        double total = 0;
        for (int i = 0; i < expectedProbs.length; i++) {
            double mi = ((double) samples) * expectedProbs[i];
            total += ((double) counts[i] - mi) * ((double) counts[i] - mi) / mi;
        }
        return total;
    }

    @Test
    public void testCorrectTile() {
        String[] state = {"BFCB0014151300D0901000011", "A1Ma04c23S0d11b22a33e34d5FbdfB0Mb01S0e11a22b13e44a5Fcccce"};
        int[] count = new int[3];
        String errorMessagePrefix = errorPrefix(state);
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char c = Azul.drawTileFromBag(state);
            assertFalse(c < 'b' || c > 'd', errorMessagePrefix + "expected a char between 'b' and 'd', but you drew: " + c);
            count[c - 'b']++;
        }
        assertTrue(Arrays.stream(count).min().getAsInt() > 0, errorMessagePrefix + "expected you to draw at least one of each value from 'b' - 'd' in 10,000 draws, but you missed a value");
    }

    @Test
    public void testEmptyBag() {
        String[] state = {"AFCB0000000000D1113080909", "A5Me00c01d02a04d10b14a20b21c23e32d43S0b11c22e33a34c3FeeeB11Me00b01d03a04d10e11a13d21b23a30e32a42S1b22c23d44c3Fdf"};
        int[] count = new int[5];
        String errorMessagePrefix = errorPrefix(state);
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char c = Azul.drawTileFromBag(state);
            assertFalse(c < 'a' || c > 'e', errorMessagePrefix + "expected a char between 'a' and 'e', but you drew: " + c);
            count[c - 'a']++;
        }
        assertTrue(Arrays.stream(count).min().getAsInt() > 0, errorMessagePrefix + "expected you to draw at least one of each value from 'a' - 'e' in 10,000 draws, but you missed a value");
    }
}
