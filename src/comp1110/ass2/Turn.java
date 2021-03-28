package comp1110.ass2;

/**
 * This Turn class controls the order of execution(player.play())
 */
public class Turn {

    Player[] players;
    Player firstPlayer;

    public Turn(Player[] players) {
        this.players = players;
        this.firstPlayer = players[0];
    }

    public void play() {
        for(int i = 0; i < players.length; i++) {
            firstPlayer.play();
            firstPlayer = firstPlayer.nextPlayer;
        }
    }

    public void setFirstPlayer(Player firstPlayer){
        this.firstPlayer = firstPlayer;
    }
}
