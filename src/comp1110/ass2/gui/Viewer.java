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

    private Rectangle[][] mosaic = new Rectangle[5][5];
    private Rectangle[][] Storage = new Rectangle[5][5];
    private Rectangle[][] Floor = new Rectangle[1][7];
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

        int indexM = playerState.indexOf("M");
        int indexS = playerState.indexOf("S");
        int indexF = playerState.indexOf("F");
        int indexB = playerState.indexOf("B");

        String substringm = playerState.substring(indexM, indexS);
        decodeMosaic(substringm);

        String substrings = playerState.substring(indexS, indexF);
        decodeStorage(substrings);

        String substringf = playerState.substring(indexF, indexB);
        decodeFloor(substringf);
    }

    void decodeMosaic(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaic[row][column].setOpacity(1.0);
        }
    }

    void decodeStorage(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            int row = positions.charAt(i) - 48;
            i = i + 1;
            char code =  positions.charAt(i);
            String code1 = String.valueOf(code);
            i = i + 1;
            int num = positions.charAt(i) - 48;
            for(int j =1; j<=num;j++){
                switch (code1){
                    case "a":
                        //想把灰色的初始瓷砖变成解码出来的颜色
                };
            }
        }
    }

    void decodeFloor(String positions) {
        for(int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            i = i + 1;
            Floor[1][i].setOpacity(1.0);
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

        Rectangle storageRect = new Rectangle(50, 50, 410, 410);
        storageRect.setFill(Color.GREY);
        root.getChildren().add(storageRect);

        Rectangle mosaicRect = new Rectangle(700, 50, 410, 410);
        mosaicRect.setFill(Color.YELLOW);
//        root.getChildren().add(mosaicRect);

        Rectangle floorRect = new Rectangle(50, 500, 570, 90);
        floorRect.setFill(Color.RED);
        root.getChildren().add(floorRect);

//        Rectangle tileRect = new Rectangle(420, 100, 80, 80);
//        tileRect.setFill(Color.GREEN);
//        root.getChildren().add(tileRect);

        ////////////////////// STORAGE //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 3) {
                    continue;
                }
                Rectangle tileRect = new Rectangle(50 + 10 + 80 * j,
                        50 + 10 + 80 * i, 70, 70);
                tileRect.setFill(Color.GREY);
                tileRect.setOpacity(0.2);
                /*
                怎么才能变色，底色是灰色，然后把decode出来的转成颜色填充进去
                 */
                root.getChildren().add(tileRect);
            }
        }

        ////////////////////// <MOSAIC> //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle tileRect = new Rectangle(700 + 10 + 80 * j,
                        50 + 10 + 80 * i, 70, 70);
                tileRect.setFill(colorArrayList.get((j - i + 5) % 5));
                tileRect.setOpacity(0.2);
                mosaic[i][j] = tileRect;
                root.getChildren().add(tileRect);
            }
        }

        ////////////////////// FLOOR //////////////////////
        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(50 + 10 + 80 * i,
                    500 + 10, 70, 70);
            tileRect.setFill(Color.GREEN);
            root.getChildren().add(tileRect);
        }

        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
