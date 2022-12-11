package edu.vanier.ufo.controller;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.ui.GameWorld;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
    @FXML
    Button btnPlay;

    GameEngine gameWorld;
    private Stage primaryStage;
    public void initialize(){

    }

    public void handlePlayButton(){
        gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "JavaFX Space Invaders");
        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();
        primaryStage.setScene(gameWorld.getGameSurface());
        
        primaryStage.setOnCloseRequest((event) -> {
            gameWorld.shutdown();
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
