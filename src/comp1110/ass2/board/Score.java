package comp1110.ass2.board;

public class Score {
    private int score;

    public Score() {
        score = 0;
    }

    /**
     * add other score
     * @param otherScore
     */
    public void addScore(int otherScore) {
        // FIXME
    }

    /**
     * subtract other score
     * !NOTE: a score cannot goes below 0
     * @param otherScore
     */
    public void subtractScore(int otherScore) {
        // FIXME
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }
}
