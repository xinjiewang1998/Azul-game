package comp1110.ass2.gui;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField playerTextField;
    private TextField boardTextField;

    private final ArrayList<Color> colorArrayList = new ArrayList<>(
            Arrays.asList(Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED)
    );

    private final Rectangle[][] storage = new Rectangle[5][5];
    private final Rectangle[][] mosaicA = new Rectangle[5][5];
    private final Rectangle[][] mosaicB = new Rectangle[5][5];
    private final Rectangle[][] floor = new Rectangle[1][7];

    private final Rectangle[][] factories = new Rectangle[2][10];
    private final Rectangle[][] centre = new Rectangle[4][4];
    private final Rectangle[][] bag = new Rectangle[10][10];
    private final Rectangle[][] discard = new Rectangle[10][10];
    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state TASK 4
     */
    void displayState(String[] state) {
        // FIXME Task 4: implement the simple state viewer
        // A20Ma02a13b00e42S2a13e44a1Faabbe
        // A20 M a02 a13 b00 e42 S 2a1 3e4 4a1 F aabbe

        String playerState = state[0];
        String boardState = state[1];

        int indexA = playerState.indexOf('A', 0);
        int indexA2 = playerState.indexOf('B', indexA + 1);
        String playerAStr = playerState.substring(indexA,indexA2);
        String playerBStr = playerState.substring(indexA2);
        ArrayList<String> playerA = new ArrayList<>();
        ArrayList<String> playerB = new ArrayList<>();

        int indexAM = playerAStr.indexOf('M', 0);
        int indexAS = playerAStr.indexOf('S', indexAM+1);
        int indexAF = playerAStr.indexOf('F', indexAS +1);
        int indexBM = playerBStr.indexOf('M', 0);
        int indexBS = playerBStr.indexOf('S', indexBM+1);
        int indexBF = playerBStr.indexOf('F', indexBS +1);

        int indexF = boardState.indexOf('F',0);
        int indexC = boardState.indexOf('C',indexF +1);
        int indexB = boardState.indexOf('B',indexC +1);
        int indexD = boardState.indexOf('D',indexB +1);

        //Decode the status of player a
        if(state[0].length()>=1){
            String substringAM = playerAStr.substring(indexAM, indexAS);
            decodeMosaicB(substringAM);
            String substringAS = playerAStr.substring(indexAS, indexAF);
            decodeStorage(substringAS);
            String substringAF = playerAStr.substring(indexAF);
            decodeFloor(substringAF);

        //Decode the state of player B.
            String substringBM = playerBStr.substring(indexBM, indexBS);
            decodeMosaicA(substringBM);
            String substringBS = playerBStr.substring(indexBS, indexBF);
            decodeStorage(substringBS);
            String substringBF = playerBStr.substring(indexBF);
            decodeFloor(substringBF);
        }

        //Decode the boardState
        if(state[1].length()>=1){
            String substringF = boardState.substring(indexF, indexC);
            decodeFactories(substringF);
            String substringC = boardState.substring(indexC, indexB);
            decodeCentre(substringC);
            String substringB = boardState.substring(indexB, indexD);
            decodeBag(substringB);
            String substringD = boardState.substring(indexD);
            decodeDiscard(substringD);}
    }

    //This step is to make the decoded mosaic position opaque.
    // Opacity means there are ceramic tiles in this place, and transparency means there are no ceramic tiles
    void decodeMosaicA(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaicA[row][column].setOpacity(1.0);
        }
    }

    void decodeMosaicB(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaicB[row][column].setOpacity(1.0);
        }
    }

    //In this step, we hope to cover the colored tiles with the background color (gray) in the storage position
    void decodeStorage(String positions) {
        for(int i = 1; i < positions.length(); i = i+3) {
            int row = positions.charAt(i) - 48;
            char code =  positions.charAt(i+1);
            int num = positions.charAt(i+2) - 48;
            if(code == 'a'){
                //need to cover the color
            } else if(code == 'b'){

            }else if(code == 'c'){

            }else if(code == 'd'){

            }else if(code == 'e'){

            }
        }
    }

    void decodeFloor(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            int num = positions.charAt(i+2) - 48;
            if(code == 'a'){
                //Also need to cover the color method
            } else if(code == 'b'){

            }else if(code == 'c'){

            }else if(code == 'd'){

            }else if(code == 'e'){

            }
        }
    }

    //The example diagram only shows squares and words，do we need to display different color tiles decoded?
    private void decodeDiscard(String substringD) {
        //Record the number of tiles of various colors in the discard
        String a = substringD.substring(1,3);
        String b = substringD.substring(3,5);
        String c = substringD.substring(5,7);
        String d = substringD.substring(7,9);
        String e = substringD.substring(9);
    }

    private void decodeBag(String substringB) {
        //This is used to record the number of tiles of various colors in bag。
        String a = substringB.substring(1,3);
        String b = substringB.substring(3,5);
        String c = substringB.substring(5,7);
        String d = substringB.substring(7,9);
        String e = substringB.substring(9);
    }

    private void decodeCentre(String substringC) {

    }

    private void decodeFactories(String substringF) {
        for(int i = 1; i < substringF.length(); i++) {
            char code = substringF.charAt(i);
            if(code == 'a'||code == 'b'||code == 'c'||code == 'd'||code == 'e'){
                //If it's ceramic tile, it still needs to be covered
            }else if(code == '0'||code == '1'||code == '2'||code == '3'||code == '4'){
                //Locate in which factors and draw
            }
        }
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label playerLabel = new Label("Player State:");
        playerTextField = new TextField();
        playerTextField.setPrefWidth(100);
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(100);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                displayState(new String[]{playerTextField.getText(),
                        boardTextField.getText()});
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(playerLabel, playerTextField, boardLabel,
                boardTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Azul Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        //create five factories，each factory has four tiles.
        for (int i = 0; i<5 ;i++){
            for(int j = 0; j<2 ;j++) {
                for (int k = 0; k < 2; k++) {
                    Rectangle factoriesRect = new Rectangle(10 + i * 90 + j * 33, 10 + k * 33, 30, 30);
                    factoriesRect.setFill(Color.LIGHTGREY);
                    root.getChildren().add(factoriesRect);
                }
            }
            Text text = new Text(40 + i * 90 , 10 + 75, String.valueOf(i));
            text.setFill(Color.BLACK);
            root.getChildren().add(text);
        }

        //create a Centre area
        Rectangle CentreRect = new Rectangle(500, 15, 100, 100);
        CentreRect.setFill(Color.BLACK);
        root.getChildren().add(CentreRect);
        Rectangle CentreRect1 = new Rectangle(505, 20, 90, 90);
        CentreRect1.setFill(Color.WHITE);
        root.getChildren().add(CentreRect1);
        for (int i = 0; i<4 ;i++){
            for(int j = 0; j<4 ;j++) {
                if(i==3&&j==3){break;}
                Rectangle CentreRect2 = new Rectangle(510 + i * 20 , 25 + j * 20, 18, 18);
                CentreRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(CentreRect2);
            }
        }
        Text CentreText = new Text(530 , 10, "Centre");
        CentreText.setFill(Color.BLACK);
        root.getChildren().add(CentreText);

        //create a Bag area
        Rectangle BagRect = new Rectangle(500, 130, 200, 200);
        CentreRect.setFill(Color.BLACK);
        root.getChildren().add(BagRect);
        Rectangle BagRect1 = new Rectangle(505, 135, 190, 190);
        BagRect1.setFill(Color.WHITE);
        root.getChildren().add(BagRect1);
        for (int i = 0; i<10 ;i++){
            for(int j = 0; j<10 ;j++) {
                Rectangle BagRect2 = new Rectangle(510 + i * 18 , 140 + j * 18, 17, 17);
                BagRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(BagRect2);
            }
        }
        Text BagText = new Text(590 , 125, "Bag");
        BagText.setFill(Color.BLACK);
        root.getChildren().add(BagText);

        //create a Discard area
        Rectangle DiscardRect = new Rectangle(500, 345, 200, 200);
        CentreRect.setFill(Color.BLACK);
        root.getChildren().add(DiscardRect);
        Rectangle DiscardRect1 = new Rectangle(505, 350, 190, 190);
        DiscardRect1.setFill(Color.WHITE);
        root.getChildren().add(DiscardRect1);
        for (int i = 0; i<10 ;i++){
            for(int j = 0; j<10 ;j++) {
                Rectangle DiscardRect2 = new Rectangle(510 + i * 18 , 355 + j * 18, 17, 17);
                DiscardRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(DiscardRect2);
            }
        }
        Text DiscardText = new Text(590 , 340, "Discard");
        DiscardText.setFill(Color.BLACK);
        root.getChildren().add(DiscardText);

//          Rectangle mosaicRect = new Rectangle(50, 500, 570, 90);
//          mosaicRect.setFill(Color.RED);
//          root.getChildren().add(mosaicRect);

//        Rectangle floorRect = new Rectangle(50, 500, 570, 90);
//        floorRect.setFill(Color.Yellow);
//        root.getChildren().add(floorRect);

//        Rectangle tileRect = new Rectangle(420, 100, 80, 80);
//        tileRect.setFill(Color.GREEN);
//        root.getChildren().add(tileRect);

        //Title： "Player 1 player board"，and "Player 2 player board"
        Text PlayerAText = new Text(100 , 155, "Player 1 player board");
        PlayerAText.setFont(Font.font(32));
        PlayerAText.setFill(Color.BLACK);
        root.getChildren().add(PlayerAText);
        Rectangle PlayerARect = new Rectangle(5, 160, 490, 350);
        PlayerARect.setFill(Color.BLACK);
        root.getChildren().add(PlayerARect);
        Rectangle PlayerARect1 = new Rectangle(8, 163, 484, 344);
        PlayerARect1.setFill(Color.WHITE);
        root.getChildren().add(PlayerARect1);

        Text PlayerBText = new Text(800 , 155, "Player 2 player board");
        PlayerBText.setFont(Font.font(32));
        PlayerBText.setFill(Color.BLACK);
        root.getChildren().add(PlayerBText);
        Rectangle PlayerBRect = new Rectangle(705, 160, 490, 350);
        PlayerBRect.setFill(Color.BLACK);
        root.getChildren().add(PlayerBRect);
        Rectangle PlayerBRect1 = new Rectangle(708, 163, 484, 344);
        PlayerBRect1.setFill(Color.WHITE);
        root.getChildren().add(PlayerBRect1);

        ////////////////////// STORAGE //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 3) {
                    continue;
                }
                Rectangle tileRect = new Rectangle(10 + 45 * j, 180 + 45 * i, 40, 40);
                tileRect.setFill(Color.LIGHTGREY);
                root.getChildren().add(tileRect);
            }
        }
        Text StorageText = new Text(100 , 420, "Storage");
        StorageText.setFill(Color.BLACK);
        root.getChildren().add(StorageText);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 3) {
                    continue;
                }
                Rectangle tileRect = new Rectangle(710 + 45 * j, 180 + 45 * i, 40, 40);
                tileRect.setFill(Color.LIGHTGREY);
                root.getChildren().add(tileRect);
            }
        }
        Text StorageText2 = new Text(800 , 420, "Storage");
        StorageText2.setFill(Color.BLACK);
        root.getChildren().add(StorageText2);

        ////////////////////// <MOSAIC> //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle tileRect = new Rectangle(240 + 10 + 45 * j, 180 + 45 * i, 40, 40);
                tileRect.setFill(colorArrayList.get((j - i + 5) % 5));
                tileRect.setOpacity(0.2);
                mosaicA[i][j] = tileRect;
                root.getChildren().add(tileRect);
            }
        }
        Text MosaicText = new Text(340 , 420, "Mosaic");
        MosaicText.setFill(Color.BLACK);
        root.getChildren().add(MosaicText);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle tileRect = new Rectangle(940 + 10 + 45 * j, 180 + 45 * i, 40, 40);
                tileRect.setFill(colorArrayList.get((j - i + 5) % 5));
                tileRect.setOpacity(0.2);
                mosaicB[i][j] = tileRect;
                root.getChildren().add(tileRect);
            }
        }
        Text MosaicText2 = new Text(1040 , 420, "Mosaic");
        MosaicText2.setFill(Color.BLACK);
        root.getChildren().add(MosaicText2);

        ////////////////////// FLOOR //////////////////////

        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(10 + 45 * i, 430, 40, 40);
            tileRect.setFill(Color.LIGHTGREY);
            root.getChildren().add(tileRect);
        }
        Text FloorText = new Text(150 , 490, "Floor");
        FloorText.setFill(Color.BLACK);
        root.getChildren().add(FloorText);

        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(710 + 45 * i, 430, 40, 40);
            tileRect.setFill(Color.LIGHTGREY);
            root.getChildren().add(tileRect);
        }
        Text FloorText2 = new Text(850 , 490, "Floor");
        FloorText2.setFill(Color.BLACK);
        root.getChildren().add(FloorText2);


        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
