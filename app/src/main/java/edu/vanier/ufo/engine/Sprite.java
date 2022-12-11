package edu.vanier.ufo.engine;

import java.util.ArrayList;
import java.util.List;
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
    private boolean isDead;
    private GameEngine engine;
    private List<String> ids;
    private double prevTranslateX, prevTranslateY;
    protected Node collidingNode;

    public Sprite() {
        this.vX = 0;
        this.vY = 0;
        this.isDead = false;
        this.engine = null;
        this.ids = new ArrayList<>();
    }
    
    public void addId(String id) {
        this.ids.add(id);
    }
    
    public void removeId(String id) {
        this.ids.remove(id);
    }
    
    public boolean isId(String id) {
        return this.ids.contains(id);
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
        return this.getCollisionBounds().intersects(
            this.getCollisionBounds().sceneToLocal(
            other.getCollisionBounds().localToScene(
                other.getCollisionBounds().getBoundsInLocal()
            ))
        );
    }

    public final void update() {
        prevTranslateX = getNode().getTranslateX();
        prevTranslateY = getNode().getTranslateY();
        getNode().setTranslateX(getNode().getTranslateX() + vX);
        getNode().setTranslateY(getNode().getTranslateY() + vY);
        
        this.handleUpdate();
    }
    
    public final void undoUpdate() {
        getNode().setTranslateX(prevTranslateX);
        getNode().setTranslateY(prevTranslateY);
    }
    
    protected abstract void handleUpdate();

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

    public void die() {
        if (this.isDead)
            return;
        
        this.isDead = true;
        this.handleDeath();
    }
    
    abstract protected void handleDeath();    
    
    public boolean isDead() {
        return this.isDead;
    }
    
    protected final GameEngine getEngine() {
        return this.engine;
    }
    
    public final void setEngine(GameEngine engine) {
        if (this.engine != null)
            throw new IllegalStateException("Engine for sprite already set");
        
        this.engine = engine;
    }
}
