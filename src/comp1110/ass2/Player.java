package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.common.Common;

import java.util.ArrayList;

public class Player {
    Board board;
    Common common;

    Tile firstPlayer;
    boolean isFirstPlayer;
    Player nextPlayer;

    String playerState;

    public Player(Common common, boolean isFirstPlayer){
        this.board = new Board();
        this.common = common;
        this.isFirstPlayer = isFirstPlayer;
        if (isFirstPlayer){
            //acquire tile from centre
            ArrayList<Tile> tiles = common.getCentre().takeTilesFromCentre("firstPlayer");
            this.firstPlayer = tiles.get(0);
        }
    }

    public void setPlayerState(String playerState){
        this.playerState = playerState;
    }

    public void setNextPlayer(Player nextPlayer){
        this.nextPlayer = nextPlayer;
    }

    public Player getNextPlayer(){
        return this.nextPlayer;
    }

    /**
     * decode playerStateString.
     */
    public void decodePlayerState(){
        //fixme
    }

    /**
     * encode player state to playerState string
     * by concateenate mutiple toString methods.
     * @return playerState
     */
    public String encodePlayerState(){
        //fixme
        return "";
    }

    /**
     * according to the instruction string given,
     * pass it to board.performInstruction method.
     * @param instruction
     */
    public void play(String instruction){
        //fixme
    }

    /**
     * fetch the score from board.
     * @return the score
     */
    public int getScore(){
        //fixme
        return 0;
    }
}
