package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.common.Common;
import comp1110.ass2.common.Factory;
import java.util.Deque;

public class Player {

    private boolean isFirstPlayer;
    private Player nextPlayer;

    private char id;
    private Board board;

    public Player(char id) {
        this.id = id;
        this.board = new Board();
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Player getNextPlayer() {
        return this.nextPlayer;
    }


    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * draw tiles from factory or centre, and put it on the board
     *
     * @param color        the color picked
     * @param fromFactory  true if from factory, otherwise from centre
     * @param factoryIndex the factory index
     * @param common       the common area
     * @return the tiles picked
     */
    public Deque<Tile> drawTiles(String color, boolean fromFactory, int factoryIndex,
            Common common) {
        if (fromFactory) {
            Factory factory = common.getFactories()[factoryIndex];
            return factory.drawTiles(color, common.getCentre());
        } else {
            return common.getCentre().drawTiles(color);
        }
    }

    /**
     * according to the instruction string given, pass it to board.performInstruction method.
     *
     * @param instruction the move instruction
     */
    public void play(String instruction) {
//        // drafting
//
//        Deque tiles = drawTiles("blue", true, 0);
//
//        // placing
//        this.board.getStorage().putTiles(tiles, "blue", tiles.size(), 4);
//
//        // tiling & scoring
//        this.board.getScore().addScore(this.board.getStorage().tileAndScore());
//        this.board.getScore().addScore(this.board.getFloor().score());
//
//        // check completes a row
//        return this.board.getMosaic().hasCompleteRow();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.id);
        stringBuilder.append(this.board.toString());
        return stringBuilder.toString();
    }
}
