package comp1110.ass2.board;

import comp1110.ass2.Tile;
import java.util.ArrayList;
import java.util.Arrays;

public class Mosaic {

    private final int NUM_ROWS = 5;
    private final ArrayList<String> DEFAULT_COLORS = new ArrayList<>(
            Arrays.asList("blue", "green", "orange", "purple", "red"));

    // simple [][] is enough for use.
    private Tile[][] square;

    public Mosaic () {
        this.square = new Tile[NUM_ROWS][NUM_ROWS];
    }

    /**
     * calculate the column number according to the color and row number.
     * @param color the target color
     * @param row the target row number
     * @return the column number
     */
    public int calculateColumn(String color, int row) {
        // FIXME
        return 0;
    }

    /**
     * tells whether a colored tile has occupied that row.
     * if it is true, cannot put tile with same color on that row again.
     * @param color the target color
     * @param row the target row
     * @return true if the color exists in mosaic
     */
    public boolean hasColor(String color, int row) {
        // FIXME
        return true;
    }

    /**
     * find the correct position and place the tile
     * @param tile the tile to be placed
     * @param row the target row
     */
    public void addTile(Tile tile, int row) {
        // FIXME
    }

    /**
     * check if there is a complete row in our mosaic
     * if it has, then end the game.
     * @return true if exists a complete row
     */
    public boolean hasCompleteRow() {
        // FIXME
        return true;
    }

    /**
     * Each player gains additional bonus points if they satisfy the following conditions:
     *
     * Gain 2 points for each complete row of your mosaic (5 consecutive horizontal tiles).
     * Gain 7 points for each complete column of your mosaic (5 consecutive vertical tiles).
     * Gain 10 points for each colour of tile for which you have placed all 5 tiles on your mosaic.
     * @return the Bonus Score
     */
    public int calculateBonusScore(String[] gameState, char player) {
        int indexA = gameState[1].indexOf('A', 0);
        int indexB = gameState[1].indexOf('B', indexA + 1);
        //int indexC = gameState[1].indexOf('C', indexB + 1);
        String a =gameState[1].substring(indexA,indexB);
        String b = gameState[1].substring(indexB);
        ArrayList<String> AM = new ArrayList<>();
        ArrayList<String> BM = new ArrayList<>();
        int indexAM = a.indexOf('M', 0);
        int indexAA = a.indexOf('A', 0);
        int indexBM = b.indexOf('M', 0);
        int indexBB = b.indexOf('B', 0);
        int indexAS = a.indexOf('S', 0);
        int indexBS = b.indexOf('S', 0);
        String am =a.substring(indexAM+1,indexAS);
        String bm = b.substring(indexBM+1,indexBS);
        for(int i = 0;i<am.length();i=i+1){
            AM.add(am.substring(i,i+1));
        }
        for(int i = 0;i<bm.length();i=i+1){
            BM.add(bm.substring(i,i+1));
        }
        int ScoreA =0;
        int ScoreB = 0;
        int Score=0;
        if(player=='A'){
            int countA = 0;
            int countB = 0;
            int countC = 0;
            int countD = 0;
            int countE = 0;
            for(int i = 0;i< AM.size()-1;i++){

                if(AM.get(i).equals("a")){
                    countA++;
                }
                else if(AM.get(i).equals("b")){
                    countB++;
                }
                else if(AM.get(i).equals("c")){
                    countC++;
                }
                else if(AM.get(i).equals("d")){
                    countD++;
                }
                else if(AM.get(i).equals("e")){
                    countE++;
                }
            }
            if(countA ==5){
                ScoreA +=10;
            }
            if(countB==5){
                ScoreA +=10;
            }
            if(countC==5){
                ScoreA +=10;
            }
            if(countD==5){
                ScoreA +=10;
            }
            if(countE==5){
                ScoreA +=10;
            }

            ArrayList<String> pos = new ArrayList<String>();
            for(int i=1;i<am.length();i+=3){
                pos.add(am.substring(i,i+2));
            }
            for(int i=0;i<5;i++){
                int num = 0;
                for(int j=0;j<5;j++){
                    for(int k=0;k< pos.size();k++){
                        String aa  = ""+i+j;
                        if(pos.get(k).equals(aa)){
                            num++;break;
                        }
                    }
                }
                if(num == 5){
                    ScoreA +=2;
                }
            }
            for(int i=0;i<5;i++){
                int num_1 = 0;
                for(int j=0;j<5;j++){
                    for (String po : pos) {
                        String aa = "" + j + i;
                        if (po.equals(aa)) {
                            num_1++;
                            break;
                        }
                    }
                }
                if(num_1 == 5){
                    ScoreA +=7;
                }
            }
            Score=ScoreA;}
        if(player=='B'){
            int countA = 0;
            int countB = 0;
            int countC = 0;
            int countD = 0;
            int countE = 0;
            for(int i = 0;i< BM.size()-1;i++){

                if(BM.get(i).equals("a")){
                    countA++;
                }
                else if(BM.get(i).equals("b")){
                    countB++;
                }
                else if(BM.get(i).equals("c")){
                    countC++;
                }
                else if(BM.get(i).equals("d")){
                    countD++;
                }
                else if(BM.get(i).equals("e")){
                    countE++;
                }
            }
            if(countA ==5){
                ScoreB +=10;
            }
            if(countB==5){
                ScoreB +=10;
            }
            if(countC==5){
                ScoreB +=10;
            }
            if(countD==5){
                ScoreB +=10;
            }
            if(countE==5){
                ScoreB +=10;
            }

            ArrayList<String> pos = new ArrayList<String>();
            for(int i=1;i<bm.length();i+=3){
                pos.add(bm.substring(i,i+2));
            }
            for(int i=0;i<5;i++){
                int num = 0;
                for(int j=0;j<5;j++){
                    for(int k=0;k< pos.size();k++){
                        String bb  = ""+i+j;
                        if(pos.get(k).equals(bb)){
                            num++;break;
                        }
                    }
                }
                if(num == 5){
                    ScoreB +=2;
                }
            }
            for(int i=0;i<5;i++){
                int num_1 = 0;
                for(int j=0;j<5;j++){
                    for (String po : pos) {
                        String bb = "" + j + i;
                        if (po.equals(bb)) {
                            num_1++;
                            break;
                        }
                    }
                }
                if(num_1 == 5){
                    ScoreB +=7;
                }
            }
            Score=ScoreB;
        }

        return Score;
    }

    @Override
    public String toString() {
        // FIXME
        return "";
    }

}
