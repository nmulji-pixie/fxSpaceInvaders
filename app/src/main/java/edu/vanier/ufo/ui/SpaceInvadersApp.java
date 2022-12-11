package edu.vanier.ufo.ui;

import edu.vanier.ufo.controller.Controller;
import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main driver of the game.
 *
 * @author cdea
 */
public class SpaceInvadersApp extends Application {

    Controller controller = new Controller();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Setup title, scene, stats, controls, and actors.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
        loader.setController(controller);
        Pane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        controller.setPrimaryStage(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

}
