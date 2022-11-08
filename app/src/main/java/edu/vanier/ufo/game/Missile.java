package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;

/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {

 
    public Missile(String imagePath) {        
        super(imagePath);
    }
    
    public void implode(final GameEngine gameWorld)  {
        gameWorld.getSceneNodes().getChildren().remove(getNode());
    }
    
}
