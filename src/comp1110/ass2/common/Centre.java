package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Centre {


    private ArrayList<Tile> tiles;
    private Tile firstPlayerTile;

    public Centre() {
        tiles = new ArrayList<>();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayList<Tile> centre) {
        this.tiles = centre;
    }

    public Tile getFirstPlayerTile() {
        return firstPlayerTile;
    }

    public void setFirstPlayerTile(Tile firstPlayerTile) {
        this.firstPlayerTile = firstPlayerTile;
    }

    /**
     * place one tile to the centre.
     *
     * @param tile the tile to be placed
     */
    public void placeTile(Tile tile) {
        this.tiles.add(tile);
    }

    /**
     * move the remaining tiles on this factory to the centre.
     *
     * @param otherTiles a array contains remaining tiles on this factory
     */
    public void placeTiles(ArrayList<Tile> otherTiles) {
        tiles.addAll(otherTiles);
    }


    /**
     * Pick all tiles of the same colour from the centre.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the same color player chose.
     */
    public ArrayDeque<Tile> drawTiles(String color) {
        ArrayDeque<Tile> returnTiles = new ArrayDeque<>();
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (tile != null && tile.getColor().equals(color)) {
                returnTiles.push(tile);
                tiles.set(i, null);
            }
        }
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile != null) {
                temp.add(tile);
            }
        }
        tiles = temp;
        return returnTiles;
    }

    /**
     * The centre substring starts with a 'C' This is followed by *up to* 15 characters. Each
     * character is 'a' to 'e', alphabetically - representing a tile in the centre. The centre
     * string is sorted alphabetically. For example: "Caaabcdde" The Centre contains three 'a'
     * tiles, one 'b' tile, one 'c' tile, two 'd' tile and one 'e' tile.
     *
     * @param token the centre string
     * @return true if is well formed centre string
     */
    public static boolean isWellFormedCentreString(String token) {
        if (token == null || token.length() > 15) {
            return false;
        }
        int fCount = 0;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            // alphabetical order
            if (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e') {
                if (i != 0 && (c - token.charAt(i - 1) < 0)) {
                    return false;
                }
            } else if (c == 'f') {
                fCount++;
                if (fCount > 1) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of floor state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedCentreString(token)) {
            return;
        }

        firstPlayerTile = null;
        tiles = new ArrayList<>();

        for (int i = 0; i < token.length(); i++) {
            if (token.charAt(i) == 'f') {
                firstPlayerTile = Tile.from('f');
            } else {
                tiles.add(Tile.from(token.charAt(i)));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tile tile : tiles) {
            stringBuilder.append(tile.getColorCode());
        }
        if (firstPlayerTile != null) {
            stringBuilder.append('f');
        }
        return stringBuilder.toString();
    }

}
