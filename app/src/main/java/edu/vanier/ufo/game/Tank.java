package edu.vanier.ufo.game;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.Sprite;
import javafx.event.ActionEvent;
import javafx.scene.CacheHint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

public class Tank extends Sprite {
    private final static float THRUST_AMOUNT = 2.3f;

    private final static float MISSILE_THRUST_AMOUNT = 6.3F;

    private final StackPane flipBook;

    private boolean shieldOn;

    private Circle shield;

    private FadeTransition shieldFade;
    
    private final ResourcesManager.TankColor color;
    private ResourcesManager.BarrelType barrelType;
    
    private final RotatedImageView tankSprite;
    private final RotatedImageView barrelSprite;
    private final RotatedImageView shotSprite;
    
    private static final int SHOT_TICKS = (int) (0.1 * ResourcesManager.FRAMES_PER_SECOND);
    private int shotTicks;
    
    private int cooldownTicks;
    
    private double health;
    private ProgressBar healthBar;

    public Tank(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y) {
        // Load one image.
        this.color = color;
        this.barrelType = barrelType;
        
        this.health = ResourcesManager.MAX_HEALTH;
        this.tankSprite = new RotatedImageView(ResourcesManager.getTankBody(this.color), -90, new Point2D(0.5, 0.5));
        
        this.barrelSprite = new RotatedImageView(ResourcesManager.getTankBarrel(this.color, this.barrelType), 90, new Point2D(0.5, 0.8));
        
        this.shotSprite = new RotatedImageView(ResourcesManager.getTankShot(this.barrelType), -90, new Point2D(0.5, 0.5));
        this.shotSprite.setPivot(new Point2D(0.5, -this.barrelSprite.getHeight() / this.shotSprite.getHeight()));
        this.shotSprite.setVisible(false);
        
        this.initHealthBar();
        this.initShield();
        
        this.flipBook = new StackPane(
            this.tankSprite,
            this.barrelSprite,
            this.shotSprite,
            this.healthBar,
            this.shield
        );
        
        setNode(flipBook);
        flipBook.setTranslateX(x);
        flipBook.setTranslateY(y);
        flipBook.setCache(true);
        flipBook.setCacheHint(CacheHint.SPEED);
        //flipBook.setManaged(false);
        //flipBook.setAutoSizeChildren(false);
        
        this.setCollisionBounds(this.tankSprite);
    }

    /**
     * Initialize health bar
     */
    private void initHealthBar() {
        this.healthBar = new ProgressBar(this.health / ResourcesManager.MAX_HEALTH);

        this.healthBar.setPrefWidth(this.tankSprite.getWidth() * 0.8);
        this.healthBar.setTranslateY(-this.tankSprite.getHeight());
        this.healthBar.setTranslateY(-this.tankSprite.getHeight());
        
        this.setHealthBarColor(Color.LIME);
    }
    
    private void initShield() {
        this.shield = new Circle();
        
        // add shield
        shield = new Circle();
        shield.setRadius(60);
        shield.setStrokeWidth(5);
        shield.setStroke(Color.LIMEGREEN);
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
            shieldFade.stop();
            shield.setVisible(false);
            setCollisionBounds(this.tankSprite);
        });
        shieldFade.playFromStart();
        
        this.shield.setVisible(false);
    }
    
    /**
     * Change the velocity of the atom particle.
     */
    @Override
    public void handleUpdate() {
        if (this.shotSprite.isVisible())
            if (--shotTicks == 0)
                this.shotSprite.setVisible(false);
        
        if (this.cooldownTicks != 0)
            --cooldownTicks;
    }

    /**
     * The center X coordinate of the current visible image. See
     * <code>getCurrentTankImage()</code> method.
     *
     * @return The scene or screen X coordinate.
     */
    public double getCenterX() {
        return this.flipBook.getTranslateX() + this.flipBook.getWidth() / 2;
    }

    /**
     * The center Y coordinate of the current visible image. See
     * <code>getCurrentTankImage()</code> method.
     *
     * @return The scene or screen Y coordinate.
     */
    public double getCenterY() {
        return this.flipBook.getTranslateY() + this.flipBook.getHeight() / 2;
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
        this.shotSprite.turnToScene(sceneX, sceneY);
    }

    public boolean fire() {
        if (this.cooldownTicks != 0)
            return false;
        
        this.shotSprite.setVisible(true);
        this.shotTicks = SHOT_TICKS;
        this.cooldownTicks = this.barrelType.getCooldownFrames();
        
        Missile missile = new Missile(
            ResourcesManager.getTankBullet(this.color, this.barrelType), this, 90
        );
        
        missile.setVelocityX(Math.cos(Math.toRadians(this.barrelSprite.getRotation())) * (MISSILE_THRUST_AMOUNT));
        missile.setVelocityY(Math.sin(Math.toRadians(this.barrelSprite.getRotation())) * (MISSILE_THRUST_AMOUNT));

        missile.getNode().setTranslateX(
            this.getCenterX() +
            (
                Math.cos(Math.toRadians(this.barrelSprite.getRotation())) *
                this.barrelSprite.getHeight()
            ) -
            (missile.getImageViewNode().getWidth() / 2)
        );
        
        missile.getNode().setTranslateY(
            this.getCenterY() +
            (
                Math.sin(Math.toRadians(this.barrelSprite.getRotation())) *
                this.barrelSprite.getHeight()
            ) -
            (missile.getImageViewNode().getHeight() / 2)
        );
        
        this.getEngine().queueAddSprites(missile);
        this.getEngine().playSound(ResourcesManager.SoundDescriptor.SOUND_SHOOT);
        
        return true;
    }

    public void changeWeapon() {
        int i;
        
        if (this.cooldownTicks != 0)
            return;
        
        final ResourcesManager.BarrelType barrelTypes[] =
            ResourcesManager.BarrelType.values();
        
        for (i = 0; i < barrelTypes.length; ++i)
            if (this.barrelType == barrelTypes[i])
                break;

        this.setBarrelType(barrelTypes[++i % barrelTypes.length]);
    }

    public void shieldToggle() {
        shieldOn = !shieldOn;
        if (shieldOn) {
            setCollisionBounds(shield);
            shield.setVisible(true);
            shieldFade.playFromStart();
        } else {
            shieldFade.stop();
        }
    }
    
    public double getCooldown() {
        return (double)this.cooldownTicks / (double)this.barrelType.getCooldownFrames();
    }

    @Override
    protected void handleDeath() {
        Explosion explosion = new Explosion(
            ResourcesManager.ExplosionKind.NORMAL,
            this.getCenterX(),
            this.getCenterY()
        );
        
        this.getEngine().queueAddSprites(explosion);
    }
    
    public double getHealth() {
        return this.health;
    }
    
    public void takeDamage(double damage) {
        if (damage < 0)
            throw new IllegalArgumentException("Can't have negative damage");
        else if (this.shieldOn)
            return;

        this.health -= damage;
        this.healthBar.setProgress(this.health / ResourcesManager.MAX_HEALTH);

        if (this.health <= 0) {
            this.health = 0;
            this.die();
        }
    }

    public ResourcesManager.TankColor getColor() {
        return color;
    }

    public ResourcesManager.BarrelType getBarrelType() {
        return barrelType;
    }
    
    protected void setHealthBarColor(Color color) {
        this.healthBar.setStyle(String.format(
            "-fx-accent: #%02x%02x%02x",
            (int)(color.getRed() * 255),
            (int)(color.getGreen() * 255),
            (int)(color.getBlue() * 255)
        ));
    }
    
    private void setBarrelType(ResourcesManager.BarrelType newBarrelType) {
        this.barrelType = newBarrelType;
        
        this.barrelSprite.setImage(
            new Image(
                ResourcesManager.getTankBarrel(this.color, this.barrelType)
            )
        );
        
        this.shotSprite.setImage(
            new Image(
                ResourcesManager.getTankShot(this.barrelType)
            )
        );
        
        this.shotSprite.setPivot(new Point2D(
            0.5, -this.barrelSprite.getHeight() / this.shotSprite.getHeight()
        ));
    }
    
    protected int getCooldownTicks() {
        return this.cooldownTicks;
    }
    
    protected void setCooldownTicks(int newValue) {
        this.cooldownTicks = newValue;
    }
}
