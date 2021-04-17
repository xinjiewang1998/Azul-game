package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Bag {

    private final int COLOR_LIMIT = 20;
    //private ArrayList<Tile> bag = new ArrayList<>();
    private String bag;
    private String discard;

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    ArrayList<Tile> tiles;
    int start;
    int end;

    Discard discardd;
//
//    public Bag(Discard discard) {
//        this.discard = discard;
//
//        tiles = new ArrayList<>();
//        int index = 0;
//        for (int i = 0; i < 20; i++) {
//            tiles.add(Tile.Blue);
//            tiles.add(Tile.Green);
//            tiles.add(Tile.Orange);
//            tiles.add(Tile.Purple);
//            tiles.add(Tile.Red);
//        }
//        Collections.shuffle(tiles);
//
//        start = 0;
//        end = 100;
//
//    }

//    constructor
//    public Bag() {
//    for (int i = 0; i < COLOR_LIMIT; i++) {
//    bag.add(Tile.Blue);
//    bag.add(Tile.Green);
//    bag.add(Tile.Orange);
//    bag.add(Tile.Purple);
//     bag.add(Tile.Red);
//     }
//    Collections.shuffle(bag);
//     }



    public Bag() {
        tiles = new ArrayList<>();
    }

    // old
    public ArrayList drawTiles() {

//        start = 96end = 100
//        if (start + 4) % 100 <
        if(start + 4 > end) {
            int amount = discardd.refillFromDiscard(this.tiles);
            end += amount;
            if(start + 4 > end) {
                // still does not fill enough tiles
                // do something
            }
        }

        // assume there is enough tiles
        // TODO cannot do this assume, fix it above
        Tile first = tiles.get(start);
        Tile second = tiles.get(start + 1);
        Tile third = tiles.get(start + 2);
        Tile forth = tiles.get(start + 3);

        start = (start + 4) % 100;
        return new ArrayList<>(Arrays.asList(
                first, second, third, forth
        ));
    }

    /**
     * Each factory is filled with exactly four tiles drawn randomly from the bag
     * <p>
     * take four tiles from bag randomly.
     *
     * @return a Array contains four tiles.
     */
    public char drawTile(String[] gameState) {
        ArrayList<String> B = new ArrayList<>();
        int indexF = gameState[0].indexOf('F', 0);
        int indexC = gameState[0].indexOf('C', indexF + 1);
        int indexB = gameState[0].indexOf('B', indexC + 1);
        int indexD = gameState[0].indexOf('D', indexB + 1);
        this.bag = gameState[0].substring(indexB + 1, indexD);
        this.discard = gameState[0].substring(indexD + 1);
        for (int i = 0; i < this.bag.length() - 1; i = i + 2) {
            B.add(this.bag.substring(i, i + 2));
        }
        int num = 0;
        for (String s : B) {
            if (s.equals("00")) {
                num++;
            }
        }
        //String b = gameState[0].substring(indexD + 1);
        ArrayList<String> D = new ArrayList<>();
        if (num == B.size()) {
            for (int i = 0; i < this.discard.length(); i = i + 2) {
                D.add(this.discard.substring(i, i + 2));
            }
            //refill the Bag from Discard


            B = refill(B,D);
            for (int n =0;n<D.size();n=n+1){
                D.set(n,"00");}
            int numB=0;
            for (String s : B) {
                if (s.equals("00")) {
                    numB++;
                }
            }
            if (numB==B.size()) {
                return 'Z';
            }

        }
        Random r = new Random();
        char result = ' ';
        char[] tile = {'a', 'b', 'c', 'd', 'e'};
        ArrayList<Character> Bag = new ArrayList<>();
        for(int i = 0;i<B.size(); i++){
            for(int j =0;j<Integer.parseInt(B.get(i));j++){
                Bag.add(tile[i]);
            }
        }
            int position = r.nextInt(Bag.size());
            result = Bag.get(position);

        //while (true) {
            //int position = r.nextInt(5);
            //if (!B.get(position).equals("00")) {
                //String to int
                //int p = Integer.parseInt(B.get(position));
                //p = p - 1;
                //B.set(position, "" + p);
               // result = tile[position];
                //break;
           // }
        //}

        return result;
    }

    /**
     * If at any point you run out of tiles in the bag,
     * you should move all the tiles from the discard pile into the bag
     * and then continue filling the factories.
     * <p>
     * refill from discard.
     *
     * @param B Statement of Bag
     * @param D Statement of Discard
     * @return Statement of Bag after being refilled
     */
    public ArrayList<String> refill(ArrayList<String> B, ArrayList<String> D) {
        for (int n =0;n<B.size();n=n+1){
            B.set(n, D.get(n));
        }

        return B;
    }

    /////////////////////////////NEW////////////////////////////

    /**
     * 3. [bag] The bag substring starts with a 'B'
     * and is followed by 5 2-character substrings
     * 1st substring represents the number of 'a' tiles, from 0 - 20.
     * 2nd substring represents the number of 'b' tiles, from 0 - 20.
     * 3rd substring represents the number of 'c' tiles, from 0 - 20.
     * 4th substring represents the number of 'd' tiles, from 0 - 20.
     * 5th substring represents the number of 'e' tiles, from 0 - 20.
     * For example: "B0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     */
    public static boolean isWellFormedBagString(ArrayList<Character> chars) {
        if (chars.size() != 10) {
            return false;
        } else {
            for(int i = 0; i < 10; i++) {
                String s = chars.get(i).toString() + chars.get(++i).toString();
                try {
                    int num = Integer.parseInt(s);
                    if (num < 0 || num > 20) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }

            }
        }
        return true;
    }

    public void fillFrom(String bagState) {
        for(int i = 0; i < bagState.length(); i+=2) {
            String substring = bagState.substring(i, i+2);
            int amount = Integer.parseInt(substring);
            for(int j = 0; j < amount; j++) {
                this.tiles.add(Tile.from((char) ('a' + i/2)));
            }
//            this.tiles.add(Tile.from(centreState.charAt(i)));
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        HashMap<Character, Integer> count = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            count.put((char) ('a' + i), 0);
        }

        for (Tile tile : tiles) {
            char code = tile.getColorCode();
            count.merge(code, 1, Integer::sum);
        }

        for (int i = 0; i < 5; i++) {
            int num = count.get((char) ('a' + i));
            if (num < 10) {
                stringBuilder.append(0);
            }
            stringBuilder.append(num);
        }

        return stringBuilder.toString();
    }
}
