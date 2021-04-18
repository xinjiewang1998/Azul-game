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
     *
     * @param code the color code 'a' to 'f'
     * @return the corresponding tile
     */
    public static Tile from(char code) {
        for (Tile t : Tile.values()) {
            if (code == t.code) {
                return t;
            }
        }
        return FirstPlayer;
    }

    /**
     * Getter method for color.
     *
     * @return return the color string
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Getter method for code.
     *
     * @return return the color code character
     */
    public char getColorCode() {
        return this.code;
    }
}
