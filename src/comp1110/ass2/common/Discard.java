package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class Discard {

    ArrayList<Tile> tiles;

    public Discard() {
        tiles = new ArrayList<>();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Empty any row that no longer has a tile in the rightmost space and place all remaining tiles
     * in the discard pile. Any tiles that remain in incomplete rows on your board remain for the
     * next round.
     *
     * @param leftTiles tiles left in storage.
     */
    public void placeTiles(ArrayDeque<Tile> leftTiles) {
        this.tiles.addAll(leftTiles);
    }

    /**
     * Add extra tile to discard pile.
     *
     * @param extraTile extraTile
     */
    public void placeTile(Tile extraTile) {
        this.tiles.add(extraTile);
    }

    /**
     * Send all tiles from discard to bag
     *
     * @param bag the bag get refilled
     */
    public void refillTiles(Bag bag) {
        bag.placeTiles(tiles);
        tiles.clear();
    }

    /**
     * The discard substring starts with a 'D' and is followed by 5 2-character substrings defined
     * the same as the bag substring. For example: "D0005201020" The bag contains zero 'a' tiles,
     * five 'b' tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     *
     * @param token the discard string
     * @return true if is well formed discard string
     */
    public static boolean isWellFormedDiscardString(String token) {
        if (token == null || token.length() != 10) {
            return false;
        } else {
            for (int i = 0; i < 10; i++) {
                String s = token.charAt(i) + String.valueOf(token.charAt(++i));
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

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of score state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedDiscardString(token)) {
            return;
        }

        tiles = new ArrayList<>();

        for (int i = 0; i < token.length(); i += 2) {
            String substring = token.substring(i, i + 2);
            int amount = Integer.parseInt(substring);
            for (int j = 0; j < amount; j++) {
                this.tiles.add(Tile.from((char) ('a' + i / 2)));
            }
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

        // extra '0'
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
