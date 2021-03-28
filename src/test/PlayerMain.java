package test;

import test.Player;

public class PlayerMain {

    public static void main(String[] args) {
        Player p1 = new Player(10);

        p1.addScore();
        p1.addScore();
        p1.addScore();

        System.out.println(p1.getScore());
    }
}
