package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Mosaic {

    final int MOSAIC_LENGTH = 5;
    final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(
            Arrays.asList("blue", "green", "orange", "purple", "red"));

    Tile[][] matrix;
    int[] colorCount;

    public Mosaic() {
        matrix = new Tile[MOSAIC_LENGTH][MOSAIC_LENGTH];
        colorCount = new int[] {0, 0, 0, 0, 0};
    }

    public boolean hasColor(String color, int row) {
        int column = (row + DEFAULT_COLORS.indexOf(color)) % MOSAIC_LENGTH;
        return (matrix[row][column] != null);
    }

    // no safety guarantee, please call hasColor first if required.
    public int putTile(Tile t, int row) {
        int index = DEFAULT_COLORS.indexOf(t.getColor());
        int column = (row + index) % MOSAIC_LENGTH;

        matrix[row][column] = t;
        colorCount[index]++;

        int count = 1;
        boolean hasHorizontallyLinkedTiles = false;
        boolean hasVerticallyLinkedTiles = false;
        // up
        for (int i = row - 1; i >= 0; i--) {
            if(matrix[i][column] != null) {
                hasVerticallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // down
        for (int i = row + 1; i < MOSAIC_LENGTH; i++) {
            if(matrix[i][column] != null) {
                hasVerticallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // left
        for (int i = column - 1; i >= 0; i--) {
            if(matrix[row][i] != null) {
                hasHorizontallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        // right
        for (int i = column + 1; i < MOSAIC_LENGTH; i++) {
            if(matrix[row][i] != null) {
                hasHorizontallyLinkedTiles = true;
                count++;
            } else {
                break;
            }
        }
        return (hasHorizontallyLinkedTiles && hasVerticallyLinkedTiles) ? count + 1 : count;
    }

    public boolean hasCompleteRow() {
        for (int i = 0; i < MOSAIC_LENGTH; i++) {
            boolean allFilled = true;
            for(int j = 0; j < MOSAIC_LENGTH; j++) {
                if(matrix[i][j]==null) {
                    allFilled = false;
                    break;
                }
            }
            if(allFilled) {
                return true;
            }
        }
        return false;
    }

    public int getBonus() {
        int total = 0;
        for (int i = 0; i < MOSAIC_LENGTH; i++) {
            boolean rowFilled = true;
            boolean colFilled = true;
            for (int j = 0; j < MOSAIC_LENGTH; j++) {
                if (matrix[i][j] == null) {
                    rowFilled = false;
                    break;
                }
                if (matrix[j][i] == null) {
                    colFilled = false;
                    break;
                }
            }
            if (rowFilled) {
                total += 2;
            }
            if (colFilled) {
                total += 7;
            }
        }
        for (int i = 0; i < MOSAIC_LENGTH; i++) {
            if (colorCount[i] == MOSAIC_LENGTH) {
                total += 10;
            }
        }
        return total;
    }
}





class Spot {
    boolean isOccupied;
    String defaultColor;
    Tile tile;

    public Spot(boolean isOccupied, String defaultColor) {
        this.isOccupied = isOccupied;
        this.defaultColor = defaultColor;
    }

    public boolean placeTile(Tile t) {
        if (!this.isOccupied && this.defaultColor.equals(t.getColor())) {
            this.isOccupied = true;
            this.tile = t;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.defaultColor;
    }
}
