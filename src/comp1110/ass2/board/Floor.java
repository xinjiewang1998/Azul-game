package comp1110.ass2.board;

import comp1110.ass2.Tile;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Discard;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Floor {

    private ArrayDeque<Tile> tiles;
    private Tile firstPlayerTile;

    public Floor() {
        this.tiles = new ArrayDeque<>();
    }


    public ArrayDeque<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(ArrayDeque<Tile> tiles) {
        this.tiles = tiles;
    }

    public Tile getFirstPlayerTile() {
        return firstPlayerTile;
    }

    public void setFirstPlayerTile(Tile firstPlayerTile) {
        this.firstPlayerTile = firstPlayerTile;
    }

    private int getSize() {
        return (firstPlayerTile != null) ? tiles.size() + 1 : tiles.size();
    }

    /**
     * check if floor contains first player tile
     *
     * @return true if has first player tile
     */
    public boolean hasFirstPlayerTile() {
        return firstPlayerTile != null;
    }

    /**
     * place first player tile
     *
     * @param firstPlayerTile the firstPlayerTile
     */
    public void placeFirstPlayerTile(Tile firstPlayerTile, Discard discard) {
        this.firstPlayerTile = firstPlayerTile;
        if (tiles.size() == 7) {
            discard.placeTile(tiles.pop());
        }
    }


    /**
     * Add all other tiles to our tiles.
     *
     * @param otherTiles the other tiles
     */
    public void placeTiles(ArrayDeque<Tile> otherTiles, Discard discard) {
        while (otherTiles.size() != 0 && getSize() <= 7) {
            tiles.add(otherTiles.pop());
        }
        if (otherTiles.size() > 0) {
            discard.placeTiles(otherTiles);
        }
    }

    /**
     * Clear all tiles including first player tile, and send back to discard and centre.
     *
     * @param discard the discard to have normal colored tiles
     * @param centre  the centre to have first player tile
     */
    public void clearTiles(Discard discard, Centre centre) {
        discard.placeTiles(this.tiles);
        if (this.firstPlayerTile != null) {
            centre.setFirstPlayerTile(this.firstPlayerTile);
            this.firstPlayerTile = null;
        }
        this.tiles.clear();
    }

    /**
     * calculate the total penalty of the floor.
     *
     * @return the penalty score
     */
    public Score calculatePenalty() {
        int size = getSize();
        int penalty = switch (size) {
            case 0 -> 0;
            case 1 -> -1;
            case 2 -> -2;
            case 3 -> -4;
            case 4 -> -6;
            case 5 -> -8;
            case 6 -> -11;
            default -> -14;
        };
        return new Score(penalty);
    }

    /**
     * The Floor substring begins with an 'F' and is followed by *up to* 7 characters in
     * alphabetical order. Each character is 'a' to 'f' - where 'f' represents the first player
     * token. There is only one first player token.
     *
     * @param token the floor string
     * @return true if is well formed floor string
     */
    public static boolean isWellFormedFloorString(String token) {
        if (token == null || token.length() > 7) {
            return false;
        }
        int countF = 0;
        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            // only abcedf allowed
            if (c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e' && c != 'f') {
                return false;
            }
            // alphabetical order
            if (i != 0 && (c - token.charAt(i - 1) < 0)) {
                return false;
            }
            if (c == 'f') {
                countF += 1;
            }
        }
        // only one f
        return countF <= 1;
    }

    public static boolean isFloorValid(String floor) {
        if (floor.length() > 7) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of floor state
     */
    public void reconstructFromString(String token) {
        if (!isWellFormedFloorString(token)) {
            return;
        }

        firstPlayerTile = null;
        tiles = new ArrayDeque<>();

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
