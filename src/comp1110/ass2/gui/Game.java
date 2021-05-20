package comp1110.ass2.gui;

import comp1110.ass2.Azul;
import comp1110.ass2.Player;
import comp1110.ass2.Tile;
import comp1110.ass2.board.Storage;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Factory;
import java.util.ArrayDeque;
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
import javafx.scene.control.CheckBox;
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
 * <p>
 * Some testing data A21Ma00b01c02d03e04a11b12c13e21a22c30b34a44S2b2FaaaaB24Ma00c02d03e04e10a11b12c13d14d20a22b23e32b34b40S4d2Fdd
 * AF1bbbe2abde3cdee4bcceCadfB1915161614D1103100920 A1b4 B3e2 A4cF
 */
public class Game extends Application {

    /* comp1110.ass2.board layout */
    private static final int BOARD_WIDTH = 1200;
    private static final int BOARD_HEIGHT = 700;
    private static final int PLAYER_BOARD_WIDTH = 500;
    private static final int PLAYER_BOARD_HEIGHT = 500;
    private static final int MARGIN_X = 30;
    private static final int MARGIN_Y = 30;

    private static final int SQUARE_SIZE = 40;
    private static final int OBJECTIVE_WIDTH = 162;
    private static final int OBJECTIVE_HEIGHT = 150;
    private static final int OBJECTIVE_MARGIN_X = 100;
    private static final int OBJECTIVE_MARGIN_Y = 20;


    private final Group root = new Group();
    private final Group gTiles = new Group();
    private final Group leftBoard = new Group();
    private final Group rightBoard = new Group();
    private final Group common = new Group();
    private final Group score = new Group();

    private final Group controls = new Group();

    private final Group objective = new Group();


    private TextField playerTextField;
    private TextField boardTextField;
    private TextField moveTextField;

    private final ArrayList<Color> colorArrayList = new ArrayList<>(
            Arrays.asList(Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.RED)
    );
    private comp1110.ass2.Game game = new comp1110.ass2.Game();
    private comp1110.ass2.Game azulGame;


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

    private final CheckBox[] checkboxes = new CheckBox[2];
    /* Define a drop shadow effect that we will appy to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /* Graphical representations of tiles */
    class GTile extends Rectangle {

        char tileID;
        Color color;

        /**
         * Construct a particular playing tile
         *
         * @param tile The letter representing the tile to be created.
         */
        GTile(char tile) {
            if (tile > 'f' || tile < 'a') {
                throw new IllegalArgumentException("Bad tile: \"" + tile + "\"");
            }
            tileID = tile;

//            setFitHeight(SQUARE_SIZE);
//            setFitWidth(SQUARE_SIZE);
//            setImage(new Image(Game.class.getResource(URI_BASE + tile + "-" + (char)(orientation+'0') + ".png").toString()));

            color = switch (tile) {
                case 'a' -> Color.BLUE;
                case 'b' -> Color.GREEN;
                case 'c' -> Color.ORANGE;
                case 'd' -> Color.PURPLE;
                case 'e' -> Color.RED;
                case 'f' -> Color.CYAN;
                default -> Color.GREY;
            };
            setFill(color);
            setHeight(SQUARE_SIZE);
            setWidth(SQUARE_SIZE);
            setEffect(dropShadow);
        }
    }

    /**
     * This class extends Tile with the capacity for it to be dragged and dropped, and
     * snap-to-grid.
     */
    class DraggableTile extends GTile {

        int homeX, homeY;           // the position in the window where
        // the tile should be when not on the board
        double mouseX, mouseY;      // the last known mouse positions (used when dragging)
        //        Image[] images = new Image[4];
        int homeFrom;

        /**
         * Construct a draggable tile
         *
         * @param tile The tile identifier ('a' - 'f')
         */
        DraggableTile(char tile, int from, int x, int y) {
            super(tile);
//            for (int i = 0; i < 4; i++) {
//                char idx = (char) (i + '0');
//                images[i] = new Image(Game.class.getResource(URI_BASE + tile + "-" + idx + ".png").toString());
//            }
//            setImage(images[0]);
//            tileState[tile - 'a'] = NOT_PLACED; // start out off board
//            homeX = MARGIN_X + ((tile - 'a') % 3) * SQUARE_SIZE;
            homeX = x;
            setLayoutX(homeX);
//            homeY = OBJECTIVE_MARGIN_Y + OBJECTIVE_HEIGHT + MARGIN_Y + ((tile - 'a') / 3) * 2 * SQUARE_SIZE;
            homeY = y;
            setLayoutY(homeY);

            homeFrom = from;

            /* event handlers */
            setOnMousePressed(event -> {      // mouse press indicates begin of drag
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseDragged(event -> {      // mouse is being dragged
//                hideCompletion();
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {     // drag is complete
                System.out.println("1");
                try {
                    snapToGrid(event.getSceneX(), event.getSceneY());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


        }

        /**
         * Snap the tile to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid(double x, double y) throws InterruptedException {
            System.out.println(x + " " + y);

            String move = azulGame.getTurn();
            if (homeFrom < 0) {
                // given x and y
                // generate move
                // PLAYER + row + column
                int row = (homeY - 180) / 45;
                move += row;
                // x + 240 + 10 + 45 * j, 180 + 45 * i
                if (y - homeY >= 0 && y - homeY <= 40) {
                    for (int col = 0; col < 5; col++) {
                        if (azulGame.getTurn().equals("A") &&
                                x - (10 + 240 + 45 * col) <= 40 && x >= 10 + 240) {
                            move += col;
                            break;
                        }
                        if (azulGame.getTurn().equals("B") &&
                                x - (700 + 10 + 240 + 45 * col) <= 40 && x >= 700 + 10 + 240) {
                            move += col;
                            break;
                        }
                    }
                }
                System.out.println(move);
                String[] gameState = azulGame.rebuildStateString();
                if (move.length() == 3 && azulGame.isMoveValid(gameState, move)) {
                    String currentTurn = azulGame.getTurn();
                    gameState = azulGame.applyMove(gameState, move);
                    System.out.println("A apply move " + gameState[0]);
                    System.out.println("A apply move " + gameState[1]);

                    gameState = azulGame.nextRound(gameState);
                    System.out.println("A next round " + gameState[0]);
                    System.out.println("A next round " + gameState[1]);
                    makeFCTiles();
                    makeOtherTiles();
                    makeScore();

                    // AI
                    int index  = (azulGame.getTurn().equals("A")) ? 0 : 1;
                    if(checkboxes[index].isSelected() && !azulGame.getTurn().equals(currentTurn) ) {
                        // until change turn
                        currentTurn = azulGame.getTurn();
                        while(azulGame.getTurn().equals(currentTurn)) {
                            System.out.println("kale");
                            System.out.println(currentTurn);
                            String action = azulGame.generateAction(gameState);
                            if(action != null) {
                                System.out.println(action);
                                gameState = azulGame.applyMove(gameState, action);
                                System.out.println("B apply move " + gameState[0]);
                                System.out.println("B apply move " + gameState[1]);
                            }
                            gameState = azulGame.nextRound(gameState);
                            System.out.println("B next round " + gameState[0]);
                            System.out.println("B next round " + gameState[1]);
                            makeFCTiles();
                            makeOtherTiles();
                            makeScore();
                        }
                    }

                } else {
                    snapToHome();
                }

            } else {
                // given x and y
                // generate move
                // PLAYER + Factory + Color + row
                move += (homeFrom == 100) ? String.valueOf('C') : String.valueOf(homeFrom);
                move += tileID;

                // x + 10 + 45 * i, 430,
                if (y >= 430 && y <= 430 + 40) {
                    if (azulGame.getTurn().equals("A") && x >= 10 && x <= 10 + 6 * 45 + 40) {
                        move += "F";
                    }
                    if (azulGame.getTurn().equals("B") && x >= 700 + 10
                            && x <= 700 + 10 + 6 * 45 + 40) {
                        move += "F";
                    }
                } else {
                    //  x = x + 10 + 45 * j, y = 180 + 45 * i,
                    for (int row = 0; row < 5; row++) {
                        if (y - (180 + 45 * row) <= 40 && y >= 180) {
                            // A
                            if (azulGame.getTurn().equals("A") && x >= (10 + 45 * (4 - row))
                                    && x <= 10 + 45 * 4 + 40) {
                                move += row;
                            }
                            // B
                            if (azulGame.getTurn().equals("B") && x >= (700 + 10 + 45 * (4 - row))
                                    && x <= 700 + 10 + 45 * 4 + 40) {
                                move += row;
                            }
                            break;
                        }
                    }
                }
                System.out.println(move);
                String[] gameState = azulGame.rebuildStateString();
                if (move.length() == 4 && azulGame.isMoveValid(gameState, move)) {
                    String currentTurn = azulGame.getTurn();
                    gameState = azulGame.applyMove(gameState, move);
                    System.out.println("A apply move " + gameState[0]);
                    System.out.println("A apply move " + gameState[1]);

                    gameState = azulGame.nextRound(gameState);
                    System.out.println("A next round " + gameState[0]);
                    System.out.println("A next round " + gameState[1]);
                    makeFCTiles();
                    makeOtherTiles();
                    makeScore();

                    // AI
                    int index  = (azulGame.getTurn().equals("A")) ? 0 : 1;
                    if(checkboxes[index].isSelected() && !azulGame.getTurn().equals(currentTurn) ) {
                        // until change turn
                        currentTurn = azulGame.getTurn();
                        while(azulGame.getTurn().equals(currentTurn)) {
                            System.out.println("kale");
                            System.out.println(currentTurn);
                            String action = azulGame.generateAction(gameState);
                            if(action != null) {
                                System.out.println(action);
                                gameState = azulGame.applyMove(gameState, action);
                                System.out.println("B apply move " + gameState[0]);
                                System.out.println("B apply move " + gameState[1]);
                            }
                            gameState = azulGame.nextRound(gameState);
                            System.out.println("B next round " + gameState[0]);
                            System.out.println("B next round " + gameState[1]);
                            makeFCTiles();
                            makeOtherTiles();
                            makeScore();
                        }
                    }

                } else {
                    snapToHome();
                }
            }



            // BF0bcbe1cedb2dddc3abab4bedaCfB0504050501D0706070612
            // A22Md00c01c10d11e12e20b21e31b40S3c24a2FB0Mc00a01b02d10a12a20d21e40S3b1F

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
        }

        /**
         * @return true if the tile is on the board
         */
//        private boolean onBoard() {
//            return getLayoutX() > (PLAY_AREA_X - (SQUARE_SIZE / 2)) && (getLayoutX() < (PLAY_AREA_X + 3.5 * SQUARE_SIZE))
//                    && getLayoutY() > (PLAY_AREA_Y - (SQUARE_SIZE / 2)) && (getLayoutY() < (PLAY_AREA_Y + 2.5 * SQUARE_SIZE));
//        }

        /**
         * a function to check whether the current destination cell
         * is already occupied by another tile
         *
         * @return true if the destination cell for the current tile
         * is already occupied, and false otherwise
         */
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


        /**
         * Snap the tile to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
//            setFitHeight(2 * SQUARE_SIZE);
//            setFitWidth(SQUARE_SIZE);
//            setImage(images[0]);
//            orientation = 0;
//            tileState[tileID] = NOT_PLACED;
        }

        /**
         * Determine the grid-position of the origin of the tile
         * or 'NOT_PLACED' if it is off the grid, taking into account its rotation.
         */
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

        /**
         * @return the mask placement represented as a string
         */
//        public String toString() {
//            return "" + tileState[tileID];
//        }
    }


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

//            String substringF = boardState.substring(indexF, indexC);
//            decodeFactories(substringF, factories);
//            String substringC = boardState.substring(indexC, indexB2);
//            decodeCentre(substringC, centre);
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
            char code = positions.charAt(i);
            i = i + 1;
            int row = positions.charAt(i) - 48;
            i = i + 1;
            int column = positions.charAt(i) - 48;
            mosaic[row][column].setFill(fillColor(code));
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
        for (Rectangle rectangle : floor) {
            rectangle.setFill(Color.LIGHTGREY);
        }

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
            case 'f' -> Color.CYAN;
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
     * Set up the group that represents the places that make the board
     */
    private void makeBoard(Group board, int x, char player) {
        board.getChildren().clear();

//        ImageView baseboard = new ImageView();
//        baseboard.setImage(new Image(BASEBOARD_URI));
//        baseboard.setFitWidth(BOARD_WIDTH);
//        baseboard.setFitHeight(BOARD_HEIGHT);
//        baseboard.setLayoutX(BOARD_X);
//        baseboard.setLayoutY(BOARD_Y);

        // box
        Text title = new Text(x + 50, 155, "Player " + player + " board with Score ");
        title.setFont(Font.font(32));
        title.setFill(Color.BLACK);
        board.getChildren().add(title);

        Rectangle outerBox = new Rectangle(x + 5, 160, 490, 350);
        outerBox.setFill(Color.BLACK);
        board.getChildren().add(outerBox);

        Rectangle innerBox = new Rectangle(x + 8, 163, 484, 344);
        innerBox.setFill(Color.WHITE);
        board.getChildren().add(innerBox);

        Rectangle[][] storage = (player == 'A') ? storageA : storageB;
        Rectangle[][] mosaic = (player == 'A') ? mosaicA : mosaicB;
        Rectangle[] floor = (player == 'A') ? floorA : floorB;
        ////////////////////// STORAGE //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j <= 3) {
                    continue;
                }
                Rectangle tileRect = new Rectangle(x + 10 + 45 * j, 180 + 45 * i, SQUARE_SIZE,
                        SQUARE_SIZE);
                tileRect.setFill(Color.LIGHTGREY);
                storage[i][j] = tileRect;
                board.getChildren().add(tileRect);
            }
        }
        Text storageTitle = new Text(x + 100, 420, "Storage");
        storageTitle.setFill(Color.BLACK);
        board.getChildren().add(storageTitle);

        ////////////////////// MOSAIC //////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Rectangle tileRect = new Rectangle(x + 240 + 10 + 45 * j, 180 + 45 * i, SQUARE_SIZE,
                        SQUARE_SIZE);
//                tileRect.setFill(colorArrayList.get((j - i + 5) % 5));
//                tileRect.setOpacity(0.2);
                tileRect.setFill(Color.LIGHTGREY);
                mosaic[i][j] = tileRect;
                board.getChildren().add(tileRect);
            }
        }
        Text mosaicTitle = new Text(x + 340, 420, "Mosaic");
        mosaicTitle.setFill(Color.BLACK);
        board.getChildren().add(mosaicTitle);

        ////////////////////// FLOOR //////////////////////
        for (int i = 0; i < 7; i++) {
            Rectangle tileRect = new Rectangle(x + 10 + 45 * i, 430, SQUARE_SIZE, SQUARE_SIZE);
            tileRect.setFill(Color.LIGHTGREY);
            floor[i] = tileRect;
            board.getChildren().add(tileRect);
        }
        Text FloorText = new Text(x + 150, 490, "Floor");
        FloorText.setFill(Color.BLACK);
        board.getChildren().add(FloorText);

        // to back
        board.toBack();
    }

    /**
     * Set up the group that represents the places that make the common
     */
    private void makeCommon() {
        //////////////////////////// AI CHECKBOX ///////////////////////////////////
        CheckBox playerACheckBox = new CheckBox();
        CheckBox playerBCheckBox = new CheckBox();
        playerACheckBox.setLayoutX(1100);
        playerACheckBox.setLayoutY(30);
        playerACheckBox.setText("AI for A");
        playerBCheckBox.setLayoutX(1100);
        playerBCheckBox.setLayoutY(60);
        playerBCheckBox.setText("AI for B");
        checkboxes[0] = playerACheckBox;
        checkboxes[1] = playerBCheckBox;

        common.getChildren().add(playerACheckBox);
        common.getChildren().add(playerBCheckBox);


        //////////////////////////// FACTORIES /////////////////////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Rectangle factoryRect = new Rectangle(10 + i * 95 + j * 45, 10 + k * 45,
                            SQUARE_SIZE, SQUARE_SIZE);
                    factoryRect.setFill(Color.LIGHTGREY);
                    common.getChildren().add(factoryRect);
//                    factories[j][i * 2 + k] = factoriesRect;
                }
            }
            Text text = new Text(45 + i * 95, 116, String.valueOf(i));
            text.setFill(Color.BLACK);
            common.getChildren().add(text);
        }

        //////////////////////////// CENTRE /////////////////////////////////////
//        Rectangle outerCentre = new Rectangle(500, 15, 100, 100);
//        outerCentre.setFill(Color.BLACK);
//        common.getChildren().add(outerCentre);
//        Rectangle innerCentre = new Rectangle(505, 20, 90, 90);
//        innerCentre.setFill(Color.WHITE);
//        common.getChildren().add(innerCentre);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                Rectangle centreRect = new Rectangle(710 + i * 45, 10 + j * 45, SQUARE_SIZE,
                        SQUARE_SIZE);
                centreRect.setFill(Color.LIGHTGREY);
                common.getChildren().add(centreRect);
//                centre[i][j] = centreRect;
            }
        }
        Text centreTitle = new Text(920, 116, "Centre");
        centreTitle.setFill(Color.BLACK);
        common.getChildren().add(centreTitle);

        //////////////////////////// BAG /////////////////////////////////////
        Rectangle outerBag = new Rectangle(500, 130, 200, 200);
        outerBag.setFill(Color.BLACK);
        common.getChildren().add(outerBag);
        Rectangle innerBag = new Rectangle(505, 135, 190, 190);
        innerBag.setFill(Color.WHITE);
        common.getChildren().add(innerBag);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle bagRect = new Rectangle(510 + i * 18, 140 + j * 18, 17, 17);
                bagRect.setFill(Color.LIGHTGREY);
                common.getChildren().add(bagRect);
                bag[i][j] = bagRect;
            }
        }
        Text bagTitle = new Text(590, 125, "Bag");
        bagTitle.setFill(Color.BLACK);
        common.getChildren().add(bagTitle);

        //////////////////////////// DISCARD /////////////////////////////////////
        Rectangle outerDiscard = new Rectangle(500, 345, 200, 200);
        outerDiscard.setFill(Color.BLACK);
        common.getChildren().add(outerDiscard);
        Rectangle innerDiscard = new Rectangle(505, 350, 190, 190);
        innerDiscard.setFill(Color.WHITE);
        common.getChildren().add(innerDiscard);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle discardRect = new Rectangle(510 + i * 18, 355 + j * 18, 17, 17);
                discardRect.setFill(Color.LIGHTGREY);
                common.getChildren().add(discardRect);
                discard[i][j] = discardRect;
            }
        }
        Text discardTitle = new Text(590, 340, "Discard");
        discardTitle.setFill(Color.BLACK);
        common.getChildren().add(discardTitle);

        // make common
        common.toBack();
    }

    /**
     * Set up each of the six tiles
     */
    private void makeFCTiles() {
        gTiles.getChildren().clear();
        Centre centre = azulGame.getCommon().getCentre();
        Factory[] factories = azulGame.getCommon().getFactories();
        Player[] players = azulGame.getPlayers();

        for (int i = 0; i < factories.length; i++) {
            ArrayList<Tile> tiles = factories[i].getTiles();
            for (int j = 0; j < tiles.size(); j++) {
                int col = j % 2;
                int row = j / 2;
                int x = 10 + i * 95 + col * 45;
                int y = 10 + row * 45;
                gTiles.getChildren().add(new DraggableTile(tiles.get(j).getColorCode(), i, x, y));
            }
        }

        ArrayList<Tile> centreTiles = centre.getTiles();
        for (int i = 0; i < centreTiles.size(); i++) {
            int col = i % 8;
            int row = i / 8;
            int x = 710 + col * 45;
            int y = 10 + row * 45;
            gTiles.getChildren().add(new DraggableTile(centreTiles.get(i).getColorCode(),
                    100, x, y));
        }

        if (centre.hasFirstPlayerTile()) {
            gTiles.getChildren().add(new DraggableTile(centre.getFirstPlayerTile().getColorCode(),
                    100, 710 + 7 * 45, 10 + 1 * 45));
        }

        for (Player player : players) {
            int from = 'A' - player.getId() - 1;
            int prefix = switch (from) {
                case -1 -> 0;
                case -2 -> 700;
                default -> 300;
            };

            ArrayList<ArrayDeque<Tile>> storageTiles = player.getBoard().getStorage().getTriangle();
            for (int row = 0; row < storageTiles.size(); row++) {
                int size = storageTiles.get(row).size();
                if (size != 0) {
                    char code = storageTiles.get(row).peek().getColorCode();
                    for (int i = 0; i < size; i++) {
                        // x + 10 + 45 * j, 180 + 45 * i,
                        gTiles.getChildren().add(new DraggableTile(code, from,
                                prefix + 10 + 45 * (4 - row + i), 180 + 45 * row));
                    }
                }
            }
        }

        gTiles.toFront();
    }

    private void makeOtherTiles() {
        for (Player player : azulGame.getPlayers()) {
            Rectangle[][] mosaic = (player.getId() - 'A' == 0) ? mosaicA : mosaicB;
//            Rectangle[][] storage = (player.getId() - 'A' == 0) ? storageA : storageB;
            Rectangle[] floor = (player.getId() - 'A' == 0) ? floorA : floorB;

            String mosaicString = "M" + player.getBoard().getMosaic().toString();
//            String storageString = "S" + player.getBoard().getStorage().toString();
            String floorString = "F" + player.getBoard().getFloor().toString();
            decodeMosaic(mosaicString, mosaic);
//            decodeStorage(storageString, storage);
            decodeFloor(floorString, floor);
        }

        decodeBag("B" + azulGame.getCommon().getBag().toString(), bag);
        decodeDiscard("D" + azulGame.getCommon().getDiscard().toString(), discard);
    }

    private void makeScore() {
        score.getChildren().clear();
        // x + 150, 155,
        String playerAScore = azulGame.getPlayers()[0].getBoard().getScore().toString();
        String playerBScore = azulGame.getPlayers()[1].getBoard().getScore().toString();
        String turn = azulGame.getTurn();

        Text playerAScoreText = new Text(0 + 420, 155, playerAScore);
        playerAScoreText.setFont(Font.font(32));
        playerAScoreText.setFill(Color.BLACK);

        Text playerBScoreText = new Text(700 + 420, 155, playerBScore);
        playerBScoreText.setFont(Font.font(32));
        playerBScoreText.setFill(Color.BLACK);

        Text turnText = new Text(500, 30, turn);
        turnText.setFont(Font.font(32));
        turnText.setFill(Color.BLACK);

        score.getChildren().add(playerAScoreText);
        score.getChildren().add(playerBScoreText);
        score.getChildren().add(turnText);
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
    }

    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
//            hideCompletion();
//            dinosaursGame = new Dinosaurs((int) difficulty.getValue()-1);
            azulGame = new comp1110.ass2.Game();
//            String [] resSet = {""};
//            String sol = dinosaursGame.getSolutions().toArray(resSet)[0];
//            if (sol != null)
//                makeSolution(sol);
            azulGame.refillFactories(azulGame.rebuildStateString());
            System.out.println(azulGame.rebuildStateString()[0]);
            System.out.println(azulGame.rebuildStateString()[1]);
            makeFCTiles();
            makeOtherTiles();
            makeScore();
        } catch (IllegalArgumentException e) {
            System.err.println("Uh oh. " + e);
            e.printStackTrace();
            Platform.exit();
        }
//        resetPieces();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Azul Viewer");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

        root.getChildren().add(gTiles);
        root.getChildren().add(leftBoard);
        root.getChildren().add(rightBoard);
        root.getChildren().add(common);
//        root.getChildren().add(solution);
        root.getChildren().add(controls);
//        root.getChildren().add(exposed);
//        root.getChildren().add(objective);
        root.getChildren().add(score);

//        setUpHandlers(scene);
//        setUpSoundLoop();
        makeBoard(leftBoard, 0, 'A');
        makeBoard(rightBoard, 700, 'B');
        makeCommon();
        makeControls();
//        makeCompletion();

        newGame();

//        old stuff
//        changeState();
//        root.getChildren().add(controls);
//        makeControls();
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
