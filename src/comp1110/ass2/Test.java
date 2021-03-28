package comp1110.ass2;

import comp1110.ass2.common.Bag;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Discard;
import comp1110.ass2.common.Factory;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
//        Tile myTile = Tile.from('c');
//        final Map<Integer, Integer> GAME_SETUP = Map.of(
//                2, 5,
//                3, 7,
//                4, 9
//        );
//        final int NUM_PLAYERS = 3;
//
//        Discard discard = new Discard();
//        Bag bag = new Bag(discard);
//        Factory[] factories = new Factory[GAME_SETUP.get(NUM_PLAYERS)];
//        for (int i = 0; i < factories.length; i++) {
//            factories[i] = new Factory(bag);
//            factories[i].drawTiles();
//        }
//        Centre centre = new Centre();

        // fill bag
        // fill factory

//        Player myPlayer = new Player(factories, centre, discard);
//        Player hisPlayer = new Player(factories, centre, discard);
//        Player herPlayer = new Player(factories, centre, discard);
//
//        myPlayer.setNextPlayer(hisPlayer);
//        hisPlayer.setNextPlayer(herPlayer);
//        herPlayer.setNextPlayer(myPlayer);
//
//        myPlayer.setFirstPlayer(true);
//
//        while (myPlayer.play() &&
//                hisPlayer.play() &&
//                herPlayer.play()) {
//
//            for (int i = 0; i < factories.length; i++) {
//                factories[i] = new Factory(bag);
//                factories[i].drawTiles();
//            }
//        }
//        // bonus
//        myPlayer.calculateScore();
//        hisPlayer.calculateScore();
//        herPlayer.calculateScore();

//        Player[] players = new Player[10];
//        for (int i =0; i < players.length; i++) {
//            players[i] = new Player();
//        }
//        Turn myTurn = new Turn(players);
//        myTurn.play();
//        myTurn.setFirstPlayer(players[5]);
//        myTurn.play();
//        myTurn.setFirstPlayer(players[3]);
//        myTurn.play();
//        myTurn.setFirstPlayer(players[2]);
//        myTurn.play();
//        myTurn.setFirstPlayer(players[8]);
//        myTurn.play();
//        myTurn.setFirstPlayer(players[0]);
//        myTurn.play();

        String a = "01234567";
        System.out.println(a.substring(0, 3));
        Azul.isPlayerStateWellFormed(
                "A07Me01a11d20b30b41S0a11b22c13c44d1FeeB08Md03b13e23c32b41S0b11c12a33d24e4Fab");
    }
}
