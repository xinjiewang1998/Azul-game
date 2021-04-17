package comp1110.ass2;

import comp1110.ass2.board.Board;
import comp1110.ass2.board.Floor;
import comp1110.ass2.board.Mosaic;
import comp1110.ass2.board.Score;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Common;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Player {

    boolean isFirstPlayer;
    Player nextPlayer;
    Floor floor;


    String playerState;
    Factory[] factories;
    Centre centre;

    //new
    Board board;
    Common common;

    Tile firstPlayer;


    public Player(Factory[] factories, Centre centre, Discard discard) {

        this.mosaic = new Mosaic();
        this.floor = new Floor();
        this.storage = new Storage(this.mosaic, this.floor, discard);

        this.factories = factories;
        this.centre = centre;

        this.score = new Score(0);

    }

    public Player() {
        this.mosaic = new Mosaic();
        this.floor = new Floor();
        this.storage = new Storage();
        this.score = new Score(0);
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
            Factory factory = factories[factoryIndex];
            ArrayList<Tile> factoryTiles = factory.getTiles();
            int count = 0;

            for (int i = 0; i < factoryTiles.size(); i++) {
                Tile tile = factoryTiles.get(i);
                if(tile != null && tile.getColor().equals(color)) {
                    tiles.push(tile);
                } else {
                    this.centre.addTileToCentre(tile);
                }
                factoryTiles.clear();
            }
        } else {
            ArrayList<Tile> centreTiles = centre.getTiles();
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
        return this.score.addScore(this.mosaic.getBonus()).getScore();
    }

    public boolean play() {

        // drafting

        Deque tiles = drawTiles("blue", true, 0);

        // placing
        this.storage.putTiles(tiles, "blue", tiles.size(), 4);

        // tiling & scoring
        this.score.addScore(this.storage.tileAndScore());
        this.score.subtractScore(this.floor.score());

        // check completes a row
        return this.mosaic.hasCompleteRow();
    }

    public void decodeStateString( String playerState) {
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
