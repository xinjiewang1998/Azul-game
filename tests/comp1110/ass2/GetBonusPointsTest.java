package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class GetBonusPointsTest {
    @Test
    public void testGetBonusPoints() {
        String[] state = ExampleGames.VALID_STATES[ExampleGames.VALID_STATES.length - 1];
        int player0 = Azul.getBonusPoints(state, 'A');
        assertEquals(21, player0, "Azul.getBonusPoints({" + state[0] + "," + state[1] + "},\"A\"");
        int player1 = Azul.getBonusPoints(state, 'B');
        assertEquals(26, player1, "Azul.getBonusPoints({" + state[0] + "," + state[1] + "},\"B\"");
    }
}
