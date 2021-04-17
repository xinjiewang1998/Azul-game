package test;

public class Player {

    // fields
    int score;
    Tile[] tiles;
    Floor floor;

    // constructor
    public Player(int score) {
        this.score = score;
        this.floor = new Floor();
        tiles = new Tile[10];
    }

    // methods
    public int getScore() {
        return score;
    }

    public void addScore() {
        this.score += 1;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor newFloor) {
        this.floor = newFloor;
    }

    public void receiveTiles(Floor floor) {
        for(int i =0 ;i < tiles.length; i++) {
            tiles[i] = floor.getTiles()[i];
        }
    }

    public void moveTiles2(Floor floor) {
        tiles = floor.sendTile();
    }

}


class Floor {
    // fields
    Tile[] tiles;

    // constructor
    public Floor() {
        tiles = new Tile[10];
    }

    // methods
    public Tile[] getTiles() {
        return this.tiles;
    }

    public Tile[] sendTile() {
        return this.tiles.clone();
    }
}

class Tile {
    // fields
    String color;

    // constructor
    public Tile(String color) {
        this.color = color;
    }

    // methods
    public String getColor() {
        return this.color;
    }

    public void changeToOrange() {
        this.color = "orange";
    }

    public void changeToBlue() {
        this.color = "blue";
    }

    public void changeToGreen() {
        this.color = "green";
    }
}
