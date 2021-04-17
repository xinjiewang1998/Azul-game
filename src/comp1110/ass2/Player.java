package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.common.Common;
import comp1110.ass2.common.Factory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Player {

    boolean isFirstPlayer;
    Player nextPlayer;
    Tile firstPlayer;

    private char id;
    private Board board;
    private Common common;

    public Player(char id) {
        this.id = id;
        this.board = new Board();
    }

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



    public boolean getFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean isFirstPlayer) {
        this.isFirstPlayer = isFirstPlayer;
    }

    // TODO need to check if the color does not exists at all (hasTile()?)
    public Deque<Tile> drawTiles(String color, boolean fromFactory, int factoryIndex) {
        Deque<Tile> tiles = new ArrayDeque<>();

        if (fromFactory) {
            Factory factory = this.common.getFactories()[factoryIndex];
            ArrayList<Tile> factoryTiles = factory.getTiles();
            int count = 0;

            for (int i = 0; i < factoryTiles.size(); i++) {
                Tile tile = factoryTiles.get(i);
                if(tile != null && tile.getColor().equals(color)) {
                    tiles.push(tile);
                } else {
                    this.common.getCentre().addTileToCentre(tile);
                }
                factoryTiles.clear();
            }
        } else {
            ArrayList<Tile> centreTiles = this.common.getCentre().getTiles();
            for (int i = 0; i < centreTiles.size(); i++) {
                Tile tile = centreTiles.get(i);
                if(tile != null && tile.getColor().equals(color)) {
                    tiles.push(tile);
                    centreTiles.set(i, null);
                }
            }
        }
        return tiles;
    }

    public int calculateScore() {
        return this.board.getScore().addScore(this.board.getMosaic().getBonus()).getScore();
    }

    public boolean play() {

        // drafting

        Deque tiles = drawTiles("blue", true, 0);

        // placing
        this.board.getStorage().putTiles(tiles, "blue", tiles.size(), 4);

        // tiling & scoring
        this.board.getScore().addScore(this.board.getStorage().tileAndScore());
        this.board.getScore().subtractScore(this.board.getFloor().score());

        // check completes a row
        return this.board.getMosaic().hasCompleteRow();
    }

    public void decodeStateString( String playerState) {
    }

    public void setNextPlayer(Player nextPlayer){
        this.nextPlayer = nextPlayer;
    }

    public Player getNextPlayer(){
        return this.nextPlayer;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(Tile firstPlayer) {
        this.firstPlayer = firstPlayer;
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

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.id);
        stringBuilder.append(this.board.toString());
        return stringBuilder.toString();
    }
}
