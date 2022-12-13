package edu.vanier.ufo.ui;

import edu.vanier.ufo.level.LevelSelectionScene;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main driver of the game.
 *
 * @author cdea
 */
public class SpaceInvadersApp extends Application {

    @FXML
    private ImageView btnPlay;
    
    private LevelSelectionScene levelScene;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Setup title, scene, stats, controls, and actors.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_page.fxml"));
        loader.setController(this);
        this.levelScene = new LevelSelectionScene(primaryStage);
        
        Pane root = loader.load();
        btnPlay.setOnMouseClicked((e) -> {
            this.shutdown();
            
            primaryStage.setScene(this.levelScene);
            primaryStage.setFullScreen(true);
        });
        
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        this.shutdown();
    }
    
    private void shutdown() {
        if (this.levelScene == null)
            return;
        
        this.levelScene.shutdownWorld();
    }
}
