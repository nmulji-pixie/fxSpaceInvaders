package edu.vanier.ufo.level;

import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.layout.Pane;

public final class Level {
    private final int levelNumber;
    private Tank tank;
    private int sprites;
    private final Pane background;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.tank = generateTank();
        this.sprites = generateNumberOfSprites();
        this.background = this.generateLevelBackground();
    }

    private Tank generateTank() {
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

    private int generateNumberOfSprites(){
        switch (this.levelNumber) {
            case 1:
                this.sprites = 50;
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
    
    public ResourcesManager.SoundDescriptor getMusic() {
        return switch (this.levelNumber) {
            case 1 -> ResourcesManager.SoundDescriptor.MUSIC_LEVEL1;
            case 2 -> ResourcesManager.SoundDescriptor.MUSIC_LEVEL2;
            case 3 -> ResourcesManager.SoundDescriptor.MUSIC_LEVEL3;
            default -> null;
        };
    }

    private Pane generateLevelBackground(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(switch(this.levelNumber) {
            case 1 -> "/fxml/level1.fxml";
            case 2 -> "/fxml/level2.fxml";
            case 3 -> "/fxml/level1.fxml";
            default -> "/fxml/level1.fxml";
        }));
        
        try {
            Pane pane = loader.load();
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

    public int getLevelNumber() {
        return levelNumber;
    }

    public Pane getBackground() {
        return background;
    }
}
