package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static comp1110.ass2.ExampleGames.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class RefillFactoriesTest {

    @Test
    public void testCorrectNumberOfTiles() {
        for (String[] validState : VALID_STATES) {
            String[] out = Azul.refillFactories(validState);
            assertNotNull(out, "Azul.refillFactories({\"" + validState[0] + "\", \"" + validState[1] + "\"})");
            String sharedState = out[0].substring(1);
            String errorMessagePrefix = "Azul.refillFactories({\"" + validState[0] + "\", \"" + validState[1] + "\"})"
                    + System.lineSeparator()
                    + "returned {\"" + out[0] + "\", \"" + out[1] + "\"};"
                    + System.lineSeparator();
            String fc = sharedState.substring(0, sharedState.indexOf('B'));
            String bag = sharedState.substring(sharedState.indexOf('B'), sharedState.indexOf('D'));
            String discard = sharedState.substring(sharedState.indexOf('D'));
            int[] totalTiles = new int[5];
            for (int i = 1; i < fc.length(); i++) {
                char tile = fc.charAt(i);
                if (tile >= 'a' && tile <= 'e') {
                    totalTiles[tile - 'a']++;
                }
            }
            for (int i = 0; i < 5; i++) {
                int numTiles = Integer.valueOf(bag.substring(i * 2 + 1, (i + 1) * 2 + 1));
                totalTiles[i] += numTiles;
            }
            for (int i = 0; i < 5; i++) {
                int numTiles = Integer.valueOf(discard.substring(i * 2 + 1, (i + 1) * 2 + 1));
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
}
