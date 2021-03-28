package comp1110.ass2;

public class Turn {

    private Player[] players;
    private Player firstPlayer;
    public Turn (Player[] players, Player firstPlayer){
        this.firstPlayer = firstPlayer;
        this.players = players;
    }

    /**
     * cyclic go through all players starting from first player and
     * call play() method on each player.
     */
    public void play(){
        Player player = firstPlayer;
        for (int i =0; i< players.length ; i++){
            player.play("some instruction");
            player = player.getNextPlayer();
        }
    }

    /**
     * check this turn is the last turn.
     * @return true if this is the last turn.
     */
    public boolean hasFinished(){
        //fixme
        return true;
    }

    /**
     * change the first player to a new player.
     * @param first the new first player.
     */
    public void changeFirstPlayer(Player first){
        this.firstPlayer = first;
    }
}
