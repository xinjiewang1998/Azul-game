package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;

public class Board {

    private Score score;
    private Storage storage;
    private Mosaic mosaic;
    private Floor floor;

    public Board()  {
    }

    /**
     * Initialize the board with correct prams.
     */
    public void init() {
        this.score = new Score();
        this.storage = new Storage();
        this.mosaic = new Mosaic();
        this.floor = new Floor();
    }

    /**
     * 0. decode instruction
     * 1. add tiles to storage
     * 2. move complete row to mosaic
     * 3. bring back score
     * @param tiles
     * @param instruction
     * @return
     */
    public int performInstruction(ArrayList<Tile> tiles, String instruction) {
        // FIXME
        return 0;
    }

    /**
     * Cleanup the board after the game.
     */
    public void cleanUp() {
        // FIXME
    }

    public String toSting() {
        // FIXME
        return "";
    };
}
