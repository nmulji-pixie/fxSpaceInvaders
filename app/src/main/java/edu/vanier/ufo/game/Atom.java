package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.Sprite;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 * A spherical looking object (Atom) with a random radius, color, and velocity.
 * When two atoms collide each will fade and become removed from the scene. The
 * method called implode() implements a fade transition effect.
 *
 * @author cdea
 */
public class Atom extends Sprite {
    private boolean rotationFollowVelocity;
    private final RotatedImageView imageViewNode;
    
    /**
     * Constructor will create a optionally create a gradient fill circle shape.This sprite will contain a JavaFX Circle node.
     *
     * @param imagePath the path of the image to be embedded in the node object.
     * @param baseRotate
     * @param pivot
     */
    public Atom(String imagePath, double baseRotate, Point2D pivot) {
        this.imageViewNode = new RotatedImageView(imagePath, baseRotate, pivot);
        this.collisionBounds = new Rectangle(
            this.imageViewNode.getWidth(),
            this.imageViewNode.getHeight()
        );
        
        this.collisionBounds.getTransforms().add(
            this.imageViewNode.getRotationTransform()
        );

        this.collisionBounds.setVisible(false);
        
        this.rotationFollowVelocity = false;
        this.node = new Group(this.imageViewNode, this.collisionBounds);
    }
    
    
    public Atom(String imagePath) {
        this(imagePath, 0, new Point2D(0.5, 0.5));
    }

    /**
     * Change the velocity of the current atom particle.
     */
    @Override
    public void handleUpdate() {
        if (this.rotationFollowVelocity)
            this.getImageViewNode().turnToDirection(vX, vY);
    }

    /**
     * Returns a node casted as a JavaFX Circle shape.
     *
     * @return Circle shape representing JavaFX node for convenience.
     */
    public RotatedImageView getImageViewNode() {
        return this.imageViewNode;
    }

    /**
     * Animate an implosion. Once done remove from the game world
     */
    @Override
    protected void handleDeath() {
    }

    public boolean isRotationFollowVelocity() {
        return this.rotationFollowVelocity;
    }

    public void setRotationFollowVelocity(boolean rotationFollowVelocity) {
        this.rotationFollowVelocity = rotationFollowVelocity;
    }
}
