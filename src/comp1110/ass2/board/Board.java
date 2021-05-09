package comp1110.ass2.board;

/**
 * @Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 */
public class Board {

    private Score score;
    private Storage storage;
    private Mosaic mosaic;
    private Floor floor;

    public Board() {
        init();
    }


    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Mosaic getMosaic() {
        return mosaic;
    }

    public void setMosaic(Mosaic mosaic) {
        this.mosaic = mosaic;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }


    /**
     * Initialize the board with correct prams.
     */
    public void init() {
        this.score = new Score();
        this.storage = new Storage();
        this.mosaic = new Mosaic();
        this.floor = new Floor();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.score.toString());
        stringBuilder.append('M');
        stringBuilder.append(this.mosaic.toString());
        stringBuilder.append('S');
        stringBuilder.append(this.storage.toString());
        stringBuilder.append('F');
        stringBuilder.append(this.floor.toString());
        return stringBuilder.toString();
    }
}
