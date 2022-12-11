package edu.vanier.ufo.game;

import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.scene.paint.Color;

public class TankBot extends Tank {
    public TankBot(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y) {
        super(color, barrelType, x, y);
        this.setHealthBarColor(Color.RED);
    }
}
