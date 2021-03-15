package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {
    /* board layout */
    private static final int BOARD_WIDTH = 1200;
    private static final int BOARD_HEIGHT = 700;

    @Override
    public void start(Stage stage) throws Exception {
        //  FIXME Task 9: Implement a basic playable Azul game in JavaFX that only allows tiles to be placed in valid places
        stage.setTitle("Azul");
        Group root = new Group();
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

        stage.setScene(scene);
        stage.show();
    }
}
