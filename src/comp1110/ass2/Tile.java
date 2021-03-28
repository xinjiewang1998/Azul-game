package comp1110.ass2;

public enum Tile {
    Blue('a', "blue"),
    Green('b', "green"),
    Orange('c', "orange"),
    Purple('d', "purple"),
    Red('e', "red"),
    FirstPlayer('f', "firstPlayer");

    private final char code;
    private final String color;

    Tile(char code, String color) {
        this.code = code;
        this.color = color;
    }

    /**
     * decode color code, and return the corresponding Tile.
     * @param code
     * @return
     */
    public static Tile from(char code) {
        // TODO
        return Blue;
    }

    /**
     * Getter method for color.
     * @return
     */
    public String getColor() {
        return this.color;
    }
}
