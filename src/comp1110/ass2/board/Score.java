package comp1110.ass2.board;

public class Score {


    private int score;

    public Score(int score) {
        this.score = score;
    }

    public Score() {
        score = 0;
    }


    /**
     * add other score
     * @param otherScore the other score with type int
     * @return the score
     */
    public Score addScore(int otherScore) {
        this.score = Math.max(0, this.score + otherScore);
        return this;
    }

    /**
     * add other score
     * @param otherScore the other score with type Score
     * @return the score
     */
    public Score addScore(Score otherScore) {
        this.score = Math.max(0, this.score + otherScore.getScore());
        return this;
    }

    public void minus (int anotherNum) {
        if (score > anotherNum) {
            this.score -= anotherNum;
        } else {
            this.score = 0;
        }
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

    //////////////////NEW//////////////////////////////////


    public void fillFrom(String scoreToken) {
        this.score = Integer.parseInt(scoreToken);
    }


    @Override
    public String toString() {
        // FIXME
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.score);
        return stringBuilder.toString();
    }
}
