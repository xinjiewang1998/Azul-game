package comp1110.ass2.board;

public class Score {

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
    }
}
