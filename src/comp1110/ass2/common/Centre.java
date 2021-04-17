package comp1110.ass2.common;

import comp1110.ass2.Tile;
import java.util.ArrayList;

public class Centre {
    private final int PLAYER_LIMIT = 1;

    private ArrayList<Tile> tiles = new ArrayList<>();
    private Tile firstPlayerTile;

    //constructor
    public Centre() {

    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
    public void setTiles(ArrayList<Tile> centre) {
        this.tiles = centre;
    }

    public void addTileToCentre(Tile tile) {
        this.tiles.add(tile);
    }


    public void addFirstPlayerTile(Tile firstPlayerTile) {
        this.firstPlayerTile = firstPlayerTile;
    }

    /**
     * move the remaining tiles on this factory to the centre.
     *
     * @param otherTiles a array contains remaining tiles on this factory
     */
    public void addTilesToCentre(ArrayList<Tile> otherTiles) {
        //FIXME
    }


    /**
     * Pick all tiles of the same colour from the centre.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the same color player chose.
     */
    public ArrayList<Tile> takeTilesFromCentre(String color) {
        //FIXME
        return new ArrayList<>();
    }

    /////////////////////////////NEW////////////////////////////

    /**
     * 2. [centre] The centre substring starts with a 'C'
     * This is followed by *up to* 15 characters.
     * Each character is 'a' to 'e', alphabetically - representing a tile
     * in the centre.
     * The centre string is sorted alphabetically.
     * For example: "Caaabcdde" The Centre contains three 'a' tiles, one 'b'
     * tile, one 'c' tile, two 'd' tile and one 'e' tile.
     */
    public static boolean isWellFormedCentreString(ArrayList<Character> chars) {
        if (chars.size() > 15) {
            return false;
        }
        int fCount = 0;
        for (int i = 0; i < chars.size(); i++) {
            char c = chars.get(i);
            char prevChar, currChar;
            if (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e') {
                if (i != 0 && (c - chars.get(i-1) < 0)) {
                    return false;
                }
            } else if (c == 'f') {
                fCount++;
                if(fCount > 1) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void fillFrom(String centreState) {
        for(int i = 0; i < centreState.length(); i++) {
            if (centreState.charAt(i) == 'f') {
                this.firstPlayerTile = Tile.from('f');
            } else {
                tiles.add(Tile.from(centreState.charAt(i)));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < tiles.size(); i++) {
            stringBuilder.append(tiles.get(i).getColorCode());
        }
        if(firstPlayerTile != null) {
            stringBuilder.append('f');
        }
        return stringBuilder.toString();
    }

}
