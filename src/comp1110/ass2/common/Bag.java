package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bag {

    private final int COLOR_LIMIT = 20;

    private ArrayList<Tile> tiles;

    public Bag() {
        tiles = new ArrayList<>();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * fill bag with 20 colored tiles each
     */
    public void init() {
        tiles = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < COLOR_LIMIT; i++) {
            tiles.add(Tile.Blue);
            tiles.add(Tile.Green);
            tiles.add(Tile.Orange);
            tiles.add(Tile.Purple);
            tiles.add(Tile.Red);
        }
        Collections.shuffle(tiles);
    }

    /**
     * Each factory is filled with exactly four tiles drawn randomly from the bag
     * <p>
     * take four tiles from bag randomly.
     *
     * @return a Array contains four tiles.
     */
    public ArrayList<Tile> drawTiles(Discard discard) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Tile tile = this.drawTile(discard);
            if (tile == null) {
                break;
            }
            tiles.add(tile);
        }
        return tiles;
    }


    /**
     * draw 1 tile
     *
     * @param discard the discard may used for refill
     * @return the tile
     */
    public Tile drawTile(Discard discard) {
        if (tiles.size() == 0) {
            discard.refillTiles(this);
            if (this.tiles.size() == 0) {
                return null;
            }
        }
        Collections.shuffle(tiles);
        return tiles.remove(tiles.size() - 1);

    }

    /**
     * place other tiles into the bag
     *
     * @param otherTiles the tiles to be added
     */
    public void placeTiles(ArrayList<Tile> otherTiles) {
        tiles.addAll(otherTiles);
    }


    /**
     * The bag substring starts with a 'B' and is followed by 5 2-character substrings 1st substring
     * represents the number of 'a' tiles, from 0 - 20. 2nd substring represents the number of 'b'
     * tiles, from 0 - 20. 3rd substring represents the number of 'c' tiles, from 0 - 20. 4th
     * substring represents the number of 'd' tiles, from 0 - 20. 5th substring represents the
     * number of 'e' tiles, from 0 - 20. For example: "B0005201020" The bag contains zero 'a' tiles,
     * five 'b' tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     *
     * @param token the bag string
     * @return true if is well formed bag string
     */
    public static boolean isWellFormedBagString(String token) {
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
     * @param token the string representation of bag state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedBagString(token)) {
            return;
        }

        tiles = new ArrayList<>();

        for (int i = 0; i < token.length(); i += 2) {
            String substring = token.substring(i, i + 2);
            int amount = Integer.parseInt(substring);
            for (int j = 0; j < amount; j++) {
                this.tiles.add(Tile.from((char) ('a' + i / 2)));
            }
            Collections.shuffle(this.tiles);
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
