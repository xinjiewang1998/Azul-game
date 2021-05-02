package comp1110.ass2.gui;

import comp1110.ass2.Azul;
import comp1110.ass2.Player;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javax.swing.text.Element;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 * <p>
 * WILL Adapt third party code Dinosaurs to make tiles draggable. Third Party: Dinosaurs
 * (https://gitlab.cecs.anu.edu.au/comp1110/dinosaurs)
 *
 * Some testing data
 * A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaaB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd
 * AF1bbbe2abde3cdee4bcceCadfB1915161614D1103100920
 * A1b4 B3e2 A4cF
 */
public class Game extends Application {

    /* comp1110.ass2.board layout */
    private static final int BOARD_WIDTH = 1200;
    private static final int BOARD_HEIGHT = 700;

    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField playerTextField;
    private TextField boardTextField;
    private TextField moveTextField;

    private final ArrayList<Color> colorArrayList = new ArrayList<>(
            Arrays.asList(Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED)
    );
    private comp1110.ass2.Game game = new comp1110.ass2.Game();



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
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state TASK 4
     */
    void displayState(String[] state) {

        // Example Board: AF1bbbe2abde3cdee4bcceCadfB1915161614D1103100920
        // Example Player:
        // A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaaB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd
        changeState();

        game.reconstructBoardsFrom(state[0]);
        game.reconstructCommonFrom(state[1]);
        System.out.println(game.rebuildStateString()[0]);
        System.out.println(game.rebuildStateString()[1]);

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
     * This step is to make the decoded mosaic position opaque, opacity means there are ceramic
     * tiles in this place, and transparency means there are no ceramic tiles.
     *
     * @param positions the position at mosaic
     * @param mosaic    the mosaic
     */
    private void decodeMosaic(String positions, Rectangle[][] mosaic) {
        for (int i = 1; i < positions.length(); i++) {
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaic[row][column].setOpacity(1.0);
        }
    }

    /**
     * In this step, we hope to cover the colored tiles with the background color (gray) in the
     * storage position
     *
     * @param positions the position at storage
     * @param storage   the storage
     */
    private void decodeStorage(String positions, Rectangle[][] storage) {
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
     * Decode floor info
     *
     * @param positions the position at floor
     * @param floor     the floor
     */
    private void decodeFloor(String positions, Rectangle[] floor) {
        for (int i = 1; i < positions.length(); i++) {
            char code = positions.charAt(i);
            floor[i - 1].setFill(fillColor(code));
        }
    }

    /**
     * Decode factories' info
     *
     * @param substringF the substring of factory
     * @param factories  the factories
     */
    private void decodeFactories(String substringF, Rectangle[][] factories) {
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
     * Decode discard info
     *
     * @param substringC the substring of centre
     * @param centre     the centre
     */
    private void decodeCentre(String substringC, Rectangle[][] centre) {
        for (int i = 1; i < substringC.length(); i++) {
            char code = substringC.charAt(i);
            centre[(i - 1) % 4][(i - 1) / 4].setFill(fillColor(code));
        }
    }

    /**
     * Decode discard info
     *
     * @param substringD the substring of discard
     * @param discard    the discard
     */
    void decodeDiscard(String substringD, Rectangle[][] discard) {
        //Record the number of tiles of various colors in the discard
        fillBagAndDiscard(substringD, discard);
    }

    /**
     * Decode bag info
     *
     * @param substringB the substring of bag
     * @param bag        the bag
     */
    private void decodeBag(String substringB, Rectangle[][] bag) {
        //This is used to record the number of tiles of various colors in bag。
        fillBagAndDiscard(substringB, bag);
    }

    /**
     * Fill bag and discard with color
     *
     * @param substring the substring of bag or discard
     * @param rectangle the rectangle
     */
    private void fillBagAndDiscard(String substring, Rectangle[][] rectangle) {
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

    void moveState(String move) {

        // Example Player:
        // A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaaB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd
        // A21 M a00b01c02d03e04a11b12c13e21a22c30b34a44       S2b2 Faaaa
        // B24 M a00c02d03e04e10a11b12c13d14d20a22b23e32b34b40 S4d2 Fdd
        // Example Board: AF1bbbe2abde3cdee4bcceCadfB1915161614D1103100920
        // Example Move: A1b4 B3e2 A4cF B2b0

        // AFCB0000000000D0000000000
        // A21 M a00b01c02d03e04a11b12c13e21a22c30b34a44       S2b2 Faaaaf
        // B24 M a00c02d03e04e10a11b12c13d14d20a22b23e32b34b40 S4d2 Fdd
        changeState();

        String[] currentState = game.rebuildStateString();
        game.applyMove(currentState, move);
        String[] newState = game.rebuildStateString();
        System.out.println(newState[0]);
        System.out.println(newState[1]);
        //////////////////////////// PLAYER STATE /////////////////////////////////////
        String playerState = newState[1];
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
        String boardState = newState[0];
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
        Label moveLabel = new Label("Move:");
        moveTextField = new TextField();
        moveTextField.setPrefWidth(100);
        Button moveButton = new Button("Apply Move");
        moveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                moveState(moveTextField.getText());
            }
        });

        HBox hb = new HBox();
        hb.getChildren().addAll(playerLabel, playerTextField, boardLabel,
                boardTextField, button, moveTextField, moveButton);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(BOARD_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

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

//        Text examplePlayer = new Text (130, 570,
//                "A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaaB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd");
//        Text exampleCommon = new Text(130, 585,"AF1bbbe2abde3cdee4bcceCadfB1915161614D1103100920");
//        Text exampleMove = new Text(130, 600, "A1b4 B3e2 A4cF");
//        root.getChildren().add(examplePlayer);
//        root.getChildren().add(exampleCommon);
//        root.getChildren().add(exampleMove);

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
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        changeState();
        root.getChildren().add(controls);
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

//package comp1110.ass1.gui;
//
//        import comp1110.ass1.Dinosaurs;
//        import comp1110.ass1.Orientation;
//        import comp1110.ass1.Tile;
//        import javafx.application.Application;
//        import javafx.application.Platform;
//        import javafx.event.ActionEvent;
//        import javafx.event.EventHandler;
//        import javafx.scene.Group;
//        import javafx.scene.Node;
//        import javafx.scene.Scene;
//        import javafx.scene.control.Button;
//        import javafx.scene.control.Label;
//        import javafx.scene.control.Slider;
//        import javafx.scene.effect.DropShadow;
//        import javafx.scene.image.Image;
//        import javafx.scene.image.ImageView;
//        import javafx.scene.input.KeyCode;
//        import javafx.scene.media.AudioClip;
//        import javafx.scene.paint.Color;
//        import javafx.scene.text.Font;
//        import javafx.scene.text.FontWeight;
//        import javafx.scene.text.Text;
//        import javafx.scene.text.TextAlignment;
//        import javafx.stage.Stage;
//
//public class Game extends Application {
//
//    private static final int SQUARE_SIZE = 100;
//    private static final int MARGIN_X = 30;
//    private static final int MARGIN_Y = 30;
//    private static final int BOARD_WIDTH = 542;
//    private static final int BOARD_HEIGHT = 444;
//    private static final int BOARD_MARGIN = 71;
//    private static final int OBJECTIVE_WIDTH = 162;
//    private static final int OBJECTIVE_HEIGHT = 150;
//    private static final int OBJECTIVE_MARGIN_X = 100;
//    private static final int OBJECTIVE_MARGIN_Y = 20;
//    private static final int BOARD_Y = MARGIN_Y;
//    private static final int BOARD_X = MARGIN_X + (3 * SQUARE_SIZE) + SQUARE_SIZE + MARGIN_X;
//    private static final int PLAY_AREA_Y = BOARD_Y + BOARD_MARGIN;
//    private static final int PLAY_AREA_X = BOARD_X + BOARD_MARGIN;
//    private static final int GAME_WIDTH = BOARD_X + BOARD_WIDTH + MARGIN_X;
//    private static final int GAME_HEIGHT = 620;
//    private static final long ROTATION_THRESHOLD = 50; // Allow rotation every 50 ms
//
//    /* marker for unplaced tiles */
//    public static final char NOT_PLACED = 255;
//
//    /* node groups */
//    private final Group root = new Group();
//    private final Group gtiles = new Group();
//    private final Group solution = new Group();
//    private final Group board = new Group();
//    private final Group controls = new Group();
//    private final Group exposed = new Group();
//    private final Group objective = new Group();
//
//    private static String solutionString;
//
//    /* where to find media assets */
//    private static final String URI_BASE = "assets/";
//    private static final String BASEBOARD_URI = Game.class.getResource(URI_BASE + "baseboard.png").toString();
//
//    // TODO update the audio
//    /* Loop in public domain CC 0 http://www.freesound.org/people/oceanictrancer/sounds/211684/ */
//    private static final String LOOP_URI = Game.class.getResource(URI_BASE + "211684__oceanictrancer__classic-house-loop-128-bpm.wav").toString();
//    private AudioClip loop;
//
//    /* game variables */
//    private boolean loopPlaying = false;
//
//    /* the difficulty slider */
//    private final Slider difficulty = new Slider();
//
//    /* message on completion */
//    private final Text completionText = new Text("Well done!");
//
//    /* the state of the tiles */
//    char[] tileState = new char[6];   //  all off screen to begin with
//
//    /* The underlying game */
//    Dinosaurs dinosaursGame;
//
//    /* Define a drop shadow effect that we will appy to tiles */
//    private static DropShadow dropShadow;
//
//    /* Static initializer to initialize dropShadow */ {
//        dropShadow = new DropShadow();
//        dropShadow.setOffsetX(2.0);
//        dropShadow.setOffsetY(2.0);
//        dropShadow.setColor(Color.color(0, 0, 0, .4));
//    }
//
//    /* Graphical representations of tiles */
//    class GTile extends ImageView {
//        int tileID;
//
//
//        /**
//         * Construct a particular playing tile
//         *
//         * @param tile The letter representing the tile to be created.
//         */
//        GTile(char tile) {
//            if (tile > 'f' || tile < 'a') {
//                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
//            }
//            this.tileID = tile - 'a';
//            setFitHeight(2 * SQUARE_SIZE);
//            setFitWidth(SQUARE_SIZE);
//            setEffect(dropShadow);
//        }
//
//        /**
//         * Construct a playing tile, which is placed on the board at the start of the game,
//         * as a part of some challenges
//         *
//         * @param tile  The letter representing the tile to be created.
//         * @param orientation   The integer representation of the tile to be constructed
//         */
//        GTile(char tile, int orientation) {
//            if (tile > 'f' || tile < 'a') {
//                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
//            }
//            this.tileID = tile - 'a';
//            if (orientation%2 == 0) {
//                setFitHeight(2 * SQUARE_SIZE);
//                setFitWidth(SQUARE_SIZE);
//            }
//            else {
//                setFitHeight(SQUARE_SIZE);
//                setFitWidth(2*SQUARE_SIZE);
//            }
//            setImage(new Image(Game.class.getResource(URI_BASE + tile + "-" + (char)(orientation+'0') + ".png").toString()));
//            setEffect(dropShadow);
//        }
//
//        /**
//         * A constructor used to build the objective tile.
//         *
//         * @param tile The tile to be displayed (one of 80 objectives)
//         * @param x    The x position of the tile
//         * @param y    The y position of the tile
//         */
//        GTile(int tile, int x, int y) {
//            if (!(tile <= 80 && tile >= 1)) {
//                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
//            }
//
//            String t = String.format("%02d", tile);
//            setImage(new Image(Game.class.getResource(URI_BASE + t + ".png").toString()));
//            this.tileID = tile;
//            setFitHeight(OBJECTIVE_HEIGHT);
//            setFitWidth(OBJECTIVE_WIDTH);
//            setEffect(dropShadow);
//
//            setLayoutX(x);
//            setLayoutY(y);
//        }
//    }
//
//    /**
//     * This class extends Tile with the capacity for it to be dragged and dropped,
//     * and snap-to-grid.
//     */
//    class DraggableTile extends GTile {
//        int homeX, homeY;           // the position in the window where
//        // the tile should be when not on the board
//        double mouseX, mouseY;      // the last known mouse positions (used when dragging)
//        Image[] images = new Image[4];
//        int orientation;    // 0=North... 3=West
//        long lastRotationTime = System.currentTimeMillis(); // only allow rotation every ROTATION_THRESHOLD (ms)
//        // This caters for mice which send multiple scroll events per tick.
//
//        /**
//         * Construct a draggable tile
//         *
//         * @param tile The tile identifier ('a' - 'f')
//         */
//        DraggableTile(char tile) {
//            super(tile);
//            for (int i = 0; i < 4; i++) {
//                char idx = (char) (i + '0');
//                images[i] = new Image(Game.class.getResource(URI_BASE + tile + "-" + idx + ".png").toString());
//            }
//            setImage(images[0]);
//            orientation = 0;
//            tileState[tile - 'a'] = NOT_PLACED; // start out off board
//            homeX = MARGIN_X + ((tile - 'a') % 3) * SQUARE_SIZE;
//            setLayoutX(homeX);
//            homeY = OBJECTIVE_MARGIN_Y + OBJECTIVE_HEIGHT + MARGIN_Y + ((tile - 'a') / 3) * 2 * SQUARE_SIZE;
//            setLayoutY(homeY);
//
//            /* event handlers */
//            setOnScroll(event -> {            // scroll to change orientation
//                if (System.currentTimeMillis() - lastRotationTime > ROTATION_THRESHOLD){
//                    lastRotationTime = System.currentTimeMillis();
//                    hideCompletion();
//                    rotate();
//                    event.consume();
//                    checkCompletion();
//                }
//            });
//            setOnMousePressed(event -> {      // mouse press indicates begin of drag
//                mouseX = event.getSceneX();
//                mouseY = event.getSceneY();
//            });
//            setOnMouseDragged(event -> {      // mouse is being dragged
//                hideCompletion();
//                toFront();
//                double movementX = event.getSceneX() - mouseX;
//                double movementY = event.getSceneY() - mouseY;
//                setLayoutX(getLayoutX() + movementX);
//                setLayoutY(getLayoutY() + movementY);
//                mouseX = event.getSceneX();
//                mouseY = event.getSceneY();
//                event.consume();
//            });
//            setOnMouseReleased(event -> {     // drag is complete
//                snapToGrid();
//            });
//        }
//
//        /**
//         * Snap the tile to the nearest grid position (if it is over the grid)
//         */
//        private void snapToGrid() {
//
//            if (onBoard() && (!alreadyOccupied())) {
//                if ((getLayoutX() >= (PLAY_AREA_X - (SQUARE_SIZE / 2))) && (getLayoutX() < (PLAY_AREA_X + (SQUARE_SIZE / 2)))) {
//                    setLayoutX(PLAY_AREA_X);
//                } else if ((getLayoutX() >= PLAY_AREA_X + (SQUARE_SIZE / 2)) && (getLayoutX() < PLAY_AREA_X + 1.5 * SQUARE_SIZE)) {
//                    setLayoutX(PLAY_AREA_X + SQUARE_SIZE);
//                } else if ((getLayoutX() >= PLAY_AREA_X + 1.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 2.5 * SQUARE_SIZE)) {
//                    setLayoutX(PLAY_AREA_X + 2 * SQUARE_SIZE);
//                } else if ((getLayoutX() >= PLAY_AREA_X + 2.5 * SQUARE_SIZE) && (getLayoutX() < PLAY_AREA_X + 3.5 * SQUARE_SIZE)) {
//                    setLayoutX(PLAY_AREA_X + 3 * SQUARE_SIZE);
//                }
//
//                if ((getLayoutY() >= (PLAY_AREA_Y - (SQUARE_SIZE / 2))) && (getLayoutY() < (PLAY_AREA_Y + (SQUARE_SIZE / 2)))) {
//                    setLayoutY(PLAY_AREA_Y);
//                } else if ((getLayoutY() >= PLAY_AREA_Y + (SQUARE_SIZE / 2)) && (getLayoutY() < PLAY_AREA_Y + 1.5 * SQUARE_SIZE)) {
//                    setLayoutY(PLAY_AREA_Y + SQUARE_SIZE);
//                } else if ((getLayoutY() >= PLAY_AREA_Y + 1.5 * SQUARE_SIZE) && (getLayoutY() < PLAY_AREA_Y + 2.5 * SQUARE_SIZE)) {
//                    setLayoutY(PLAY_AREA_Y + 2 * SQUARE_SIZE);
//                }
//                setPosition();
//            } else {
//                snapToHome();
//            }
//            checkCompletion();
//        }
//
//
//        /**
//         * @return true if the tile is on the board
//         */
//        private boolean onBoard() {
//            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 3.5 * SQUARE_SIZE))
//                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
//        }
//
//        /**
//         * a function to check whether the current destination cell
//         * is already occupied by another tile
//         *
//         * @return true if the destination cell for the current tile
//         * is already occupied, and false otherwise
//         */
//        private boolean alreadyOccupied() {
//            int x = (int) (getLayoutX() + (SQUARE_SIZE / 2) - PLAY_AREA_X) / SQUARE_SIZE;
//            int y = (int) (getLayoutY() + (SQUARE_SIZE / 2) - PLAY_AREA_Y) / SQUARE_SIZE;
//
//            // it occupies two cells
//            int idx1 = y * 4 + x;
//            int idx2;
//
//            if (orientation%2 == 0)
//                idx2 = (y+1) * 4 + x;
//            else
//                idx2 = y * 4 + x + 1;
//
//            for (int i = 0; i < 6; i++) {
//                if (tileState[i] == NOT_PLACED)
//                    continue;
//
//                int tIdx1 = tileState[i] / 4;
//                int tIdx2;
//                int tOrn = tileState[i] % 4;
//
//                if (tOrn%2 == 0)
//                    tIdx2 = tIdx1 + 4;
//                else
//                    tIdx2 = tIdx1 + 1;
//
//                if (tIdx1 == idx1 || tIdx2 == idx1 || tIdx1 == idx2 || tIdx2 == idx2)
//                    return true;
//            }
//            return false;
//        }
//
//
//        /**
//         * Snap the tile to its home position (if it is not on the grid)
//         */
//        private void snapToHome() {
//            setLayoutX(homeX);
//            setLayoutY(homeY);
//            setFitHeight(2 * SQUARE_SIZE);
//            setFitWidth(SQUARE_SIZE);
//            setImage(images[0]);
//            orientation = 0;
//            tileState[tileID] = NOT_PLACED;
//        }
//
//
//        /**
//         * Rotate the tile by 90 degrees and update any relevant state
//         */
//        private void rotate() {
//            orientation = (orientation + 1) % 4;
//            setImage(images[(orientation)]);
//            setFitWidth((1 + (orientation % 2)) * SQUARE_SIZE);
//            setFitHeight((2 - (orientation % 2)) * SQUARE_SIZE);
//            toFront();
//            setPosition();
//        }
//
//
//        /**
//         * Determine the grid-position of the origin of the tile
//         * or 'NOT_PLACED' if it is off the grid, taking into account its rotation.
//         */
//        private void setPosition() {
//            int x = (int) (getLayoutX() - PLAY_AREA_X) / SQUARE_SIZE;
//            int y = (int) (getLayoutY() - PLAY_AREA_Y) / SQUARE_SIZE;
//            if (x < 0)
//                tileState[tileID] = NOT_PLACED;
//            else {
//                char val = (char) ((y * 4 + x) * 4 + orientation);
//                tileState[tileID] = val;
//            }
//        }
//
//
//        /**
//         * @return the mask placement represented as a string
//         */
//        public String toString() {
//            return "" + tileState[tileID];
//        }
//    }
//
//    /**
//     * Set up event handlers for the main game
//     *
//     * @param scene The Scene used by the game.
//     */
//    private void setUpHandlers(Scene scene) {
//        /* create handlers for key press and release events */
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.M) {
//                toggleSoundLoop();
//                event.consume();
//            } else if (event.getCode() == KeyCode.Q) {
//                Platform.exit();
//                event.consume();
//            } else if (event.getCode() == KeyCode.SLASH) {
//                solution.setOpacity(1.0);
//                gtiles.setOpacity(0);
//                event.consume();
//            }
//        });
//        scene.setOnKeyReleased(event -> {
//            if (event.getCode() == KeyCode.SLASH) {
//                solution.setOpacity(0);
//                gtiles.setOpacity(1.0);
//                event.consume();
//            }
//        });
//    }
//
//
//    /**
//     * Set up the sound loop (to play when the 'M' key is pressed)
//     */
//    private void setUpSoundLoop() {
//        try {
//            loop = new AudioClip(LOOP_URI);
//            loop.setCycleCount(AudioClip.INDEFINITE);
//        } catch (Exception e) {
//            System.err.println(":-( something bad happened (" + LOOP_URI + "): " + e);
//        }
//    }
//
//
//    /**
//     * Turn the sound loop on or off
//     */
//    private void toggleSoundLoop() {
//        if (loopPlaying)
//            loop.stop();
//        else
//            loop.play();
//        loopPlaying = !loopPlaying;
//    }
//
//    /**
//     * Set up the group that represents the solution (and make it transparent)
//     *
//     * @param solution The solution as an array of chars.
//     */
//    private void makeSolution(String solution) {
//        this.solution.getChildren().clear();
//
//        if (solution.length() == 0) {
//            return;
//        }
//
//        if (solution.length() != 24) {
//            throw new IllegalArgumentException("Solution incorrect length: " + solution);
//        }
//
//        solutionString = solution;
//        for (int i = 0; i < solution.length(); i+=4) {
//            GTile gtile = new GTile(solution.charAt(i), Tile.placementToOrientation(solution.substring(i,i+4)).ordinal());
//            int x = solution.charAt(i+1) - '0';
//            int y = solution.charAt(i+2) - '0';
//
//            gtile.setLayoutX(PLAY_AREA_X + (x * SQUARE_SIZE));
//            gtile.setLayoutY(PLAY_AREA_Y + (y * SQUARE_SIZE));
//
//            this.solution.getChildren().add(gtile);
//        }
//        this.solution.setOpacity(0);
//    }
//
//
//    /**
//     * Set up the group that represents the places that make the board
//     */
//    private void makeBoard() {
//        board.getChildren().clear();
//
//        ImageView baseboard = new ImageView();
//        baseboard.setImage(new Image(BASEBOARD_URI));
//        baseboard.setFitWidth(BOARD_WIDTH);
//        baseboard.setFitHeight(BOARD_HEIGHT);
//        baseboard.setLayoutX(BOARD_X);
//        baseboard.setLayoutY(BOARD_Y);
//        board.getChildren().add(baseboard);
//
//        board.toBack();
//    }
//
//
//    /**
//     * Set up each of the six tiles
//     */
//    private void makeTiles() {
//        gtiles.getChildren().clear();
//        for (char m = 'a'; m <= 'f'; m++) {
//            gtiles.getChildren().add(new DraggableTile(m));
//        }
//    }
//
//
//    /**
//     * Add the objective to the board
//     */
//    private void addObjectiveToBoard() {
//        objective.getChildren().clear();
//        objective.getChildren().add(new GTile(dinosaursGame.getObjective().getProblemNumber(), OBJECTIVE_MARGIN_X, OBJECTIVE_MARGIN_Y));
//    }
//
//
//    /**
//     * Check game completion and update status
//     */
//    private void checkCompletion() {
//        String state = new String("");
//        for (int i = 0; i < 6; i++) {
//            if (tileState[i] == NOT_PLACED)
//                return;
//            state = state +
//                    (char)(i + 'a') +
//                    (char)(((tileState[i]/4)%4)+'0') +
//                    (char)(((tileState[i]/4)/4)+'0') +
//                    (Orientation.values()[tileState[i]%4].toChar());
//        }
//
//        if (state.equals(solutionString))
//            showCompletion();
//        else
//            return;
//    }
//
//
//    /**
//     * Put all of the tiles back in their home position
//     */
//    private void resetPieces() {
//        gtiles.toFront();
//        for (Node n : gtiles.getChildren()) {
//            ((DraggableTile) n).snapToHome();
//        }
//    }
//
//
//    /**
//     * Create the controls that allow the game to be restarted and the difficulty
//     * level set.
//     */
//    private void makeControls() {
//        Button button = new Button("Restart");
//        button.setLayoutX(BOARD_X + BOARD_MARGIN + 240);
//        button.setLayoutY(GAME_HEIGHT - 55);
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                newGame();
//            }
//        });
//        controls.getChildren().add(button);
//
//        difficulty.setMin(1);
//        difficulty.setMax(4);
//        difficulty.setValue(0);
//        difficulty.setShowTickLabels(true);
//        difficulty.setShowTickMarks(true);
//        difficulty.setMajorTickUnit(1);
//        difficulty.setMinorTickCount(1);
//        difficulty.setSnapToTicks(true);
//
//        difficulty.setLayoutX(BOARD_X + BOARD_MARGIN + 70);
//        difficulty.setLayoutY(GAME_HEIGHT - 50);
//        controls.getChildren().add(difficulty);
//
//        final Label difficultyCaption = new Label("Difficulty:");
//        difficultyCaption.setTextFill(Color.GREY);
//        difficultyCaption.setLayoutX(BOARD_X + BOARD_MARGIN);
//        difficultyCaption.setLayoutY(GAME_HEIGHT - 50);
//        controls.getChildren().add(difficultyCaption);
//    }
//
//
//    /**
//     * Create the message to be displayed when the player completes the puzzle.
//     */
//    private void makeCompletion() {
//        completionText.setFill(Color.BLACK);
//        completionText.setEffect(dropShadow);
//        completionText.setCache(true);
//        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
//        completionText.setLayoutX(20);
//        completionText.setLayoutY(375);
//        completionText.setTextAlignment(TextAlignment.CENTER);
//        root.getChildren().add(completionText);
//    }
//
//
//    /**
//     * Show the completion message
//     */
//    private void showCompletion() {
//        completionText.toFront();
//        completionText.setOpacity(1);
//    }
//
//
//    /**
//     * Hide the completion message
//     */
//    private void hideCompletion() {
//        completionText.toBack();
//        completionText.setOpacity(0);
//    }
//
//
//    /**
//     * Start a new game, resetting everything as necessary
//     */
//    private void newGame() {
//        try {
//            hideCompletion();
//            dinosaursGame = new Dinosaurs((int) difficulty.getValue()-1);
//            String [] resSet = {""};
//            String sol = dinosaursGame.getSolutions().toArray(resSet)[0];
//            if (sol != null)
//                makeSolution(sol);
//            makeTiles();
//            addObjectiveToBoard();
//        } catch (IllegalArgumentException e) {
//            System.err.println("Uh oh. " + e);
//            e.printStackTrace();
//            Platform.exit();
//        }
//        resetPieces();
//    }
//
//
//    @Override
//    public void start(Stage primaryStage) {
//
//        primaryStage.setTitle("DINOSAURS - Mystic Islands");
//        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);
//
//        root.getChildren().add(gtiles);
//        root.getChildren().add(board);
//        root.getChildren().add(solution);
//        root.getChildren().add(controls);
//        root.getChildren().add(exposed);
//        root.getChildren().add(objective);
//
//        setUpHandlers(scene);
//        setUpSoundLoop();
//        makeBoard();
//        makeControls();
//        makeCompletion();
//
//        newGame();
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}
