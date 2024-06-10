package chess.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Starting point of the JavaFX GUI
 */
public class Gui extends Application {

    /**
     * This method is called by the Application to start the GUI.
     *
     * @param primaryStage The initial root stage of the application.
     * @throws Exception e
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("chess A.I.");

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/startScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1150);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        StartScreen startScreen = fxmlLoader.getController();
        startScreen.setStage(primaryStage);
        primaryStage.show();


    }

    /**
     * The entry point of the GUI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

}
