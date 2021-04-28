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

    public static int tileNum(String string,char a){
        int num = 0;
        for (int i =0 ;i<string.length();i++){
            if (string.charAt(i)==a){
                num++;
            }
        }
        return num;
    }

    public static int tileNum(String string,int i){
        int num = Integer.parseInt(string.substring(i,i+2));
        return num;
    }

    public static int tileNum(String string,char a,int i){
        int num = 0;
        for (int j =0 ;j<string.length();j++){
            if (string.charAt(j)==a){
                num= num + Integer.parseInt(string.substring(i+1,i+2));
            }
        }
        return num;
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
