package edu.vanier.ufo.level;

import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.ui.GameWorld;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public final class Level {
    private GameWorld gameWorld;
    private final int levelNumber;
    private Tank tank;
    private int sprites;
    private GridPane levelTile;

    public Level(int levelNumber, Stage primaryStage, Runnable shutdownCallback) {
        this.levelNumber = levelNumber;
        this.tank = generateTank();
        this.sprites = generateNumberOfSprites();
        this.levelTile = generateLevelBackground();
        
        this.gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "TankInvaders", shutdownCallback, this.sprites,this.tank,this.levelNumber , this.levelTile);
        primaryStage.setScene(this.gameWorld.getGameSurface());
        this.gameWorld.initialize(primaryStage);
        this.gameWorld.beginGameLoop();
    }

    public Tank generateTank() {
        switch (this.levelNumber) {
            case 1:
                this.tank = new Tank(ResourcesManager.TankColor.GREEN, ResourcesManager.BarrelType.NORMAL, 350, 400);
                break;
            case 2:
                this.tank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.LONG, 350, 400);
                break;
            case 3:
                this.tank = new Tank(ResourcesManager.TankColor.SAND, ResourcesManager.BarrelType.THICK, 350, 400);
                break;
            default:
                break;
        }
        return this.tank;
    }

    public int generateNumberOfSprites(){
        switch (this.levelNumber) {
            case 1:
                this.sprites = 5;
                break;
            case 2:
                this.sprites = 10;
                break;
            case 3:
                this.sprites = 15;
                break;
            default:
                break;
        }
        return this.sprites;
    }

    public GridPane generateLevelBackground(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/map1.fxml"));
        try {
            GridPane pane = loader.load();
            return pane;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void shutdown() {
        this.gameWorld.shutdown();
    }

    public Tank getTank() {
        return tank;
    }

    public int getSprites() {
        return sprites;
    }
}
