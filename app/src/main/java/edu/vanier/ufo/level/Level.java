package edu.vanier.ufo.level;

import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.ui.GameWorld;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.io.IOException;

public class Level {
    GameWorld gameWorld;
    private final int levelNumber;
    private Tank tank;
    private int sprites;
    private GridPane levelTile;


    public Level(int levelNumber, Stage primaryStage) {
        this.levelNumber = levelNumber;
        this.tank = generateTank();
        this.sprites = generateNumberOfSprites();
        this.levelTile = generateLevelBackground();

        this.gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "TankInvaders", this.sprites,this.tank,this.levelNumber , this.levelTile);
        primaryStage.setScene(this.gameWorld.getGameSurface());
        this.gameWorld.initialize(primaryStage);
        this.gameWorld.beginGameLoop();
    }

    public Tank generateTank() {
        if (this.levelNumber == 1) {
            this.tank = new Tank(ResourcesManager.TankColor.GREEN, ResourcesManager.BarrelType.NORMAL, 350, 400);
        } else if (this.levelNumber == 2) {
            this.tank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.LONG, 350, 400);
        } else if (this.levelNumber == 3) {
            this.tank = new Tank(ResourcesManager.TankColor.SAND, ResourcesManager.BarrelType.THICK, 350, 400);
        }
        return this.tank;
    }

    public int generateNumberOfSprites(){
        if (this.levelNumber == 1){
            this.sprites = 5;
        } else if (this.levelNumber == 2) {
            this.sprites = 10;
        }else if (this.levelNumber == 3){
            this.sprites = 15;
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



    public Tank getTank() {
        return tank;
    }

    public int getSprites() {
        return sprites;
    }
}
