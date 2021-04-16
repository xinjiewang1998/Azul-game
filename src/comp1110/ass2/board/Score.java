package comp1110.ass2.board;

public class Score {
<<<<<<< HEAD

    int score;

    public Score() {
        score = 0;
    }

    public void minus (int anotherNum) {
        if (score > anotherNum) {
            this.score -= anotherNum;
        } else {
            this.score = 0;
        }
=======
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
>>>>>>> a91385b2ffdf018acb34b1448499b292d5f24f2f
    }
}
