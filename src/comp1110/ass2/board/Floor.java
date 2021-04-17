package comp1110.ass2.board;

import comp1110.ass2.Tile;
import comp1110.ass2.common.Centre;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class Floor {

    private ArrayList<Tile> tiles;

    public Floor() {
        this.tiles = new ArrayList<>();
    }

    /**
     * Add all other tiles to our tiles.
     * @param otherTiles the other tiles
     */
    public void addTiles(ArrayDeque<Tile> otherTiles) {
        // FIXME
    }

    public void putTiles(Deque tiles) {
        this.tiles.addAll(tiles);
        tiles.clear();
    }

    public int score() {
        return switch (tiles.size()) {
            case 0 -> 0;
            case 1 -> -1;
            case 2 -> -2;
            case 3 -> -4;
            case 4 -> -6;
            case 5 -> -8;
            case 6 -> -11;
            default -> -14;
        };
    }

    /**
     * Clear all tiles and return them.
     * @return the tiles
     */
    public ArrayList<Tile> clearTiles() {
        // FIXME
        return tiles;
    }

    /**
     * calculate the total penalty of the floor.
     * @return the penalty score
     */
    public Score calculatePenalty() {
        // FIXME
        return new Score(0);
    }

    /**
     * check if floor contains first player tile
     * @return true if has first player tile
     */
    public boolean hasFirstPlayerTile() {
        return false;
    }

    /**
     * return first player tile to centre
     * @param centre the shared centre
     */
    public void returnFirstPlayerTile(Centre centre) {

    }



    /**
     * 5. [floor] The Floor substring begins with an 'F'
     * and is followed by *up to* 7 characters in alphabetical order.
     * Each character is 'a' to 'f' - where 'f' represents the first player token.
     * There is only one first player token.
     */
    public static boolean isFloorWellFormedString(ArrayList<Character> floor) {
        if (floor.size() > 7) {
            return false;
        }
        int countF = 0;
        for (int i = 0; i < floor.size(); i++) {
            char c = floor.get(i);
            if (c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e' && c != 'f') {
                return false;
            }
            if(i != 0 && (c - floor.get(i-1) < 0)) {
                return false;
            }
            if(c == 'f') {
                countF += 1;
            }
        }
        if(countF > 1) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
