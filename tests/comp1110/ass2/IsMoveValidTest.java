package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static comp1110.ass2.ExampleGames.*;

import java.util.Arrays;
import java.util.Random;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsMoveValidTest {

    private void test(String[] gameState, String move, boolean expected) {
        boolean out = Azul.isMoveValid(gameState, move);
        assertEquals(expected, out, "isMoveValid for input state: " + Arrays.toString(gameState) + ", and move: '" + move + "'");
    }

    @Test
    public void testValid() {
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] fullMove = FULL_GAME_WITH_MOVES[i];
            if (fullMove.length == 2) {
                continue;
            }
            String[] gameState = {fullMove[0], fullMove[1]};
            String move = fullMove[2];
            test(gameState, move, true);
        }
    }

    @Test
    public void testStorageNotFull() {
        String[] gameState = {"FCB1516181516D0000000000", "A0MS1c12d13e24b4FaaafB0MS1c12a23e24d4F"};
        for (int p = 0; p < 2; p++) {
            char player = (char) (p + 'A');
            gameState[0] = "" + player + gameState[0];
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    String move = "" + player + row + col;
                    test(gameState, move, false);
                }
            }
        }
        String[] valid = {"AFCB1416181516D0000000000", "A0MS0a11c12d13e24b4FaaafB0MS1c12a23e24d4F"};
        test(valid, "A00", true);
    }

    @Test
    public void testMosaicColumnColour() {
        String[] gameState = {"AFCB1314131511D0000000000", "A4Mc02c13b23d31a34c40S0a11b22e33c44e5FfB1Mb01e10c24d31e43S1a22b33a44d5F"};
        String[] moves = {"A30", "A32", "A33"};
        for (String m : moves) {
            test(gameState, m, false);
        }
        test(gameState, "A03", true);
    }

    @Test
    public void testLocationOccupied() {
        String[] gameState = {"AFCB0812081010D0000040201", "A32Ma00b01c02e04a11b12c13d14e21a22b23d31a34c40e42S1e23c34d5FbB16Mb01e10a11d14a22b23c24d31a33d42e43S0a11c22e33b34a5Ff"};
        String[] moves = {"A11", "A12", "A13", "A14"};
        for (String m : moves) {
            test(gameState, m, false);
        }
        test(gameState, "A10", true);
    }

    @Test
    public void testStorageToFloor() {
        String[] gameState = {"FCB1314131511D0000000000", "A4Mc02c13b23d31a34c40S0a11b22e33c44e5FfB1Mb01e10c24d31e43S1a22b33a44d5F"};
        String[] updated = new String[2];
        updated[1] = gameState[1];
        for (int p = 0; p < 2; p++) {
            char player = (char) (p + 'A');
            updated[0] = "" + player + gameState[0];
            for (int row = 0; row < 5; row++) {
                boolean expected = false;
                String move = "" + player + row + "F";
                if (move.equals("A3F")) {
                    expected = true;
                }
                test(updated, move, expected);
            }
        }
    }

    @Test
    public void testFactoryColour() {
        String[] gameState = {"F0ceee1bcdd2acce3adee4abdeCfB1718161613D0000000000", "A0MSFB0MSF"};
        String[] updated = new String[2];
        updated[1] = gameState[1];
        String[] tiles = gameState[0].substring(1).split("(?<=\\G.{5})");
        Random rand = new Random();
        for (int p = 0; p < 2; p++) {
            String player = "" + (char) (p + 'A');
            updated[0] = "" + player + gameState[0];
            for (int f = 0; f < 5; f++) {
                for (int c = 0; c < 5; c++) {
                    String col = "" + (char) (c + 'a');
                    String move = player + f + col + rand.nextInt(5);
                    test(updated, move, tiles[f].contains(col));
                }
            }
        }
    }


    @Test
    public void testStorageColour() {
        String[] gameState = {"AF0bcde1aaab2aaee3bdee4acdeCfB0406060608D0606090503", "A0Ma00b02d04b10c13d23a33b44S1d12a13b1FB4Mb00d01a04e10c11e23d34e42S1d14c1F"};
        String storage = gameState[1].split("(?=[SF])")[1];
        String[] tiles = gameState[0].substring(2).split("(?<=\\G.{5})");
        String[] sTiles = storage.substring(1).split("(?<=\\G.{3})");
        for (int c = 0; c < sTiles.length; c++) {
            String col = "" + sTiles[c].charAt(1);
            for (int i = 0; i < 5; i++) {
                int row = c + 1;
                String move = "A" + i + col + row;
                test(gameState, move, tiles[i].contains(col) && sTiles[c].contains(col));
            }
        }
    }

    @Test
    public void testMosaicRowColor() {
        String[] gameState = {"F0abcc1acce2bbcd3bcce4adeeCfB1211131816D0000000000", "A0Ma00a11a22a33a44SFB0Mb04b10b21b32b43SF"};
        String[] updated = new String[2];
        updated[1] = gameState[1];
        String[] tiles = gameState[0].substring(1).split("(?<=\\G.{5})");
        for (int p = 0; p < 2; p++) {
            String player = "" + (char) (p + 'A');
            updated[0] = "" + player + gameState[0];
            for (int f = 0; f < 5; f++) {
                for (int c = 1; c < 5; c++) {
                    for (int row = 0; row < 5; row++) {
                        char col = tiles[f].charAt(c);
                        String move = player + f + col + row;
                        if (player.equals("A")) {
                            test(updated, move, col != 'a');
                        } else {
                            test(updated, move, col != 'b');
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testWrongPlayer() {
        // test trivial correct move
        test(VALID_STATES[0], "A0c1", true);
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] fullMove = FULL_GAME_WITH_MOVES[i];
            if (fullMove.length == 2) {
                continue;
            }
            String[] gameState = {fullMove[0], fullMove[1]};
            String move = fullMove[2];
            if (move.charAt(0) == 'A') {
                move = "B" + move.substring(1);
            } else {
                move = "A" + move.substring(1);
            }
            test(gameState, move, false);
        }
    }
}
