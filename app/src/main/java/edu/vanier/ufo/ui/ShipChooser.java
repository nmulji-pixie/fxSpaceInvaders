package edu.vanier.ufo.ui;

import edu.vanier.ufo.game.Ship;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.scene.Scene;

import java.util.ArrayList;

public class ShipChooser {
    private Ship ship;
    private ArrayList<String> ships;
    private Scene scene;

    public ShipChooser() {
        generateShipList(this.ships);
    }

    public ArrayList<String> generateShipList(ArrayList<String> ships){
        ships.add(ResourcesManager.TANK_1);
        ships.add(ResourcesManager.TANK_2);
        ships.add(ResourcesManager.TANK_3);
        ships.add(ResourcesManager.TANK_4);
        ships.add(ResourcesManager.TANK_5);
        ships.add(ResourcesManager.TANK_6);

        return ships;
    }

    public void initialize(){

    }
}
