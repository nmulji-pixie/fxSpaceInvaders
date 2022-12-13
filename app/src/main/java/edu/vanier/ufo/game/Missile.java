package edu.vanier.ufo.game;

import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.geometry.Point2D;

/**
 * A missile projectile without the radial gradient.
 */
public class Missile extends Atom {
    private final Tank owner;
    
    public Missile(String imagePath, Tank owner) {
        this(imagePath, owner, 0);
    }
    
    public Missile(String imagePath, Tank owner, double baseRotate) {
        super(imagePath, baseRotate, new Point2D(0.5, 0.5));
        this.setRotationFollowVelocity(true);
        this.owner = owner;
    }
    
    public double getCenterX() {
        return this.getNode().getTranslateX() + this.getImageViewNode().getWidth() / 2;
    }

    public double getCenterY() {
        return this.getNode().getTranslateY() + this.getImageViewNode().getHeight() / 2;
    }
    
    @Override
    protected void handleDeath() {
        this.collisionBounds = null;
        Explosion explosion = new Explosion(ResourcesManager.ExplosionKind.SMOKE, this.getCenterX(), this.getCenterY());
        explosion.setScale(0.4);
        this.getEngine().queueAddSprites(explosion);
    }
    
    public Tank getOwner() {
        return this.owner;
    }
}
