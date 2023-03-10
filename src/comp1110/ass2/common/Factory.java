package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 */
public class Factory {

    private int id;
    private ArrayList<Tile> tiles;

    public Factory() {
        tiles = new ArrayList<>();
    }

    public Factory(int id) {
        this.id = id;
        tiles = new ArrayList<>();
    }

    /**
     * @Author: Jiaan Guo
     * The factories substring begins with an 'F' and is followed by a collection of *up to* 5
     * 5-character factory strings representing each factory. Each factory string is defined in the
     * following way: 1st character is a sequential digit '0' to '4' - representing the factory
     * number. 2nd - 5th characters are 'a' to 'e', alphabetically - representing the tiles. A
     * factory may have between 0 and 4 tiles. If a factory has 0 tiles, it does not appear in the
     * factories string. Factory strings are ordered by factory number. For example: given the
     * string "F1aabc2abbb4ddee": Factory 1 has tiles 'aabc', Factory 2 has tiles 'abbb', Factory 4
     * has tiles 'ddee', and Factories 0 and 4 are empty.
     *
     * @param token the factory string
     * @return true if is well formed factory string
     */
    public static boolean isWellFormedFactoryString(String token) {
        if (token == null || token.length() > 5) {
            return false;
        }

        if (token.length() == 0) {
            return true;
        }
        // only 01234 possible
        char index = token.charAt(0);
        if (index != '0' && index != '1' && index != '2' && index != '3' && index != '4') {
            return false;
        }

        // only abcde possible
        for (int i = 1; i < token.length(); i++) {
            char color = token.charAt(i);
            if (color != 'a' && color != 'b' && color != 'c' && color != 'd' && color != 'e') {
                return false;
            }
        }
        // check unique and strictly increase
        for (int i = 1; i < token.length() - 1; i++) {
            int curr = token.charAt(i);
            int next = token.charAt(i + 1);
            if (next < curr) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @Author: Xinjie Wang, Jiaan Guo, Xiang Lu
     * Sort the tiles by color code
     */
    private void sort() {
        ArrayList<Tile> newList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (Tile tile : tiles) {
                if (tile.getColorCode() == 'a' + i) {
                    newList.add(tile);
                }
            }
        }
        tiles.clear();
        this.tiles = newList;
    }

    /**
     * @Author: Xinjie Wang
     * Each factory is filled with exactly four tiles drawn randomly from the bag.
     *
     * @param bag     the bag to draw
     * @param discard the discard if bag is empty
     */
    public void refillTiles(Bag bag, Discard discard) {
        tiles.addAll(bag.drawTiles(discard));
        sort();
    }

    /**
     * @Author: Xinjie Wang, Jiaan Guo, Xiang Lu
     * Pick all tiles of the same colour from one factory.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the color in this factory.
     */
    public ArrayDeque<Tile> drawTiles(String color, Centre centre) {
        ArrayDeque<Tile> returnTiles = new ArrayDeque<>();
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile != null && tile.getColor().equals(color)) {
                returnTiles.push(tile);
            } else {
                centre.placeTile(tile);
            }
        }
        tiles.clear();
        return returnTiles;
    }

    /**
     * @Author: Xinjie Wang, Xiang Lu
     * Count the number of tiles with specific color
     *
     * @param code the color code
     * @return the number
     */
    public int countTile(char code) {
        int count = 0;
        for (Tile tile : this.getTiles()) {
            if (tile.getColorCode() == code) {
                count++;
            }
        }
        return count;
    }

    /**
     * @Author: Xinjie Wang
     * Check tiles contains a specific color tile
     *
     * @param code the color code
     * @return true if tiles contains a specific color tile
     */
    public boolean hasTile(char code) {
        for (int i = 0; i < this.getTiles().size(); i++) {
            if (this.getTiles().get(i).getColorCode() == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Author: Jiaan Guo
     * Reconstruct internal state from string
     *
     * @param token the string representation of floor state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedFactoryString(token)) {
            return;
        }

        tiles = new ArrayList<>();

        this.id = token.charAt(0) - 48;
        for (int i = 1; i < token.length(); i++) {
            this.tiles.add(Tile.from(token.charAt(i)));
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (tiles.size() != 0) {
            stringBuilder.append(id);
            for (Tile tile : tiles) {
                stringBuilder.append(tile.getColorCode());
            }
        }
        return stringBuilder.toString();
    }
}
