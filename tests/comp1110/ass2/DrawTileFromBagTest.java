package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class DrawTileFromBagTest {

    private static int BASE_ITERATIONS = 10000;

    @Test
    public void testTiles() {
        int[] count = new int[5];
        String[] sharedState = {"AFCB2020202020D0000000000", "A0MSFB0MSF"};
        for (int i = 0; i < BASE_ITERATIONS; i++) {
            char c = Azul.drawTileFromBag(sharedState);
            assertFalse(c < 'a' || c > 'e', "Expected a char between 'a' and 'e', but you drew: " + c);
            count[c - 'a']++;
        }
        assertTrue(Arrays.stream(count).min().getAsInt() > 0, "Expected your dice A to roll at least one of each value from 'a'-'e' but missed a value");

        double[] expectedProbs = new double[]{1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 5.0};
        double chi = chiSquared(expectedProbs, BASE_ITERATIONS, count);
        assertTrue(chi < 12.84, "Distribution of tiles drawn don't appear to be uniform (chi squared value of " + chi + ")");

    }

    private static double chiSquared(double[] expectedProbs, int samples, int[] counts) {
        double total = 0;
        for (int i = 0; i < expectedProbs.length; i++) {
            double mi = ((double) samples) * expectedProbs[i];
            total += ((double) counts[i] - mi) * ((double) counts[i] - mi) / mi;
        }
        return total;
    }
}
