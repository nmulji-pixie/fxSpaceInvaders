package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A spherical looking object (Atom) with a random radius, color, and velocity.
 * When two atoms collide each will fade and become removed from the scene. The
 * method called implode() implements a fade transition effect.
 *
 * @author cdea
 */
public class Atom extends Sprite {
    private boolean rotationFollowVelocity;
    
    /**
     * Constructor will create a optionally create a gradient fill circle shape.This sprite will contain a JavaFX Circle node.
     *
     * @param imagePath the path of the image to be embedded in the node object.
     * @param baseRotate
     * @param pivot
     */
    public Atom(String imagePath, double baseRotate, Point2D pivot) {
        RotatedImageView newAtom = new RotatedImageView(imagePath, baseRotate, pivot);
        this.node = newAtom;
        this.collidingNode = newAtom;
        this.rotationFollowVelocity = false;
    }
    
    
    public Atom(String imagePath) {
        this(imagePath, 0, new Point2D(0.5, 0.5));
    }

    /**
     * Change the velocity of the current atom particle.
     */
    @Override
    public void update() {
        getNode().setTranslateX(getNode().getTranslateX() + vX);
        getNode().setTranslateY(getNode().getTranslateY() + vY);
        
        if (this.rotationFollowVelocity)
            this.getImageViewNode().turnToDirection(vX, vY);
    }

    /**
     * Returns a node casted as a JavaFX Circle shape.
     *
     * @return Circle shape representing JavaFX node for convenience.
     */
    public RotatedImageView getImageViewNode() {
        return (RotatedImageView) getNode();
    }

    /**
     * Animate an implosion. Once done remove from the game world
     *
     * @param gameWorld - game world
     */
    public void implode(final GameEngine gameWorld) {
        vX = vY = 0;
        Node currentNode = getNode();

        //Sprite explosion = new Atom(ResourcesManager.EXPLOSION);
        //explosion.getNode().setTranslateX(currentNode.getTranslateX());
        //explosion.getNode().setTranslateY(currentNode.getTranslateY());
        //gameWorld.getSceneNodes().getChildren().add(explosion.getNode());

        FadeTransition ft = new FadeTransition(Duration.millis(300), currentNode);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished((ActionEvent event) -> {
            isDead = true;
            //gameWorld.getSceneNodes().getChildren().removeAll(currentNode, explosion.getNode());
        });
        ft.play();
    }

    @Override
    public void handleDeath(GameEngine gameWorld) {
        implode(gameWorld);
        super.handleDeath(gameWorld);
    }

    public boolean isRotationFollowVelocity() {
        return this.rotationFollowVelocity;
    }

    public void setRotationFollowVelocity(boolean rotationFollowVelocity) {
        this.rotationFollowVelocity = rotationFollowVelocity;
    }
}
