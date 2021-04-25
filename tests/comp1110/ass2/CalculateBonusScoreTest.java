package comp1110.ass2;

import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateBonusScoreTest {
    @Test
    public void fullMosaicTest(){
        Mosaic mosaic = new Mosaic();
        String token = "d00c01b02a03e04e10d11c12b13a14d20c21b22a23e24d30c31b32a33e34d40c41b42a43e44";
        mosaic.reconstructFromString(token);
        Score score =mosaic.calculateBonusScore();
        assertEquals(95,score.getScore());
    }

    @Test
    public void oneRowCompletedTest(){
        Mosaic mosaic = new Mosaic();
        String token ="d00c01b02a03e04";
        mosaic.reconstructFromString(token);
        Score score =mosaic.calculateBonusScore();
        assertEquals(2,score.getScore());
    }

    @Test
    public void oneColumnCompletedTest(){
        Mosaic mosaic = new Mosaic();
        String token ="d00c10b20a30e40";
        mosaic.reconstructFromString(token);
        Score score =mosaic.calculateBonusScore();
        assertEquals(7,score.getScore());
    }

    @Test
    public void fiveSameColorTile(){
        Mosaic mosaic = new Mosaic();
        String token ="d00d11d22d33d44";
        mosaic.reconstructFromString(token);
        Score score =mosaic.calculateBonusScore();
        assertEquals(10,score.getScore());
    }
}
