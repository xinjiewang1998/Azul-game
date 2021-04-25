package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class GetBonusPointsTest {

    private final String[][] END_GAME_STATES = {
            new String[]{"AFCB1207080506D0107030805", "A21Md00c01b02a03e04d11a12c13a21c22a30d34a44S2b2FeeeefB24Md00c02a03e04a10c11d12e13b14c20e22b23b32c34e40S4d2Fcc"},
            new String[]{"AFCB0808050810D0306040401", "A17Me00c01d02b03a04d10c12b14a20b21c23e24e32d43S3a34c3FeeeB16Me00b01d03a04d10e11a13b14d21b23a30e32d34a42S2c24c3Fdf"},
            new String[]{"AFCB1004061006D0410030304", "A7Me00a01b02c03d04e12c20a22e23b34d40a44S1d12b13c3FeeeeB26Md00b01a02c03e04a10e12d14e20b22d23c24d31a43S4c4Fbf"},
            new String[]{"BFCB0907091213D0000000000", "A19Me00a01c03b04b10e11d12a14d20c21a22b23e24a30d31c32b42c44S4e2FbbbbbccB11Ma00c01e02d04b11d12e13a14d20a22b24c30d31b33a41d43S3a3Fbcccf"},
            new String[]{"BFCB0805060805D0405030709", "A36Me01d02c03b04a10d11b12e13c14c20b21d23e30S2e13c14a2FabbbfB12Mc00e02a03d04a10e11b12c14c21a22b24d42b43S3c3Fabc"},
            new String[]{"AFCB0001040100D1210051208", "A0Mc00d02e03a04b10e11c13b22c24a30b31e40d44S1a13c34a1FbbdeeeeB18Mc00b01a02d03e04d10b12a13c14e22c23a24e33e41c42b44S1e12b13a1Fcddf"},
            new String[]{"BFCB0000060103D1111050611", "A15Ma00c01e02d03b04b12e13a14d20b21a23e24e30c33d34c40S1c12c1FaaafB7Mb00c01e02a04d10b12c14c20e21a23b24d33a40d41c42S3b3Fddddddd"},
            new String[]{"AFCB0703080106D0411011105", "A7Md00a02b03e04d11e13a20c24c30e40S1c12b2FaaaaB11Ma00c01b02d03e04c12a13b14d20b21c23a32S1e13c34e4Fccddddf"},
            new String[]{"BFCB0606060308D0407080901", "A29Me00a01c02b04d10c11e13a14b20d22a23c24a40S3e24b2FafB12Ma00d01b02e04b10a11c12e13d14d23b24c43S3e14e4Faaacddd"},
            new String[]{"AFCB0201050507D1011040204", "A7Mb01c02e03a04e10a12b13d21b22c23d30c31c44S1c13b14d4FaaadddfB14Mc00e01d02b03a04a11d13d20c22a23b24e34d41e43S1c13c14c2Fbbeeee"},
            new String[]{"AFCB1412151217D0000000000", "A18Ma00b01a11b12a22b23a33b34b40a44S1c12d13d34e2FB9Mc02d20e21a22b23c24c30c41S3b24d3Ff"}, // 2 complete sets
            new String[]{"AFCB1515161516D0000000000", "A24Ma00b10e11c20d21e22a23b24a32b42d43SFB27Ma00d03b10e13c20a23d30c31b33e40d41c43SFf"}, // 2 complete columns
            new String[]{"AFCB1415121416D0000000000", "A35Ma00c02b10e11d12c13a14c20d21e22a23b24a32b42d43SFfB39Ma00d03c04b10c12e13c20a23d30c31b33e40d41c43SF"}
    };
    private final int[][] scores = {
            new int[]{12, 2},
            new int[]{2, 0},
            new int[]{2, 2},
            new int[]{2, 10},
            new int[]{2, 0},
            new int[]{0, 2},
            new int[]{2, 0},
            new int[]{0, 2},
            new int[]{0, 2},
            new int[]{0, 2},
            new int[]{20, 2},
            new int[]{2, 14},
            new int[]{11, 24},
    };

    @Test
    public void testGetBonusPoints() {
        for (int i = 0; i < END_GAME_STATES.length; i++) {
            int playerA = Azul.getBonusPoints(END_GAME_STATES[i], 'A');
            int playerB = Azul.getBonusPoints(END_GAME_STATES[i], 'B');
            assertEquals(scores[i][0], playerA, "Azul.getBonusPoints({" + END_GAME_STATES[i][0] + "," + END_GAME_STATES[i][1] + "},\"A\"");
            assertEquals(scores[i][1], playerB, "Azul.getBonusPoints({" + END_GAME_STATES[i][0] + "," + END_GAME_STATES[i][1] + "},\"B\"");
        }
    }
}
