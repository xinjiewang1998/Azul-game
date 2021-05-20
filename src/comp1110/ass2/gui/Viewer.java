package comp1110.ass2.gui;

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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 */
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

    private final Rectangle[][] storageA = new Rectangle[5][5];
    private final Rectangle[][] storageB = new Rectangle[5][5];
    private final Rectangle[][] mosaicA = new Rectangle[5][5];
    private final Rectangle[][] mosaicB = new Rectangle[5][5];
    private final Rectangle[] floorA = new Rectangle[7];
    private final Rectangle[] floorB = new Rectangle[7];


    private final Rectangle[][] factories = new Rectangle[2][10];
    private final Rectangle[][] centre = new Rectangle[4][4];
    private final Rectangle[][] bag = new Rectangle[10][10];
    private final Rectangle[][] discard = new Rectangle[10][10];

    /**
     * @Author: Xiang Lu
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state TASK 4
     */
    public void displayState(String[] state) {

        // Example Board: AF1bbbe2abde3cdee4bcceCfadB1915161614D1103100920
        // Example Player:
        // A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaafB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd
        changeState();

        //////////////////////////// PLAYER STATE /////////////////////////////////////
        String playerState = state[0];
        if (!playerState.equals("")) {

            int indexA = playerState.indexOf('A', 0);
            int indexB = playerState.indexOf('B', indexA + 1);

            String playerAStr = "";
            if (indexB != -1) {
                playerAStr = playerState.substring(indexA, indexB);
            } else {
                playerAStr = playerState.substring(indexA);
            }

            int indexAM = playerAStr.indexOf('M', 0);
            int indexAS = playerAStr.indexOf('S', indexAM + 1);
            int indexAF = playerAStr.indexOf('F', indexAS + 1);

            String substringAM = playerAStr.substring(indexAM, indexAS);
            decodeMosaic(substringAM, mosaicA);
            String substringAS = playerAStr.substring(indexAS, indexAF);
            decodeStorage(substringAS, storageA);
            String substringAF = playerAStr.substring(indexAF);
            decodeFloor(substringAF, floorA);

            String playerBStr = playerState.substring(indexB);

            int indexBM = playerBStr.indexOf('M', 0);
            int indexBS = playerBStr.indexOf('S', indexBM + 1);
            int indexBF = playerBStr.indexOf('F', indexBS + 1);

            String substringBM = playerBStr.substring(indexBM, indexBS);
            decodeMosaic(substringBM, mosaicB);
            String substringBS = playerBStr.substring(indexBS, indexBF);
            decodeStorage(substringBS, storageB);
            String substringBF = playerBStr.substring(indexBF);
            decodeFloor(substringBF, floorB);
        }
        //////////////////////////// BOARD STATE /////////////////////////////////////
        String boardState = state[1];
        if (!boardState.equals("")) {
            int indexF = boardState.indexOf('F', 0);
            int indexC = boardState.indexOf('C', indexF + 1);
            int indexB2 = boardState.indexOf('B', indexC + 1);
            int indexD = boardState.indexOf('D', indexB2 + 1);

            String substringF = boardState.substring(indexF, indexC);
            decodeFactories(substringF, factories);
            String substringC = boardState.substring(indexC, indexB2);
            decodeCentre(substringC, centre);
            String substringB = boardState.substring(indexB2, indexD);
            decodeBag(substringB, bag);
            String substringD = boardState.substring(indexD);
            decodeDiscard(substringD, discard);
        }
    }

    /**
     * @Author: Xiang Lu
     * This step is to make the decoded mosaic position opaque, opacity means there are ceramic
     * tiles in this place, and transparency means there are no ceramic tiles.
     *
     * @param positions the position at mosaic
     * @param mosaic    the mosaic
     */
    public void decodeMosaic(String positions, Rectangle[][] mosaic) {
        for (int i = 1; i < positions.length(); i++) {
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaic[row][column].setOpacity(1.0);
        }
    }

    /**
     * @Author: Xiang Lu
     * In this step, we hope to cover the colored tiles with the background color (gray) in the
     * storage position
     *
     * @param positions the position at storage
     * @param storage   the storage
     */
    public void decodeStorage(String positions, Rectangle[][] storage) {
        for (int i = 1; i < positions.length(); i = i + 3) {
            int row = positions.charAt(i) - 48;
            char code = positions.charAt(i + 1);
            int num = positions.charAt(i + 2) - 48;
            for (int q = 4; q > 3 - row + num - 1; q--) {
                storage[row][q].setFill(fillColor(code));
            }

        }
    }

    /**
     * @Author: Xiang Lu
     * Decode floor info
     *
     * @param positions the position at floor
     * @param floor     the floor
     */
    public void decodeFloor(String positions, Rectangle[] floor) {
        for (int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            floor[i - 1].setFill(fillColor(code));
        }
    }

    /**
     * @Author: Xiang Lu
     * Decode factories' info
     *
     * @param substringF the substring of factory
     * @param factories  the factories
     */
    public void decodeFactories(String substringF, Rectangle[][] factories) {
        for (int n = 1; n < substringF.length(); n = n + 5) {
            int x = (substringF.charAt(n) - 48) % 5 - 1;
            for (int m = 0; m < 4; m++) {
                char code = substringF.charAt(n + m + 1);
                int column = m / 2 + x * 2 + 2;
                int row = m % 2;
                factories[row][column].setFill(fillColor(code));
            }
        }
    }

    /**
     * @Author: Xiang Lu
     * Decode discard info
     *
     * @param substringC the substring of centre
     * @param centre     the centre
     */
    public void decodeCentre(String substringC, Rectangle[][] centre) {
        for (int i = 1; i < substringC.length(); i++) {
            char code = substringC.charAt(i);
            centre[(i - 1) % 4][(i - 1) / 4].setFill(fillColor(code));
        }
    }

    /**
     * @Author: Xiang Lu
     * Decode discard info
     *
     * @param substringD the substring of discard
     * @param discard    the discard
     */
    public void decodeDiscard(String substringD, Rectangle[][] discard) {
        //Record the number of tiles of various colors in the discard
        fillBagAndDiscard(substringD, discard);
    }

    /**
     * @Author: Xiang Lu
     * Decode bag info
     *
     * @param substringB the substring of bag
     * @param bag        the bag
     */
    public void decodeBag(String substringB, Rectangle[][] bag) {
        //This is used to record the number of tiles of various colors in bag。
        fillBagAndDiscard(substringB, bag);
    }

    /**
     * @Author: Xiang Lu
     * Fill bag and discard with color
     *
     * @param substring the substring of bag or discard
     * @param rectangle the rectangle
     */
    public void fillBagAndDiscard(String substring, Rectangle[][] rectangle) {
        int a = Integer.parseInt(substring.substring(1, 3));
        int b = Integer.parseInt(substring.substring(3, 5));
        int c = Integer.parseInt(substring.substring(5, 7));
        int d = Integer.parseInt(substring.substring(7, 9));
        int e = Integer.parseInt(substring.substring(9));

        fillColor(a, 0, rectangle, Color.BLUE);
        fillColor(b, 2, rectangle, Color.GREEN);
        fillColor(c, 4, rectangle, Color.ORANGE);
        fillColor(d, 6, rectangle, Color.PURPLE);
        fillColor(e, 8, rectangle, Color.RED);
    }

    /**
     * @Author: Xiang Lu
     * Fill the rectangle
     *
     * @param a         the limit
     * @param p         the start
     * @param rectangle the rectangle
     * @param color     the color
     */
    private void fillColor(int a, int p, Rectangle[][] rectangle, Color color) {
        if (a <= 10) {
            for (int i = p; i < p + 1; i++) {
                for (int j = 0; j < a; j++) {
                    rectangle[j][i].setFill(color);
                }
            }
        } else {
            for (int i = p; i < p + 2; i++) {
                if (i == p) {
                    for (int j = 0; j < 10; j++) {
                        rectangle[j][i].setFill(color);
                    }
                } else {
                    for (int j = 0; j < (a - 10); j++) {
                        rectangle[j][i].setFill(color);
                    }
                }
            }
        }
    }

    /**
     * @Author: Xiang Lu
     * Map code to color
     *
     * @param code the code
     * @return the color
     */
    private Color fillColor(char code) {
        return switch (code) {
            case 'a' -> Color.BLUE;
            case 'b' -> Color.GREEN;
            case 'c' -> Color.ORANGE;
            case 'd' -> Color.PURPLE;
            case 'e' -> Color.RED;
            case 'f' -> Color.FIREBRICK;
            default -> Color.GREY;
        };
    }

    /**
     * @Author: Xiang Lu
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

    /**
     * @Author: Xiang Lu
     * Create an initial interface for gamers
     */

    private void changeState() {
        //create five factories，each factory has four tiles.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Rectangle factoriesRect = new Rectangle(10 + i * 90 + j * 33, 10 + k * 33, 30,
                            30);
                    factoriesRect.setFill(Color.LIGHTGREY);
                    root.getChildren().add(factoriesRect);
                    factories[j][i * 2 + k] = factoriesRect;
                }
            }
            Text text = new Text(40 + i * 90, 10 + 75, String.valueOf(i));
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    break;
                }
                Rectangle CentreRect2 = new Rectangle(510 + i * 20, 25 + j * 20, 18, 18);
                CentreRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(CentreRect2);
                centre[i][j] = CentreRect2;
            }
        }
        Text CentreText = new Text(530, 10, "Centre");
        CentreText.setFill(Color.BLACK);
        root.getChildren().add(CentreText);

        //create a Bag area
        Rectangle BagRect = new Rectangle(500, 130, 200, 200);
        CentreRect.setFill(Color.BLACK);
        root.getChildren().add(BagRect);
        Rectangle BagRect1 = new Rectangle(505, 135, 190, 190);
        BagRect1.setFill(Color.WHITE);
        root.getChildren().add(BagRect1);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle BagRect2 = new Rectangle(510 + i * 18, 140 + j * 18, 17, 17);
                BagRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(BagRect2);
                bag[i][j] = BagRect2;
            }
        }
        Text BagText = new Text(590, 125, "Bag");
        BagText.setFill(Color.BLACK);
        root.getChildren().add(BagText);

        //create a Discard area
        Rectangle DiscardRect = new Rectangle(500, 345, 200, 200);
        CentreRect.setFill(Color.BLACK);
        root.getChildren().add(DiscardRect);
        Rectangle DiscardRect1 = new Rectangle(505, 350, 190, 190);
        DiscardRect1.setFill(Color.WHITE);
        root.getChildren().add(DiscardRect1);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle DiscardRect2 = new Rectangle(510 + i * 18, 355 + j * 18, 17, 17);
                DiscardRect2.setFill(Color.LIGHTGREY);
                root.getChildren().add(DiscardRect2);
                discard[i][j] = DiscardRect2;
            }
        }
        Text DiscardText = new Text(590, 340, "Discard");
        DiscardText.setFill(Color.BLACK);
        root.getChildren().add(DiscardText);

        //Title： "Player 1 player board"，and "Player 2 player board"
        Text PlayerAText = new Text(100, 155, "Player 1 player board");
        PlayerAText.setFont(Font.font(32));
        PlayerAText.setFill(Color.BLACK);
        root.getChildren().add(PlayerAText);
        Rectangle PlayerARect = new Rectangle(5, 160, 490, 350);
        PlayerARect.setFill(Color.BLACK);
        root.getChildren().add(PlayerARect);
        Rectangle PlayerARect1 = new Rectangle(8, 163, 484, 344);
        PlayerARect1.setFill(Color.WHITE);
        root.getChildren().add(PlayerARect1);

        Text PlayerBText = new Text(800, 155, "Player 2 player board");
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
                storageA[i][j] = tileRect;
                root.getChildren().add(tileRect);
            }
        }
        Text StorageText = new Text(100, 420, "Storage");
        StorageText.setFill(Color.BLACK);
        root.getChildren().add(StorageText);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 3) {
                    continue;
                }
                Rectangle tileRect = new Rectangle(710 + 45 * j, 180 + 45 * i, 40, 40);
                tileRect.setFill(Color.LIGHTGREY);
                storageB[i][j] = tileRect;
                root.getChildren().add(tileRect);
            }
        }
        Text StorageText2 = new Text(800, 420, "Storage");
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
        Text MosaicText = new Text(340, 420, "Mosaic");
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
        Text MosaicText2 = new Text(1040, 420, "Mosaic");
        MosaicText2.setFill(Color.BLACK);
        root.getChildren().add(MosaicText2);

        ////////////////////// FLOOR //////////////////////

        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(10 + 45 * i, 430, 40, 40);
            tileRect.setFill(Color.LIGHTGREY);
            floorA[i] = tileRect;
            root.getChildren().add(tileRect);
        }
        Text FloorText = new Text(150, 490, "Floor");
        FloorText.setFill(Color.BLACK);
        root.getChildren().add(FloorText);

        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(710 + 45 * i, 430, 40, 40);
            tileRect.setFill(Color.LIGHTGREY);
            floorB[i] = tileRect;
            root.getChildren().add(tileRect);
        }
        Text FloorText2 = new Text(850, 490, "Floor");
        FloorText2.setFill(Color.BLACK);
        root.getChildren().add(FloorText2);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Azul Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        changeState();
        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
