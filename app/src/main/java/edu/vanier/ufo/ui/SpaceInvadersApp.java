package edu.vanier.ufo.ui;

import edu.vanier.ufo.level.Level;
import edu.vanier.ufo.level.LevelScreen;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button btnPlay;
    
    private Level currentLevel;
    
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

        Pane root = loader.load();
        btnPlay.setOnAction((e) -> {
            this.shutdown();
            this.currentLevel = new Level(1, primaryStage, () -> {
                try {
                   LevelScreen screen = new LevelScreen(primaryStage);
                   primaryStage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
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
        if (this.currentLevel == null)
            return;
        
        this.currentLevel.shutdown();
        this.currentLevel = null;
    }
}
