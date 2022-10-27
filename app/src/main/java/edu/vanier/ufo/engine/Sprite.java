package edu.vanier.ufo.engine;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.Node;

/**
 * A class used to represent a sprite of any type on the scene.  
 */
public abstract class Sprite {
    // The JavaFX node that holds the sprite graphic.
    protected Node node;
    private Image image;
    protected double vX;
    protected double vY;
    private double width;
    private double height;
    public boolean isDead = false;

    protected Node collidingNode;

    public Sprite() {
        vX = 0;
        vY = 0;
    }

    public void setImage(Image inImage) {
        image = inImage;
        width = inImage.getWidth();
        height = inImage.getHeight();
    }

    public void setImage(String filename) {
        Image image = new Image(filename);
        setImage(image);
    }

    public void setVelocity(double x, double y) {
        vX = x;
        vY = y;
    }

    public void addVelocity(double x, double y) {
        vX += x;
        vY += y;
    }

    /**
     * Did this sprite collide into the other sprite?
     *
     * @param other - The other sprite.
     * @return boolean - Whether this or the other sprite collided, otherwise
     * false.
     */
    public boolean collide(Sprite other) {
        return collidingNode.getBoundsInParent().intersects(other.node.getBoundsInParent());
        /*if (collidingNode == null || other.collidingNode == null) {
            return false;
        }

        // determine it's size
        Node otherSphere = other.collidingNode;
        Node thisSphere = collidingNode;
        Point2D otherCenter = otherSphere.localToScene(otherSphere.getCenterX(), otherSphere.getCenterY());
        Point2D thisCenter = thisSphere.localToScene(thisSphere.getCenterX(), thisSphere.getCenterY());
        double dx = otherCenter.getX() - thisCenter.getX();
        double dy = otherCenter.getY() - thisCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDist = otherSphere.getRadius() + thisSphere.getRadius();

        return (distance < minDist);*/
    }

    public abstract void update();

    public boolean intersects(Sprite s) {
        //return s.getBoundary().intersects(this.getBoundary());        
        Bounds sBounds = s.getNode().localToScene(s.getNode().getBoundsInLocal());
        return node.intersects(sBounds);

    }

    public Image getImage() {
        return image;
    }

    public double getVelocityX() {
        return vX;
    }

    public void setVelocityX(double velocityX) {
        this.vX = velocityX;
    }

    public double getVelocityY() {
        return vY;
    }

    public void setVelocityY(double velocityY) {
        this.vY = velocityY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getCollisionBounds() {
        return collidingNode;
    }

    public void setCollisionBounds(Node collisionBounds) {
        this.collidingNode = collisionBounds;
    }

    public void handleDeath(GameEngine gameWorld) {
        gameWorld.getSpriteManager().addSpritesToBeRemoved(this);
    }
}
