package comp1110.ass2.board;

public class Score {
    private int score;

    public Score(int score) {
        this.score = score;
    }

    /**
     * add other score
     * @param otherScore the other score with type int
     */
    public void addScore(int otherScore) {
        // FIXME
    }

    /**
     * add other score
     * @param otherScore the other score with type Score
     */
    public void addScore(Score otherScore) {
        // FIXME
    }

    /**
     * subtract other score
     * !NOTE: a score cannot goes below 0
     * @param otherScore the other score with type int
     */
    public void subtractScore(int otherScore) {
        // FIXME
    }

    /**
     * subtract other score
     * !NOTE: a score cannot goes below 0
     * @param otherScore the other score with type Score
     */
    public void subtractScore(Score otherScore) {
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
