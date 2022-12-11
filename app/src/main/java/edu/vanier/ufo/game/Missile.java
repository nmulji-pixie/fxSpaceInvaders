package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import javafx.geometry.Point2D;

/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {
    public Missile(String imagePath) {
        super(imagePath);
        this.setRotationFollowVelocity(true);
    }
    
    public Missile(String imagePath, double baseRotate) {
        super(imagePath, baseRotate, new Point2D(0.5, 0.5));
        this.setRotationFollowVelocity(true);
    }
    
    public void implode(final GameEngine gameWorld)  {
        gameWorld.getSceneNodes().getChildren().remove(getNode());
    }
    
}
