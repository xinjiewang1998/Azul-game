package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class NextRoundTest {

    private String errorPrefix(String[] inputState, String[] outputState) {
        return "Azul.nextRound({\"" + inputState[0] + "\", \"" + inputState[1] + "\"})"
                + System.lineSeparator()
                + "returned {\"" + outputState[0] + "\", \"" + outputState[1] + "\"};"
                + System.lineSeparator();
    }

    private final String[][] GAME_STATES = {
            new String[]{"AFCB0808050810D0306040401", "A17Me00c01d02b03a04d10c12b14a20b21c23e24e32d43S3a34c3FeeeB16Me00b01d03a04d10e11a13b14d21b23a30e32d34a42S2c24c3Fdf"},
            new String[]{"AFCB1004061006D0410030304", "A7Me00a01b02c03d04e12c20a22e23b34d40a44S1d12b13c3FeeeeB26Md00b01a02c03e04a10e12d14e20b22d23c24d31a43S4c4Fbf"},
            new String[]{"BFCB0907091213D0000000000", "A19Me00a01c03b04b10e11d12a14d20c21a22b23e24a30d31c32b42c44S4e2FbbbbbccB11Ma00c01e02d04b11d12e13a14d20a22b24c30d31b33a41d43S3a3Fbcccf"},
            new String[]{"BFCB0805060805D0405030709", "A36Me01d02c03b04a10d11b12e13c14c20b21d23e30S2e13c14a2FabbbfB12Mc00e02a03d04a10e11b12c14c21a22b24d42b43S3c3Fabc"},
            new String[]{"AFCB0001040100D1210051208", "A0Mc00d02e03a04b10e11c13b22c24a30b31e40d44S1a13c34a1FbbdeeeeB18Mc00b01a02d03e04d10b12a13c14e22c23a24e33e41c42b44S1e12b13a1Fcddf"},
            new String[]{"BFCB0000060103D1111050511", "A15Ma00c01e02d03b04b12e13a14d20b21a23e24e30c33d34c40S1c12c1FaaafB7Mb00c01e02a04d10b12c14c20e21a23b24d33a40d41c42S3b3Fddddddd"},
            new String[]{"AFCB0703080106D0411011105", "A7Md00a02b03e04d11e13a20c24c30e40S1c12b2FaaaaB11Ma00c01b02d03e04c12a13b14d20b21c23a32S1e13c34e4Fccddddf"},
            new String[]{"BFCB0606060308D0407080801", "A29Me00a01c02b04d10c11e13a14b20d22a23c24a40S3e24b2FafB12Ma00d01b02e04b10a11c12e13d14d23b24c43S3e14e4Faaacdddd"},
            new String[]{"AFCB0201050507D1011040204", "A7Mb01c02e03a04e10a12b13d21b22c23d30c31c44S1c13b14d4FaaadddfB14Mc00e01d02b03a04a11d13d20c22a23b24e34d41e43S1c13c14c2Fbbeeee"},
            new String[]{"AFCB1207080506D0107030805", "A21Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FeeeefB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2Fcc"},
    };

    private final String[][] END_STATES = {
            new String[] {"BFCfB0808050810D0306040504", "A15Me00c01d02b03a04d10c12b14a20b21c23e24e32d43S3a34c3FB14Me00b01d03a04d10e11a13b14d21b23a30e32d34a42S2c24c3F"},
            new String[] {"BFCfB1004061006D0411030308", "A3Me00a01b02c03d04e12c20a22e23b34d40a44S1d12b13c3FB26Md00b01a02c03e04a10e12d14e20b22d23c24d31a43S4c4F"},
            new String[] {"BFCfB0907091213D0006050000", "A7Me00a01c03b04b10e11d12a14d20c21a22b23e24a30d31c32b42c44S4e2FB13Ma00c01e02d04b11d12e13a14d20a22b24c30d31b33a41d43S3a3F"},
            new String[] {"AFCfB0805060805D0609040709", "A30Me01d02c03b04a10d11b12e13c14c20b21d23e30S2e13c14a2FB8Mc00e02a03d04a10e11b12c14c21a22b24d42b43S3c3F"},
            new String[] {"BFCfB0001040100D1212061512", "A0Mc00d02e03a04b10e11c13b22c24a30b31e40d44S1a13c34a1FB14Mc00b01a02d03e04d10b12a13c14e22c23a24e33e41c42b44S1e12b13a1F"},
            new String[] {"AFCfB0000060103D1411051211", "A11Ma00c01e02d03b04b12e13a14d20b21a23e24e30c33d34c40S1c12c1FB0Mb00c01e02a04d10b12c14c20e21a23b24d33a40d41c42S3b3F"},
            new String[] {"BFCfB0703080106D0811031505", "A1Md00a02b03e04d11e13a20c24c30e40S1c12b2FB2Ma00c01b02d03e04c12a13b14d20b21c23a32S1e13c34e4F"},
            new String[] {"AFCfB0606060308D0807091201", "A27Me00a01c02b04d10c11e13a14b20d22a23c24a40S3e24b2FB2Ma00d01b02e04b10a11c12e13d14d23b24c43S3e14e4F"},
            new String[] {"AFCfB0201050507D1313040508", "A0Mb01c02e03a04e10a12b13d21b22c23d30c31c44S1c13b14d4FB5Mc00e01d02b03a04a11d13d20c22a23b24e34d41e43S1c13c14c2F"},
            new String[] {"AFCfB1207080506D0107050809", "A25Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2F"},
    };

    private final String[][] INPUT_STATES = {
            new String[]{"BFCB1516191814D0000000000", "A2Ma04c23S1b13e34d2FfB1Mb01S2b13e34a4Fb"}, // deduction less than or equal to current score
            new String[]{"BFCB1110081013D0303040201", "A11Me00c01d02a04d10b14a20b21c23e32d43S1c12e14c2FbbbfB13Me00b01d03a04d10e11a13d21b23a30e32a42S4c3Fdd"},
            new String[]{"AFCB0808110711D0107020502", "A19Md00c01b02a03d11a12c13a21c22d34S3a34a4FcdddB16Md00c02a03e04c11d12e13b14e22b23c34e40S3b2Feeef"},
            new String[]{"BFCB1615151714D0001000000", "A6Md02a04b14a20c23d43S3e3FbdfB6Me00b01a13e32a42S2b1Fcccce"}, // deduction greater than current score
            new String[]{"BFCB1213111512D0002040101", "A8Me00d02a04d10b14a20b21c23d43S3e34c2FdB5Me00b01a04e11a13b23e32a42S4c2Faaabf"},
            new String[]{"BFCB1313141615D0002010000", "A3Mc01a03a12d34S1c12a24a3FcddfB6Mc02e04d12b14b23c34S4e4Fbbb"},
    };

    private final String[][] OUTPUT_STATES = {
            new String[]{"AF0aade1abbd2aace3ccde4acddCfB0914151311D0001000000", "A1Ma04c23S1b13e34d2FB0Mb01S2b13e34a4F"},
            new String[]{"AF0abcd1bcde2bdee3accd4adeeCfB0807040508D0306040401", "A5Me00c01d02a04d10b14a20b21c23e32d43S1c12e14c2FB11Me00b01d03a04d10e11a13d21b23a30e32a42S4c3F"},
            new String[]{"BF0bbcd1abee2accc3acde4abeeCfB0404060506D0107030805", "A13Md00c01b02a03d11a12c13a21c22d34S3a34a4FB10Md00c02a03e04c11d12e13b14e22b23c34e40S3b2F"},
            new String[]{"AF0bbcd1acee2bcdd3aace4abbbCfB1209111411D0002040101", "A2Md02a04b14a20c23d43S3e3FB0Me00b01a13e32a42S2b1F"},
            new String[]{"BF0acdd1acdd2abbe3cddd4abdeCfB0810080710D0303040201", "A7Me00d02a04d10b14a20b21c23d43S3e34c2FB0Me00b01a04e11a13b23e32a42S4c2F"},
            new String[]{"AF0bcee1abee2aade3bddd4aabeCfB0809131209D0005020200", "A0Mc01a03a12d34S1c12a24a3FB2Mc02e04d12b14b23c34S4e4F"},
    };

    @Test
    public void testEndOfGameSharedState() {
        for (int i = 0; i < GAME_STATES.length; i++) {
            String[] out = Azul.nextRound(GAME_STATES[i].clone());
            assertNotNull(out, "Azul.nextRound({\"" + GAME_STATES[i][0] + "\", \"" + GAME_STATES[i][1] + "\"})"
                    + System.lineSeparator()
                    + "returned null");

            String errorMessagePrefix = errorPrefix(GAME_STATES[i], out);
            String outSharedState = out[0].substring(1);
            String outFc = outSharedState.substring(0, outSharedState.indexOf('B'));
            String outBag = outSharedState.substring(outSharedState.indexOf('B'), outSharedState.indexOf('D'));
            String outDiscard = outSharedState.substring(outSharedState.indexOf('D'));
            String sharedState = END_STATES[i][0].substring(1);

            String fc = sharedState.substring(0, sharedState.indexOf('B'));
            String bag = sharedState.substring(sharedState.indexOf('B'), sharedState.indexOf('D'));
            String discard = sharedState.substring(sharedState.indexOf('D'));


            assertEquals(fc, outFc, errorMessagePrefix + "expected factories to be empty at the end of game.");
            assertEquals(bag, outBag, errorMessagePrefix + "expected bag to be unchanged at the end of game.");
            assertEquals(outDiscard, discard, errorMessagePrefix + "expected discard to be updated with tiles from the floor.");

        }

    }

    @Test
    public void testEndOfGamePlayerStates() {
        for (int i = 0; i < GAME_STATES.length; i++) {
            String[] out = Azul.nextRound(GAME_STATES[i].clone());
            assertNotNull(out, "Azul.nextRound({\"" + GAME_STATES[i][0] + "\", \"" + GAME_STATES[i][1] + "\"})"
                    + System.lineSeparator()
                    + "returned null");
            String errorMessagePrefix = errorPrefix(GAME_STATES[i], out);

            String outPlayerA = out[1].substring(0, out[1].indexOf('B'));
            String outPlayerB = out[1].substring(out[1].indexOf('B'));
            int outAScore = Integer.parseInt(outPlayerA.substring(1, outPlayerA.indexOf('M')));
            int outBScore = Integer.parseInt(outPlayerB.substring(1, outPlayerB.indexOf('M')));

            String playerA = END_STATES[i][1].substring(0, END_STATES[i][1].indexOf('B'));
            String playerB = END_STATES[i][1].substring(END_STATES[i][1].indexOf('B'));
            int aScore = Integer.parseInt(playerA.substring(1, playerA.indexOf('M')));
            int bScore = Integer.parseInt(playerB.substring(1, playerB.indexOf('M')));

            String aFloor = outPlayerA.substring(outPlayerA.indexOf('F'));
            String bFloor = outPlayerB.substring(outPlayerB.indexOf('F'));

            assertEquals(1, aFloor.length(), errorMessagePrefix + "expected playerA's floor to be empty");
            assertEquals(1, bFloor.length(), errorMessagePrefix + "expected playerB's floor to be empty");

            assertEquals(aScore, outAScore, errorMessagePrefix + "expected playerA's score to be " + aScore +
                    ", but your score was " + outAScore);
            assertEquals(bScore, outBScore, errorMessagePrefix + "expected playerB's score to be " + bScore +
                    ", but your score was " + outBScore);

            assertEquals(END_STATES[i][1], out[1], errorMessagePrefix + "expected playerState to be unchanged because the game has already ended.");
        }
    }

    @Test
    public void testNotNextRound() {
        for (int i = 0; i < 13; i++) {
            String[] state = new String[2];
            state[0] = ExampleGames.FULL_GAME_WITH_MOVES[i][0];
            state[1] = ExampleGames.FULL_GAME_WITH_MOVES[i][1];
            String[] out = Azul.nextRound(state.clone());
            assertNotNull(out, "Azul.nextRound({\"" + state[0] + "\", \"" + state[1] + "\"})"
                    + System.lineSeparator()
                    + "returned null");
            String errorMessagePrefix = errorPrefix(state, out);
            assertEquals(state[0], out[0], errorMessagePrefix + "factories were not empty or storage row(s) were full, so the round has not finished.");
        }
        String[] state = new String[2];
        state[0] = ExampleGames.FULL_GAME_WITH_MOVES[13][0];
        state[1] = ExampleGames.FULL_GAME_WITH_MOVES[13][1];
        String[] out = Azul.nextRound(state.clone());
        String stateFC = state[0].substring(state[0].indexOf('F'), state[0].indexOf('B'));
        String outFC = out[0].substring(1);
        outFC = outFC.substring(outFC.indexOf('F'), outFC.indexOf('B'));
        String errorMessagePrefix = errorPrefix(state, out);
        assertNotEquals(stateFC, outFC, errorMessagePrefix + "end of round, expected factories to be refilled.");
    }

    @Test
    public void testFloorDeduction() {
        for (int i = 0; i < INPUT_STATES.length; i++) {
            String[] out = Azul.nextRound(INPUT_STATES[i].clone());
            assertNotNull(out, "Azul.nextRound({\"" + INPUT_STATES[i][0] + "\", \"" + INPUT_STATES[i][1] + "\"})"
                    + System.lineSeparator()
                    + "returned null");

            String errorMessagePrefix = errorPrefix(INPUT_STATES[i], out);

            String outPlayerA = out[1].substring(0, out[1].indexOf('B'));
            String outPlayerB = out[1].substring(out[1].indexOf('B'));
            int outAScore = Integer.parseInt(outPlayerA.substring(1, outPlayerA.indexOf('M')));
            int outBScore = Integer.parseInt(outPlayerB.substring(1, outPlayerB.indexOf('M')));

            String playerA = OUTPUT_STATES[i][1].substring(0, OUTPUT_STATES[i][1].indexOf('B'));
            String playerB = OUTPUT_STATES[i][1].substring(OUTPUT_STATES[i][1].indexOf('B'));
            int aScore = Integer.parseInt(playerA.substring(1, playerA.indexOf('M')));
            int bScore = Integer.parseInt(playerB.substring(1, playerB.indexOf('M')));

            assertEquals(aScore, outAScore, errorMessagePrefix + "expected playerA's score to be " + aScore +
                    ", but your score was " + outAScore);
            assertEquals(bScore, outBScore, errorMessagePrefix + "expected playerB's score to be " + bScore +
                    ", but your score was " + outBScore);
        }
    }

    @Test
    public void testCorrectPlayer() {
        for (int i = 0; i < INPUT_STATES.length; i++) {
            String[] out = Azul.nextRound(INPUT_STATES[i].clone());
            assertNotNull(out, "Azul.nextRound({\"" + INPUT_STATES[i][0] + "\", \"" + INPUT_STATES[i][1] + "\"})"
                    + System.lineSeparator()
                    + "returned null");

            String errorMessagePrefix = errorPrefix(INPUT_STATES[i], out);
            char outPlayer = out[0].charAt(0);
            char expectedPlayer = OUTPUT_STATES[i][0].charAt(0);
            assertEquals(expectedPlayer, outPlayer, errorMessagePrefix + "expected it to be " + expectedPlayer + "'s turn.");
        }
    }
}