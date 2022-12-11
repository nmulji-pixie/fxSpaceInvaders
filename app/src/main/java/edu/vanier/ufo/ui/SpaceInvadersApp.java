package edu.vanier.ufo.ui;

import edu.vanier.ufo.controller.Controller;
import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.levels.Level;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.beans.EventHandler;
import java.io.IOException;

/**
 * The main driver of the game.
 *
 * @author cdea
 */
public class SpaceInvadersApp extends Application {

    Controller controller = new Controller();
    @FXML
    Button btnPlay;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
        loader.setController(this);
        Pane root = loader.load();
        btnPlay.setOnAction(e -> {
            Level level = new Level(1);
            primaryStage.setScene(level.getGameScene());
            level.getGameWorld().initialize(primaryStage);

        });



        Scene scene = new Scene(root);
        primaryStage.setScene(scene);





        primaryStage.show();





    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

}
