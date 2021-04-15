package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static comp1110.ass2.ExampleGames.FULL_GAME_WITH_MOVES;
import static comp1110.ass2.ExampleGames.VALID_STATES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class RefillFactoriesTest {

    private String errorPrefix(String[] inputState, String[] outputState) {
        String result = "Azul.refillFactories({\"" + inputState[0] + "\", \"" + inputState[1] + "\"})"
                + System.lineSeparator()
                + "returned ";
        if (outputState == null) result += "null";
        else result += ("{\"" + outputState[0] + "\", \"" + outputState[1] + "\"}");
        return result + System.lineSeparator();
    }

    @Test
    public void testCorrectNumberOfTiles() {
        for (String[] validState : VALID_STATES) {
            String[] out = Azul.refillFactories(validState.clone());
            assertNotNull(out, "Azul.refillFactories({\"" + validState[0] + "\", \"" + validState[1] + "\"})");
            String sharedState = out[0].substring(1);
            String errorMessagePrefix = errorPrefix(validState, out);
            String fc = sharedState.substring(0, sharedState.indexOf('B'));
            String bag = sharedState.substring(sharedState.indexOf('B'), sharedState.indexOf('D'));
            String discard = sharedState.substring(sharedState.indexOf('D'));
            assertTrue(fc.length() > 2, errorMessagePrefix + "expected factories refilled, but your factories are empty.");
            int[] totalTiles = new int[5];
            for (int i = 1; i < fc.length(); i++) {
                char tile = fc.charAt(i);
                if (tile >= 'a' && tile <= 'e') {
                    totalTiles[tile - 'a']++;
                }
            }
            for (int i = 0; i < 5; i++) {
                int numTiles = Integer.parseInt(bag.substring(i * 2 + 1, (i + 1) * 2 + 1));
                totalTiles[i] += numTiles;
            }
            for (int i = 0; i < 5; i++) {
                int numTiles = Integer.parseInt(discard.substring(i * 2 + 1, (i + 1) * 2 + 1));
                totalTiles[i] += numTiles;
            }
            String[] playerStates = out[1].split("[A-D]");
            for (String playerState : playerStates) {
                if (!playerState.isEmpty()) {
                    String mosaic = playerState.substring(0, playerState.indexOf('S'));
                    for (char c : mosaic.toCharArray()) {
                        if (c >= 'a' && c <= 'e') {
                            totalTiles[c - 'a']++;
                        }
                    }
                    String storage = playerState.substring(playerState.indexOf('S') + 1, playerState.indexOf('F'));
                    for (int i = 0; i < storage.length(); i += 3) {
                        char tile = storage.charAt(i + 1);
                        char numTiles = storage.charAt(i + 2);
                        if (tile >= 'a' && tile <= 'e') {
                            totalTiles[tile - 'a'] += (numTiles - '0');
                        }
                    }
                    String floor = playerState.substring(playerState.indexOf('F'));
                    for (char c : floor.toCharArray()) {
                        if (c >= 'a' && c <= 'e') {
                            totalTiles[c - 'a']++;
                        }
                    }
                }
            }
            for (int i = 0; i < totalTiles.length; i++) {
                assertEquals(20, totalTiles[i], errorMessagePrefix + "incorrect number of tiles of type " + (char) (i + 'a'));
            }
        }
    }

    @Test
    public void testEmptyBag() {
        String[] emptyBag = {"BFCB0000000000D1314121012", "A40Mb01d03e04e10a11c13d14d20a22b23c24e32a33b34d42e43S2e23c3FadfB25Ma00b01e10a11b12d14d20e21c24c30b40c41a44S3d3F"};
        String[] pre = emptyBag[0].split("(?=[BD])")[2].substring(1).split("(?<=\\G.{2})");
        String[] out = Azul.refillFactories(emptyBag.clone());
        String errorMessagePrefix = errorPrefix(emptyBag, out);
        assertNotNull(out, errorMessagePrefix);
        String[] postBag = out[0].split("(?=[BD])")[1].substring(1).split("(?<=\\G.{2})");
        String[] postDisc = out[0].split("(?=[BD])")[2].substring(1).split("(?<=\\G.{2})");
        int[] counts = new int[3];
        for (int i = 0; i < 5; i++) {
            counts[0] += Integer.parseInt(pre[i]);
            counts[1] += Integer.parseInt(postBag[i]);
            counts[2] += Integer.parseInt(postDisc[i]);
        }
        assertEquals(counts[0] - 20, counts[1], errorMessagePrefix + "expected " + (counts[0] - 20) + " tiles in bag, but you had " + counts[1]);
        assertEquals(0, counts[2], errorMessagePrefix + "expected 0 tiles in discard, but you had " + counts[2]);
    }

    @Test
    public void testMissingTiles() {
        String[] missingA = {"AFCfB0013041410D1207120108", "A0MS1a12e23c24a4FB0MS1d22c23a34d3F"};
        String[] out = Azul.refillFactories(missingA.clone());
        String errorMessagePrefix = errorPrefix(missingA, out);
        assertNotNull(out, errorMessagePrefix);
        String[] fact = out[0].split("[FC]");
        int count = 0;
        for (char c : fact[1].toCharArray()) {
            assertNotEquals(c, 'a', errorMessagePrefix + "no 'a' tiles are in the bag, but you placed an 'a' tile in a factory");
            if (c >= 'a' && c <= 'e') {
                count++;
            }
        }
        assertEquals(20, count, errorMessagePrefix + "expected 20 tiles in factories but you had " + count);
    }

    @Test
    public void testPartialBag() {
        String[] state = {"AFCfB0012000006D0903090705", "A0Ma00b01c02d03e10a11c13d14e21a22b23c24c30d31e32b34b40d42e43a44S3a34c4FB0Md00a01e04e11e20b21d22c23e32d33e43S1d22c23a34d4F"};
        String[] out = Azul.refillFactories(state.clone());
        String errorMessagePrefix = errorPrefix(state, out);
        assertNotNull(out, errorMessagePrefix);
        int[] counts = new int[5];
        int count = 0;
        String[] fact = out[0].split("[FC]");
        for (char c : fact[1].toCharArray()) {
            if (c >= 'a' && c <= 'e') {
                counts[c - 'a']++;
                count++;
            }
        }
        assertEquals(20, count, errorMessagePrefix + "expected 20 tiles in factories but you had " + count);
        assertTrue(counts[0] + counts[2] + counts[3] <= 2, errorMessagePrefix + "expected <= 2 'a', 'c' and/or 'd' tiles, but you have:\n"
                + counts[0] + " 'a' tiles,\n" + counts[2] + " 'c' tiles,\n" + counts[3] + " 'd' tiles.");
    }

    @Test
    public void testFactoriesNotEmpty() {
        for (int i = 0; i < 9; i++) {
            String[] state = new String[2];
            state[0] = FULL_GAME_WITH_MOVES[i][0];
            state[1] = FULL_GAME_WITH_MOVES[i][1];
            String[] out = Azul.refillFactories(state.clone());
            String errorMessagePrefix = errorPrefix(state, out);
            assertEquals(Arrays.toString(state), Arrays.toString(out), errorMessagePrefix + "factories and/or centre are not empty, factories cannot be refilled");
        }
        String[] out = Azul.refillFactories(VALID_STATES[1].clone());
        String errorMesagePrefix = errorPrefix(VALID_STATES[1], out);
        assertNotEquals(Arrays.toString(VALID_STATES[1]), Arrays.toString(out), errorMesagePrefix + "expected factories to be refilled");
    }
}
