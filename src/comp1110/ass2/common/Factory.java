package comp1110.ass2.common;

import comp1110.ass2.Tile;

import java.util.ArrayList;

import static comp1110.ass2.Azul.drawTileFromBag;

public class Factory {
    private String factory;
    private String bag;
    private String discard;

    Bag bagg;

    ////////////////////////////////////////////////////
    private int id;
    private ArrayList<Tile> tiles;


    public Factory() {
        tiles = new ArrayList<>();
    }

    // old
    public Factory (Bag bag) {
        //this.bag = bag;
        tiles = new ArrayList<>();
    }

    public void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }
    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    public void setId(int id) {this.id = id;}
    public int getId() {return this.id;}


    public void drawTiles(Bag bag, Discard discard){
        for(int i = 0; i < 4; i++) {
            Tile tile = bag.drawTile(discard);
            if (tile != null) {
                tiles.add(tile);
            }
        }
    }

    // new
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }


    /**
     * Each factory is filled with exactly four tiles drawn randomly from the bag.
     *
     * @param gameState a array contains four tiles.
     */
    public String[] addTilesFromBag(String[] gameState, Bag bag, Discard discard) {

//        int indexF = gameState[0].indexOf('F', 0);
//        int indexC = gameState[0].indexOf('C', indexF + 1);
//        int indexB = gameState[0].indexOf('B', indexC + 1);
//        int indexD = gameState[0].indexOf('D', indexB + 1);
//        String f =gameState[0].substring(indexF+1,indexC);
//        String b = gameState[0].substring(indexB+1,indexD);
//        String a =gameState[0];
//
//        ArrayList<String> B = new ArrayList<>();
//        ArrayList<String> F = new ArrayList<>();
//
//        for(int i = 0;i<b.length()-1;i=i+2){
//            B.add(b.substring(i,i+2));
//        }
//
//        int index = 0;
//        if(f.length()!=0){
//            return gameState;
//        }
//        else{
//            String d =gameState[0].substring(indexD+1);
//            ArrayList<String> D = new ArrayList<>();
//            for(int k = 0;k<d.length();k=k+2){
//                D.add(d.substring(k,k+2));
//            }
//
//            loop1: for(int i=0;i<5;i++){
//                F.add(""+i);
//
//
//                 for (int j=0;j<4;j++){
//                    int num = 0;
//                    for (String s : B) {
//                        if (s.equals("00")) {
//                            num++;
//                        }
//                    }
//                    if(num ==B.size()){
//                        int numD=0;
//                        for (String s : D) {
//                            if (s.equals("00")) {
//                                numD++;
//                            }
//                        }
//                        if(numD==D.size()){
//                            return gameState;
//                        }
//                        else{
//                            for (int n =0;n<B.size();n=n+1){
//                                B.set(n, D.get(n));
//                                //if(Integer.parseInt(D.get(i))<10){
//                                //B.add(i,"0");
//                                //}
//                                D.set(n,"00");
//                            }
//                            gameState[0] = "F"+String.join("", F)+a.substring(indexC,indexB)+"B"+String.join("", B)+"D"+String.join("",D);
//                        }}
//
//
//                    char tile;
//                     Tile tilet = bag.drawTile(discard);
//                     tile = (tilet == null) ? 'Z' : tilet.getColorCode();
//
//                    if(tile=='a'){
//                        if(Integer.parseInt(B.get(0)) -1<10){
//                            B.set(0,"0"+(Integer.parseInt(B.get(0)) -1));
//                        }else{
//                            B.set(0,""+(Integer.parseInt(B.get(0)) -1));
//                        }
//                    }
//                    if(tile=='b'){
//                        if(Integer.parseInt(B.get(1)) -1<10){
//                            B.set(1,"0"+(Integer.parseInt(B.get(1)) -1));}
//                        else{
//                            B.set(1,""+(Integer.parseInt(B.get(1)) -1));
//                        }
//                    }
//                    if(tile=='c'){
//                        if(Integer.parseInt(B.get(2)) -1<10){
//                            B.set(2,"0"+(Integer.parseInt(B.get(2)) -1));
//                        }else{
//                            B.set(2,""+(Integer.parseInt(B.get(2)) -1));
//                        }
//                    }
//                    if(tile=='d'){
//                        if(Integer.parseInt(B.get(3)) -1<10){
//                            B.set(3,"0"+(Integer.parseInt(B.get(3)) -1));
//                        }else{
//                            B.set(3,""+(Integer.parseInt(B.get(3)) -1));
//                        }
//                    }
//                    if(tile=='e'){
//                        if(Integer.parseInt(B.get(4)) -1<10){
//                            B.set(4,"0"+(Integer.parseInt(B.get(4)) -1));
//                        }else{
//                            B.set(4,""+(Integer.parseInt(B.get(4)) -1));
//                        }
//                    }
//                    if(tile!='Z'){
//                        F.add(""+tile);
//                    }
//                    else{
//                        break loop1;
//                    }
//                    gameState[0] = "F"+String.join("", F)+a.substring(indexC,indexB)+"B"+String.join("", B)+"D"+String.join("",D);
//                }
//            }
//        }
        //gameState[0] = "F"+String.join("", F)+gameState[0].substring(indexC,indexB)+"B"+String.join("", B)+gameState[0].substring(indexD);
        // FIXME Task 6
        //FIXME
        return gameState;
    }

    /**
     * Pick all tiles of the same colour from one factory.
     *
     * @param color the color player chose.
     * @return a array contains all tiles of the color in this factory.
     */
    public ArrayList<Tile> takeTilesFromFactory(String color) {
        //FIXME
        return new ArrayList<Tile>();
    }


    /////////////////////////////NEW////////////////////////////



    public void fillFrom(String factoryState) {
        this.id = factoryState.charAt(0) - 48;
        for(int i = 1; i < factoryState.length(); i++) {
            this.tiles.add(Tile.from(factoryState.charAt(i)));
        }
    }


    @Override
    public String toString() {
        //FIXME
        StringBuilder stringBuilder = new StringBuilder();
        if (tiles.size() != 0) {
            stringBuilder.append(id);
            for (int i = 0; i < tiles.size(); i++) {
                stringBuilder.append(tiles.get(i).getColorCode());
            }
        }
        return stringBuilder.toString();
    }
}
