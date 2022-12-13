package edu.vanier.ufo.level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LevelScreen {
    int levelNumber;
    Level level;
    Pane screen;
    @FXML
    BorderPane levelOne;
    @FXML
    BorderPane levelTwo;
    @FXML
    BorderPane levelThree;
    boolean islevelChosen;
    public LevelScreen(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/levelScreen.fxml"));
        loader.setController(this);
        this.screen = loader.load();
        primaryStage.getScene().setRoot(this.screen);

        if (!islevelChosen){
            this.level = this.generateLevels(primaryStage);
        }
    }


    public Level generateLevels(Stage primaryStage){
        levelOne.setOnMouseClicked(e-> {
            this.islevelChosen = true;
            this.level = new Level(1, primaryStage, (engine) -> {
                try {
                    new LevelScreen(primaryStage);
                    System.out.println("level one");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        levelTwo.setOnMouseClicked(e -> {
            this.islevelChosen = true;
            this.level = new Level(2, primaryStage, (engine) -> {
                try {
                    new LevelScreen(primaryStage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        levelThree.setOnMouseClicked(e -> {
            this.islevelChosen = true;
            this.level = new Level(3, primaryStage, (engine) -> {
                try {
                    new LevelScreen(primaryStage);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        return this.level;
    }

    public Level getLevel() {
        return level;
    }
}
