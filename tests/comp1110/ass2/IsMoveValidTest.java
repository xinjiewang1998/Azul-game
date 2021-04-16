package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static comp1110.ass2.ExampleGames.FULL_GAME_WITH_MOVES;
import static comp1110.ass2.ExampleGames.VALID_STATES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class IsMoveValidTest {


    private String errorPrefix(String[] inputState, String move) {
        return "Azul.isMoveValid({\"" + inputState[0] + "\", \"" + inputState[1] + "\", \"" + move + "\"})"
                + System.lineSeparator();
    }

    @Test
    public void testTilingMove() {
        String[] trivial = {"AF0cdee1bdde2abbe3bcde4aaaeCfB1616181614D0000000000", "A0MSFB0MSF"};
        String move = "A0aF";
        String errorMessagePrefix = errorPrefix(trivial, move);
        boolean out = Azul.isMoveValid(trivial, move);
        assertFalse(out, errorMessagePrefix + "expected false, there are no 'a' tiles in factory 0");

        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] fullMove = FULL_GAME_WITH_MOVES[i];
            if (fullMove.length == 2) {
                continue;
            }
            if (fullMove[2].length() == 3) {
                String[] gameState = {fullMove[0], fullMove[1]};
                move = fullMove[2];
                out = Azul.isMoveValid(gameState, move);
                errorMessagePrefix = errorPrefix(gameState, move);
                assertTrue(out, errorMessagePrefix + "tiling move is valid");
            }
        }
    }

    @Test
    public void testDraftingMove() {
        String[] trivial = {"AF0acde2cdde3acde4aadeCccefB1215131109D0002000304", "A0Me04b11S2c13a34a1FB1Mc02d33S1b12e13b1F"};
        String move = "A0bF";
        String errorMessagePrefix = errorPrefix(trivial, move);
        boolean out = Azul.isMoveValid(trivial, move);
        assertFalse(out, errorMessagePrefix + "expected false, there are no 'b' tiles in factory 0");
        for (int i = 0; i < FULL_GAME_WITH_MOVES.length - 1; i++) {
            String[] fullMove = FULL_GAME_WITH_MOVES[i];
            if (fullMove.length == 2) {
                continue;
            }
            if (fullMove[2].length() == 4) {
                String[] gameState = {fullMove[0], fullMove[1]};
                move = fullMove[2];
                out = Azul.isMoveValid(gameState, move);
                errorMessagePrefix = errorPrefix(gameState, move);
                assertTrue(out, errorMessagePrefix + "drafting move is valid");
            }
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
                    String errorMessagePrefix = errorPrefix(gameState, move);
                    boolean out = Azul.isMoveValid(gameState, move);
                    assertFalse(out, errorMessagePrefix + "Storage row is not full");
                }
            }
        }
        String[] valid = {"AFCB1416181516D0000000000", "A0MS0a11c12d13e24b4FaaafB0MS1c12a23e24d4F"};
        String errorMessagePrefix = errorPrefix(valid, "A00");
        boolean out = Azul.isMoveValid(valid, "A00");
        assertTrue(out, errorMessagePrefix);
    }

    @Test
    public void testMosaicColumnColour() {
        String[] gameState = {"AFCB1314131511D0001000002", "A8Ma00c02b11c13e22b23d31a34c40S3c44e5FfB1Mb01e10c24d31e43S1a22b33a44d5F"};
        String[] moves = {"A30", "A32", "A33"};
        for (String move : moves) {
            String errorMessagePrefix = errorPrefix(gameState, move);
            boolean out = Azul.isMoveValid(gameState, move);
            assertFalse(out, errorMessagePrefix + "Mosaic column already contains this colour");
        }
        String move = "A3F";
        String errorMessagePrefix = errorPrefix(gameState, move);
        boolean out = Azul.isMoveValid(gameState, move);
        assertTrue(out, errorMessagePrefix + "Mosaic column does not contain this colour, move is valid");
    }

    @Test
    public void testLocationOccupied() {
        String[] gameState = {"AFCB0812081010D0000040201", "A32Ma00b01c02e04a11b12c13d14e21a22b23d31a34c40e42S1e23c34d5FbB16Mb01e10a11d14a22b23c24d31a33d42e43S0a11c22e33b34a5Ff"};
        String[] moves = {"A11", "A12", "A13", "A14"};
        for (String move : moves) {
            String errorMessagePrefix = errorPrefix(gameState, move);
            boolean out = Azul.isMoveValid(gameState, move);
            assertFalse(out, errorMessagePrefix + "Mosaic location is already occupied");
        }
        String move = "A10";
        String errorMessagePrefix = errorPrefix(gameState, move);
        boolean out = Azul.isMoveValid(gameState, move);
        assertTrue(out, errorMessagePrefix + "Mosaic location is not occupied, move is valid");
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
                String move = "" + player + row + "F";
                boolean out = Azul.isMoveValid(updated, move);
                String errorMessagePrefix = errorPrefix(updated, move);
                if (move.equals("A3F")) {
                    assertTrue(out, errorMessagePrefix + "no other valid move from Storage to Mosaic exists, so a move to the floor is valid");
                } else {
                    assertFalse(out, errorMessagePrefix + "a valid move from Storage to Mosaic exists");
                }
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
                    String errorMessagePrefix = errorPrefix(updated, move);
                    boolean out = Azul.isMoveValid(updated, move);
                    if (!tiles[f].contains(col)) {
                        assertFalse(out, errorMessagePrefix + "factory " + f + " doesn't contain a(n) '" + col + "' tile");
                    }
                    else {
                        assertTrue(out, errorMessagePrefix + "factory " + f + " does contain a(n) '" + col + "' tile");
                    }
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
                boolean expected = tiles[i].contains(col) && sTiles[c].contains(col);
                boolean out = Azul.isMoveValid(gameState, move);
                String errorMessagePrefix = errorPrefix(gameState, move);
                if (expected) {
                    assertTrue(out, errorMessagePrefix);
                }
                else {
                    assertFalse(out, errorMessagePrefix + "factory " + i + " doesn't contain this colour, or storage row " + c + " already contains a different colour" );

                }
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
                        String errorMessagePrefix = errorPrefix(updated, move);
                        boolean out = Azul.isMoveValid(updated, move);
                        if (player.equals("A")) {
                            assertEquals(col != 'a', out, errorMessagePrefix + "Mosaic row " + row + " already contains a(n) '" + col +"' tile");
                        } else {
                            assertEquals(col != 'b', out, errorMessagePrefix + "Mosaic row " + row + " already contains a(n) '" + col +"' tile");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testWrongPlayer() {
        boolean out = Azul.isMoveValid(VALID_STATES[0], "A0c1");
        String errorMessagePrefix = errorPrefix(VALID_STATES[0], "A0c1");
        assertTrue(out, errorMessagePrefix);
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
            errorMessagePrefix = errorPrefix(gameState, move);
            out = Azul.isMoveValid(gameState, move);
            assertFalse(out, errorMessagePrefix + "it is not this player's turn");
        }
    }

    @Test
    public void testFactoryToFloor() {
        String[] gameState = {"F0ceee1bcdd2acce3adee4abdeCfB1718161613D0000000000", "A0MSFB0MSF"};
        String[] trivial = {"AF0ceee1bcdd2acce3adee4abdeCfB1718161613D0000000000", "A0MSFB0MSF"};
        String move = "A1a1";
        String errorMessagePrefix = errorPrefix(gameState, move);
        boolean out = Azul.isMoveValid(trivial, move);
        assertFalse(out, errorMessagePrefix + "there are no 'a' tiles in factory 1");

        String[] tiles = gameState[0].substring(1, gameState[0].indexOf('C')).split("(?<=\\G.{5})");
        String[] updated = new String[2];
        updated[1] = gameState[1];

        for (int p = 0; p < 2; p++) {
            String player = "" + (char) (p + 'A');
            updated[0] = "" + player + gameState[0];
            for (int f = 0; f < 5; f++) {
                for (int c = 1; c < 5; c++) {
                    char colour = tiles[f].charAt(c);
                    move = player + f + colour + 'F';
                    errorMessagePrefix = errorPrefix(updated, move);
                    out = Azul.isMoveValid(updated, move);
                    assertTrue(out, errorMessagePrefix + "move to floor is valid");
                }
            }
        }

    }
}
