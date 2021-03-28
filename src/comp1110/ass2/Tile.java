package comp1110.ass2;

public enum Tile {
    Blue('a', "blue"),
    Green( 'b', "green"),
    Orange( 'c', "orange"),
    Purple('d', "purple"),
    Red('e', "red"),
    FirstPlayer('f', "firstPlayer");

    private final char code;
    private final String color;

    Tile (char code, String color) {
        this.code = code;
        this.color = color;
    }

    // decode code, return the corresponding Tile
    public static Tile from(char code) {
        for (Tile t : Tile.values()) {
            if (code == t.code) {
                return t;
            }
        }
        return FirstPlayer;
    }

    public String getColor() {
        return this.color;
    }
}
