package edu.vanier.ufo.level;

import edu.vanier.ufo.game.Ship;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.scene.image.Image;


public class GameLevel {
    private Ship ship;
    private int level_number;
    private int numberOfSprites;
    private int maxLevel = 4;

    public GameLevel(int level_number) {
        this.level_number = level_number;
        setShip(generateShip());
        setNumberOfSprites(generateNumberOfSprites());
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getLevel_number() {
        return level_number;
    }

    public void setLevel_number(int level_number) {
        this.level_number = level_number;
    }

    public int getNumberOfSprites() {
        return numberOfSprites;
    }

    public void setNumberOfSprites(int numberOfSprites) {
        this.numberOfSprites = numberOfSprites;
    }

    public Ship generateShip(){
        if (this.level_number == 1){
             this.ship = new Ship(new Image(ResourcesManager.TANK_1));
             return this.getShip();
        } else if (this.level_number == 2) {
            this.ship = new Ship(new Image(ResourcesManager.TANK_1));
            return this.getShip();
        } else if (this.level_number == 3) {
            this.ship = new Ship(new Image(ResourcesManager.TANK_1));
            return this.getShip();
        } else if (this.level_number == 4) {
            this.ship = new Ship(new Image(ResourcesManager.TANK_1));
            return this.getShip();
        }else {
            return null;
        }
    }

    public int generateNumberOfSprites(){
        if (this.level_number == 1){
            return 10;
        } else if (this.level_number == 2) {
            return 15;
        } else if (this.level_number == 3) {
            return 20;
        } else if (this.level_number == 4) {
            return 25;
        }else {
            return 0;
        }
    }
}
