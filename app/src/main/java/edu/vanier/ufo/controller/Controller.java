package edu.vanier.ufo.controller;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.level.GameLevel;
import edu.vanier.ufo.ui.GameWorld;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
    @FXML
    Button btnPlay;

    GameWorld gameWorld;
    private static Stage primaryStage;
    public void initialize(){

    }

    public void handlePlayButton(){
        gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "JavaFX Space Invaders");
        gameWorld.setLevel(new GameLevel(1));
        gameWorld.setCurrentLevel(1);
        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();
        primaryStage.setScene(gameWorld.getGameSurface());
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Controller.primaryStage = primaryStage;
    }
}
