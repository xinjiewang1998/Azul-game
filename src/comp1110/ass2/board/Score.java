package comp1110.ass2.board;

public class Score {

    private int score;

    public Score() {
        this.score = 0;
    }

    public Score(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * add other score
     *
     * @param otherScore the other score with type int
     * @return the score
     */
    public Score addScore(int otherScore) {
        this.score = Math.max(0, this.score + otherScore);
        return this;
    }

    /**
     * add other score
     *
     * @param otherScore the other score with type Score
     * @return the score
     */
    public Score addScore(Score otherScore) {
        this.score = Math.max(0, this.score + otherScore.getScore());
        return this;
    }

    /**
     * reconstruct internal state from string
     *
     * @param token the string representation of score state
     */
    public void reconstructFromString(String token) {
        this.score = Integer.parseInt(token);
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.score);
        return stringBuilder.toString();
    }
}
