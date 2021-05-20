package comp1110.ass2.gui;

import comp1110.ass2.Player;
import comp1110.ass2.Tile;
import comp1110.ass2.board.Score;
import comp1110.ass2.common.Centre;
import comp1110.ass2.common.Factory;
import java.util.ArrayDeque;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Author: Xinjie Wang, Jiaan Guo, Xiang Lu
 * <p>
 * Some code adapt from third party code Dinosaurs to make tiles draggable. Third Party: Dinosaurs
 * (https://gitlab.cecs.anu.edu.au/comp1110/dinosaurs)
 */
public class Game extends Application {

    /* comp1110.ass2.board layout */
    private static final int BOARD_WIDTH = 1280;
    private static final int BOARD_HEIGHT = 768;

    private static final int SQUARE_SIZE = 40;
    private static final int SQUARE_GAP = 5;
    private static final int SQUARE_WITH_GAP = SQUARE_SIZE + SQUARE_GAP; // 45
    private static final int LEFT_PADDING = 10;
    private static final int LEFT_BASE = 0;
    private static final int LEFT_BASE_WITH_PADDING = LEFT_BASE + LEFT_PADDING;
    private static final int MIDDLE_BASE = 500;
    private static final int MIDDLE_BASE_WITH_PADDING = MIDDLE_BASE + LEFT_PADDING;
    private static final int RIGHT_BASE = 700;
    private static final int RIGHT_BASE_WITH_PADDING = RIGHT_BASE + LEFT_PADDING;

    private static final int LENGTH = 5;

    private final Group root = new Group();
    private final Group gTiles = new Group();
    private final Group leftBoard = new Group();
    private final Group rightBoard = new Group();
    private final Group common = new Group();
    private final Group score = new Group();

    private final Group controls = new Group();

    private comp1110.ass2.Game azulGame;

    private final Rectangle[][] storageA = new Rectangle[5][5];
    private final Rectangle[][] storageB = new Rectangle[5][5];
    private final Rectangle[][] mosaicA = new Rectangle[5][5];
    private final Rectangle[][] mosaicB = new Rectangle[5][5];
    private final Rectangle[] floorA = new Rectangle[7];
    private final Rectangle[] floorB = new Rectangle[7];

    private final Rectangle[][] bag = new Rectangle[10][10];
    private final Rectangle[][] discard = new Rectangle[10][10];

    private CheckBox checkbox;

    /* message on completion */
    private final Text completionText = new Text("WIN!");

    /* Define a drop shadow effect that we will appy to tiles */
    private static DropShadow dropShadow;

    /* Static initializer to initialize dropShadow */ {
        dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, .4));
    }

    /* Graphical representations of tiles */
    static class GTile extends Rectangle {

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

        int homeX, homeY;
        double mouseX, mouseY;
        int homeFrom;

        /**
         * Construct a draggable tile
         *
         * @param tile The tile identifier ('a' - 'f')
         */
        DraggableTile(char tile, int from, int x, int y) {
            super(tile);
            homeX = x;
            setLayoutX(homeX);
            homeY = y;
            setLayoutY(homeY);

            homeFrom = from;

            /* event handlers */
            setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });
            setOnMouseDragged(event -> {
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });
            setOnMouseReleased(event -> {

                snapToGrid(event.getSceneX(), event.getSceneY());

            });
        }

        /**
         * Snap the tile to the nearest grid position (if it is over the grid)
         */
        private void snapToGrid(double x, double y) {
            String move = "" + azulGame.getTurn();
            // from storage to mosaic
            if (homeFrom < 0) {
                int row = (homeY - 180) / SQUARE_WITH_GAP;
                move += row;
                if (y - homeY >= 0 && y - homeY <= SQUARE_SIZE) {
                    for (int col = 0; col < LENGTH; col++) {
                        if (azulGame.getTurn().equals("A") &&
                                x - (LEFT_BASE_WITH_PADDING + 240 + SQUARE_WITH_GAP * col)
                                        <= SQUARE_SIZE
                                && x >= LEFT_BASE_WITH_PADDING + 240) {
                            move += col;
                            break;
                        }
                        if (azulGame.getTurn().equals("B") &&
                                x - (RIGHT_BASE_WITH_PADDING + 240 + SQUARE_WITH_GAP * col)
                                        <= SQUARE_SIZE
                                && x >= RIGHT_BASE_WITH_PADDING + 240) {
                            move += col;
                            break;
                        }
                    }
                }
                decodeAndMove(move, 3);
                // from factory / centre to storage, 100 is a special indicator for centre
            } else {
                move += (homeFrom == 100) ? String.valueOf('C') : String.valueOf(homeFrom);
                move += tileID;

                if (y >= 430 && y <= 430 + SQUARE_SIZE) {
                    if (azulGame.getTurn().equals("A") && x >= LEFT_BASE_WITH_PADDING
                            && x <= LEFT_BASE_WITH_PADDING + 6 * SQUARE_WITH_GAP + SQUARE_SIZE) {
                        move += "F";
                    }
                    if (azulGame.getTurn().equals("B") && x >= RIGHT_BASE_WITH_PADDING
                            && x <= RIGHT_BASE_WITH_PADDING + 6 * SQUARE_WITH_GAP + SQUARE_SIZE) {
                        move += "F";
                    }
                } else {
                    for (int row = 0; row < 5; row++) {
                        if (y - (180 + SQUARE_WITH_GAP * row) <= SQUARE_SIZE && y >= 180) {
                            if (azulGame.getTurn().equals("A") &&
                                    x >= LEFT_BASE_WITH_PADDING + SQUARE_WITH_GAP * (4 - row) &&
                                    x <= LEFT_BASE_WITH_PADDING + SQUARE_WITH_GAP * 4
                                            + SQUARE_SIZE) {
                                move += row;
                            }
                            if (azulGame.getTurn().equals("B") &&
                                    x >= RIGHT_BASE_WITH_PADDING + SQUARE_WITH_GAP * (4 - row) &&
                                    x <= RIGHT_BASE_WITH_PADDING + SQUARE_WITH_GAP * 4
                                            + SQUARE_SIZE) {
                                move += row;
                            }
                            break;
                        }
                    }
                }
                decodeAndMove(move, 4);
            }
        }

        private void decodeAndMove(String move, int targetLength) {
            String[] gameState = azulGame.rebuildStateString();
            if (azulGame.generateAction(gameState) == null) {
                azulGame.setTurn("B");
            }
            if (move.length() == targetLength && azulGame.isMoveValid(gameState, move)) {
                String currentTurn = azulGame.getTurn();
                gameState = azulGame.applyMove(gameState, move);
                gameState = azulGame.nextRound(gameState);
                boolean hasComplete = checkCompletion();
                // has complete and cannot tile further
                if (hasComplete && azulGame.generateAction(gameState) == null) {
                    Score BonusScore = azulGame.getPlayers()[0].getBoard().getMosaic()
                            .calculateBonusScore();
                    azulGame.getPlayers()[0].getBoard().getScore().addScore(BonusScore);

                    int maxScore = 0;
                    char maxPlayer = 'A';
                    for (Player player : azulGame.getPlayers()) {
                        int score = player.getBoard().getScore().getScore();
                        if (score > maxScore) {
                            maxScore = score;
                            maxPlayer = player.getId();
                        }
                    }
                    int mul = maxPlayer - 'A';
                    showCompletion(mul * RIGHT_BASE + 30, 200);
                }

                makeFCTiles();
                makeOtherTiles();
                makeScore();

                // AI
                if (checkbox.isSelected() && !azulGame.getTurn().equals(currentTurn)) {
                    // until change turn
                    currentTurn = azulGame.getTurn();

                    while (azulGame.getTurn().equals(currentTurn)) {
                        String action = azulGame.generateAction(gameState);
                        if (action != null) {
                            gameState = azulGame.applyMove(gameState, action);
                        }
                        gameState = azulGame.nextRound(gameState);

                        hasComplete = checkCompletion();
                        if (hasComplete && action == null) {
                            Score BonusScore = azulGame.getPlayers()[0].getBoard().getMosaic()
                                    .calculateBonusScore();
                            azulGame.getPlayers()[0].getBoard().getScore().addScore(BonusScore);

                            int maxScore = 0;
                            char maxPlayer = 'A';
                            for (Player player : azulGame.getPlayers()) {
                                int score = player.getBoard().getScore().getScore();
                                if (score > maxScore) {
                                    maxScore = score;
                                    maxPlayer = player.getId();
                                }
                            }
                            int mul = maxPlayer - 'A';
                            showCompletion(mul * RIGHT_BASE + 30, 200);
                            break;
                        }

                        makeFCTiles();
                        makeOtherTiles();
                        makeScore();
                    }
                }

            } else {
                snapToHome();
            }

        }

        /**
         * Snap the tile to its home position (if it is not on the grid)
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
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
     * Decode discard info
     *
     * @param substringD the substring of discard
     * @param discard    the discard
     */
    private void decodeDiscard(String substringD, Rectangle[][] discard) {
        //Record the number of tiles of various colors in the discard
        for (Rectangle[] rectangles : discard) {
            for (int j = 0; j < LENGTH; j++) {
                rectangles[j].setFill(Color.LIGHTGREY);
            }
        }
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
        for (Rectangle[] rectangles : bag) {
            for (int j = 0; j < LENGTH; j++) {
                rectangles[j].setFill(Color.LIGHTGREY);
            }
        }
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

    /**
     * Set up the group that represents the places that make the board
     */
    private void makeBoard(Group board, int x, char player) {
        board.getChildren().clear();

        // box
        Text title = new Text(x + 50, 155, "Player " + player + " with Score ");
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
                Rectangle tileRect = new Rectangle(x + LEFT_PADDING + SQUARE_WITH_GAP * j,
                        180 + SQUARE_WITH_GAP * i, SQUARE_SIZE,
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
                Rectangle tileRect = new Rectangle(x + 240 + LEFT_PADDING + SQUARE_WITH_GAP * j,
                        180 + SQUARE_WITH_GAP * i, SQUARE_SIZE,
                        SQUARE_SIZE);
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
            Rectangle tileRect = new Rectangle(x + LEFT_PADDING + SQUARE_WITH_GAP * i, 430,
                    SQUARE_SIZE,
                    SQUARE_SIZE);
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
        CheckBox playerBCheckBox = new CheckBox();
        playerBCheckBox.setLayoutX(1100);
        playerBCheckBox.setLayoutY(60);
        playerBCheckBox.setText("AI for B");
        checkbox = playerBCheckBox;

        common.getChildren().add(playerBCheckBox);

        //////////////////////////// FACTORIES /////////////////////////////////////
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    Rectangle factoryRect = new Rectangle(
                            LEFT_PADDING + i * 95 + j * SQUARE_WITH_GAP,
                            LEFT_PADDING + k * SQUARE_WITH_GAP,
                            SQUARE_SIZE, SQUARE_SIZE);
                    factoryRect.setFill(Color.LIGHTGREY);
                    common.getChildren().add(factoryRect);
                }
            }
            Text text = new Text(SQUARE_WITH_GAP + i * 95, 116, String.valueOf(i));
            text.setFill(Color.BLACK);
            common.getChildren().add(text);
        }

        //////////////////////////// CENTRE /////////////////////////////////////
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                Rectangle centreRect = new Rectangle(710 + i * SQUARE_WITH_GAP,
                        LEFT_PADDING + j * SQUARE_WITH_GAP, SQUARE_SIZE,
                        SQUARE_SIZE);
                centreRect.setFill(Color.LIGHTGREY);
                common.getChildren().add(centreRect);
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
                int x = LEFT_PADDING + i * 95 + col * SQUARE_WITH_GAP;
                int y = LEFT_PADDING + row * SQUARE_WITH_GAP;
                gTiles.getChildren().add(new DraggableTile(tiles.get(j).getColorCode(), i, x, y));
            }
        }

        ArrayList<Tile> centreTiles = centre.getTiles();
        for (int i = 0; i < centreTiles.size(); i++) {
            int col = i % 8;
            int row = i / 8;
            int x = 710 + col * SQUARE_WITH_GAP;
            int y = LEFT_PADDING + row * SQUARE_WITH_GAP;
            gTiles.getChildren().add(new DraggableTile(centreTiles.get(i).getColorCode(),
                    100, x, y));
        }

        if (centre.hasFirstPlayerTile()) {
            gTiles.getChildren().add(new DraggableTile(centre.getFirstPlayerTile().getColorCode(),
                    100, 710 + 7 * SQUARE_WITH_GAP, LEFT_PADDING + 1 * SQUARE_WITH_GAP));
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
                        gTiles.getChildren().add(new DraggableTile(code, from,
                                prefix + LEFT_PADDING + SQUARE_WITH_GAP * (4 - row + i),
                                180 + SQUARE_WITH_GAP * row));
                    }
                }
            }
        }

        gTiles.toFront();
    }

    private void makeOtherTiles() {
        for (Player player : azulGame.getPlayers()) {
            Rectangle[][] mosaic = (player.getId() - 'A' == 0) ? mosaicA : mosaicB;
            Rectangle[] floor = (player.getId() - 'A' == 0) ? floorA : floorB;

            String mosaicString = "M" + player.getBoard().getMosaic().toString();
            String floorString = "F" + player.getBoard().getFloor().toString();
            decodeMosaic(mosaicString, mosaic);
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

        Text turnText = new Text(585, 70, turn);
        turnText.setFont(Font.font(32));
        turnText.setFill(Color.BLACK);

        score.getChildren().add(playerAScoreText);
        score.getChildren().add(playerBScoreText);
        score.getChildren().add(turnText);
    }

    /**
     * Create the message to be displayed when the player completes the puzzle.
     */
    private void makeCompletion() {
        completionText.setFill(Color.BLACK);
        completionText.setEffect(dropShadow);
        completionText.setCache(true);
        completionText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        completionText.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(completionText);
    }

    /**
     * Check game completion and update status
     */
    private boolean checkCompletion() {
        for (Player player : azulGame.getPlayers()) {
            if (player.getBoard().getMosaic().hasCompleteRow()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Show the completion message
     */
    private void showCompletion(int x, int y) {
        completionText.setLayoutX(x);
        completionText.setLayoutY(y);
        completionText.toFront();
        completionText.setOpacity(1);
    }


    /**
     * Hide the completion message
     */
    private void hideCompletion() {
        completionText.toBack();
        completionText.setOpacity(0);
    }


    /**
     * Start a new game, resetting everything as necessary
     */
    private void newGame() {
        try {
            hideCompletion();
            azulGame = new comp1110.ass2.Game();
            azulGame.refillFactories(azulGame.rebuildStateString());
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
        root.getChildren().add(controls);
        root.getChildren().add(score);

//        setUpHandlers(scene);
//        setUpSoundLoop();
        makeBoard(leftBoard, LEFT_BASE, 'A');
        makeBoard(rightBoard, RIGHT_BASE, 'B');
        makeCommon();
        makeCompletion();

        newGame();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
