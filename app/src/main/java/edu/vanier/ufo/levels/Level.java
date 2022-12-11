package edu.vanier.ufo.levels;

import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.ui.GameWorld;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Level {
    private Tank tank;
    private GameWorld gameWorld;
    private int levelNumber;
    private int numberOfSprites;
    private Scene gameScene;

    public Level(int levelNumber) {
        this.gameWorld = new GameWorld(ResourcesManager.FRAMES_PER_SECOND, "JavaFXInvader");
        this.gameWorld.beginGameLoop();
        this.tank = generateTankAndSprite();
        this.levelNumber = levelNumber;
        this.gameScene = this.gameWorld.getGameSurface();
    }

    /*

       getters and setters

         */
    public Tank getTank() {
        return tank;
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    public Tank generateTankAndSprite(){
        if (this.levelNumber == 1){
            this.tank = new Tank(ResourcesManager.TankColor.GREEN, ResourcesManager.BarrelType.NORMAL, 350, 450);
            this.numberOfSprites = 15;
        } else if (this.levelNumber == 2) {
            this.tank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.NORMAL, 350, 450);
            this.numberOfSprites = 20;
        }else if (this.levelNumber == 3) {
            this.tank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.NORMAL, 350, 450);
            this.numberOfSprites = 25;
        }else if (this.levelNumber == 4) {
            this.tank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.NORMAL, 350, 450);
            this.numberOfSprites = 30;
        }
        return this.tank;
    }

}
