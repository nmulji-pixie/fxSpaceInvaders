package edu.vanier.ufo.game;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.Sprite;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Map;
import javafx.geometry.Point2D;

public class Tank extends Sprite {
    private final static float THRUST_AMOUNT = 2.3f;

    private final static float MISSILE_THRUST_AMOUNT = 6.3F;

    private final Group flipBook = new Group();

    private KeyCode keyCode;

    private boolean shieldOn;

    private Circle shield;

    private FadeTransition shieldFade;

    private Circle hitBounds;
    
    private final ResourcesManager.TankColor color;
    private final ResourcesManager.BarrelType barrelType;
    
    private final RotatedImageView tankSprite;
    private final RotatedImageView barrelSprite;

    public Tank(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y) {
        // Load one image.
        this.color = color;
        this.barrelType = barrelType;
        
        this.tankSprite = new RotatedImageView(ResourcesManager.getTankBody(this.color), -90, new Point2D(0.5, 0.5));
        this.barrelSprite = new RotatedImageView(ResourcesManager.getTankBarrel(this.color, this.barrelType), 90, new Point2D(0.5, 1));
        
        flipBook.getChildren().addAll(this.tankSprite, this.barrelSprite);
        
        setNode(flipBook);
        flipBook.setTranslateX(x);
        flipBook.setTranslateY(y);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        flipBook.setManaged(false);
        flipBook.setAutoSizeChildren(false);
        
        initHitZone();
    }

    /**
     * Initialize the collision region for the space tank. It's just an
     * inscribed circle.
     */
    private void initHitZone() {
        // build hit zone
        if (hitBounds == null) {
            hitBounds = new Circle();
            hitBounds.setCenterX(this.tankSprite.getWidth());
            hitBounds.setCenterY(this.tankSprite.getHeight());
            hitBounds.setStroke(Color.PINK);
            hitBounds.setFill(Color.RED);
            hitBounds.setRadius(this.tankSprite.getHeight()/ 2);
            hitBounds.setOpacity(0);
            flipBook.getChildren().add(hitBounds);
            setCollisionBounds(hitBounds);
        }
    }

    /**
     * Change the velocity of the atom particle.
     */
    @Override
    public void update() {
        flipBook.setTranslateX(flipBook.getTranslateX() + vX);
        flipBook.setTranslateY(flipBook.getTranslateY() + vY);
    }

    /**
     * The center X coordinate of the current visible image. See
     * <code>getCurrentTankImage()</code> method.
     *
     * @return The scene or screen X coordinate.
     */
    public double getCenterX() {
        return this.flipBook.getTranslateX() + this.tankSprite.getWidth()/ 2;
    }

    /**
     * The center Y coordinate of the current visible image. See
     * <code>getCurrentTankImage()</code> method.
     *
     * @return The scene or screen Y coordinate.
     */
    public double getCenterY() {
        return this.flipBook.getTranslateY() + this.tankSprite.getHeight()/ 2;
    }

    public void plotCourse(Map<KeyCode, Boolean> vKeys, boolean thrust){
        this.vX = (
            (vKeys.getOrDefault(KeyCode.A, false) ? -1 : 0) +
            (vKeys.getOrDefault(KeyCode.D, false) ? 1 : 0)
        );
        
        this.vY = (
            (vKeys.getOrDefault(KeyCode.W, false) ? -1 : 0) +
            (vKeys.getOrDefault(KeyCode.S, false) ? 1 : 0)
        );

        if (vX == 0 && vY == 0)
            return;
        
        double angle = Math.atan2(this.vY, this.vX);
        
        this.vX = Math.cos(angle) * THRUST_AMOUNT;
        this.vY = Math.sin(angle) * THRUST_AMOUNT;
        
        this.tankSprite.turnToDirection(vX, vY);
    }
    
    public void aimAt(double sceneX, double sceneY) {
        this.barrelSprite.turnToScene(sceneX, sceneY);
    }

    public Missile fire() {
        Missile fireMissile = new Missile(ResourcesManager.getTankBullet(this.color, this.barrelType));
        /*float slowDownAmt = 0;
        int scaleBeginningMissle;
        if (KeyCode.DIGIT2 == keyCode) {
            slowDownAmt = 1.3f;
            scaleBeginningMissle = 11;
        } else {
            scaleBeginningMissle = 8;
        }

        //fireMissile.setPosition(getNode().getLayoutX()+ 10, getNode().getLayoutY() - 20);
        // velocity vector of the missile
        fireMissile.setVelocityX(Math.cos(Math.toRadians(this.barrelSprite.getRotation())) * (MISSILE_THRUST_AMOUNT - slowDownAmt));
        fireMissile.setVelocityY(Math.sin(Math.toRadians(-this.barrelSprite.getRotation())) * (MISSILE_THRUST_AMOUNT - slowDownAmt));

        // make the missile launch in the direction of the current direction of the tank nose. based on the
        // current frame (uIndex) into the list of image view nodes.
        RotatedImage barrelImage = this.barrelSprite.getCurrentImage();

        // start to appear in the center of the tank to come out the direction of the nose of the tank.
        double offsetX = (barrelImage.getBoundsInLocal().getWidth() - fireMissile.getNode().getBoundsInLocal().getWidth()) / 2;
        double offsetY = (barrelImage.getBoundsInLocal().getHeight() - fireMissile.getNode().getBoundsInLocal().getHeight()) / 2;

        // initial launch of the missile   (multiply vector by 4 makes it appear at the nose of the tank)
        fireMissile.getNode().setTranslateX(getNode().getTranslateX() + (offsetX + (fireMissile.getVelocityX() * scaleBeginningMissle)));
        fireMissile.getNode().setTranslateY(getNode().getTranslateY() + (offsetY + (fireMissile.getVelocityY() * scaleBeginningMissle)));
        */return fireMissile;
    }

    public void changeWeapon(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public void shieldToggle() {
        if (shield == null) {
            double x = this.tankSprite.getBoundsInLocal().getWidth() / 2;
            double y = this.tankSprite.getBoundsInLocal().getHeight() / 2;

            // add shield
            shield = new Circle();
            shield.setRadius(60);
            shield.setStrokeWidth(5);
            shield.setStroke(Color.LIMEGREEN);
            shield.setCenterX(x);
            shield.setCenterY(y);
            shield.setOpacity(.70);
            setCollisionBounds(shield);
            //--
            shieldFade = new FadeTransition();
            shieldFade.setFromValue(1);
            shieldFade.setToValue(.40);
            shieldFade.setDuration(Duration.millis(1000));
            shieldFade.setCycleCount(12);
            shieldFade.setAutoReverse(true);
            shieldFade.setNode(shield);
            shieldFade.setOnFinished((ActionEvent actionEvent) -> {
                shieldOn = false;
                flipBook.getChildren().remove(shield);
                shieldFade.stop();
                setCollisionBounds(hitBounds);
            });
            shieldFade.playFromStart();

        }
        shieldOn = !shieldOn;
        if (shieldOn) {
            setCollisionBounds(shield);
            flipBook.getChildren().add(0, shield);
            shieldFade.playFromStart();
        } else {
            flipBook.getChildren().remove(shield);
            shieldFade.stop();
            setCollisionBounds(hitBounds);
        }
    }
}
