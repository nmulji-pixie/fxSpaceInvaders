package edu.vanier.ufo.level;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.SoundManager;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.ui.GameWorld;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class LevelSelectionScene extends Scene {
    @FXML
    private BorderPane levelOne, levelTwo, levelThree;
    
    private GameWorld world;
    private SoundManager soundManager;
    private final Parent selectionRoot;
    
    public LevelSelectionScene(Stage primaryStage) throws IOException {
        super(new Group());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/levelScreen.fxml"));
        loader.setController(this);
        this.selectionRoot = loader.load();
        
        this.setRoot(this.selectionRoot);
         
        this.soundManager = new SoundManager();
        this.soundManager.playSound(ResourcesManager.SoundDescriptor.MUSIC_MENU);
        
        this.initiateLevels(primaryStage);
    }


    public void initiateLevels(Stage primaryStage){
        levelOne.setOnMouseClicked(e-> {
            this.launchLevel(new Level(1));
        });

        levelTwo.setOnMouseClicked(e -> {
            this.launchLevel(new Level(2));
            
        });

        levelThree.setOnMouseClicked(e -> {
            this.launchLevel(new Level(3));
        });
    }
    
    private void launchLevel(Level level) {
        this.soundManager.shutdown();
        
        this.world = new GameWorld(
            ResourcesManager.FRAMES_PER_SECOND,
            "Level",
            this::gameOverCallback,
            level
        );
        
        this.world.setSoundManager(this.soundManager);
        this.world.initialize();
        this.setRoot(this.world.getSceneNodes());
        this.world.beginGameLoop();
    }
    
    private void gameOverCallback() {
        if (!world.isWon()) {
            try {
                Pane game_OVer = new FXMLLoader(getClass().getResource("/fxml/game_over.fxml")).load();
                world.getSceneNodes().getChildren().add(game_OVer);
                game_OVer.setOnMouseClicked((event) -> {
                    this.shutdownWorld();
                });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (world.isWon()) {
            try {
                Pane game_OVer = new FXMLLoader(getClass().getResource("/fxml/you_won.fxml")).load();
                world.getSceneNodes().getChildren().add(game_OVer);
                game_OVer.setOnMouseClicked((event) -> {
                    this.shutdownWorld();
                });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    public void shutdownWorld() {
        if (this.world == null)
            return;
        
        this.world.shutdown();
        this.world = null;
        
        this.setRoot(this.selectionRoot);
        
        this.soundManager.playSound(ResourcesManager.SoundDescriptor.MUSIC_MENU);
    }
}
