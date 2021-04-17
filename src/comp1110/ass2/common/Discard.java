package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;

public class Discard {

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    ArrayList<Tile> tiles;

    public Discard() {
        tiles = new ArrayList<>();
    }

    public int refillFromDiscard(ArrayList<Tile> to) {
        int total = tiles.size();
        to.addAll(tiles);
        tiles.clear();
        return total;
    }

    public void putTiles(Deque<Tile> tiles) {
        tiles.addAll(tiles);
        tiles.clear();
    }

    public void clear() {
        this.tiles.clear();
    }
    /**
     * Empty any row that no longer has a tile in the rightmost space and place all remaining tiles in the discard pile.
     * Any tiles that remain in incomplete rows on your board remain for the next round.
     *
     * @param leftTiles tiles left in storage.
     */
    public void addTilesToDiscard(ArrayList<Tile> leftTiles) {
        this.tiles.addAll(leftTiles);
    }


    /**
     * 4. [discard] The discard substring starts with a 'D'
     * and is followed by 5 2-character substrings defined the same as the
     * bag substring.
     * For example: "D0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     */
    public static boolean isWellFormedDiscardString(ArrayList<Character> chars) {
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

    public void fillFrom(String discardState) {
        for(int i = 0; i < discardState.length(); i+=2) {
            String substring = discardState.substring(i, i+2);
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
