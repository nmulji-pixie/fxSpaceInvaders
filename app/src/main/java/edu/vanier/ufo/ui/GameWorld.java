package edu.vanier.ufo.ui;
import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.game.Missile;
import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.game.TankBot;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;

/**
 * This is a simple game world simulating a bunch of spheres looking like atomic
 * particles colliding with each other. When the game loop begins the user will
 * notice random spheres (atomic particles) floating and colliding. The user
 * will navigate his/her ship by right clicking the mouse to thrust forward and
 * left click to fire weapon to atoms.
 *
 * @author cdea
 */


public class GameWorld extends GameEngine {
    private int sprites;
    private Tank playerTank;
    private ProgressBar cooldownTimer;
    private HBox HUD;
    private Label currentLevelLabel;
    private String score;
    private Label scoreLabel;
    private int currentLevel;
    private GridPane levelTile;

    public GameWorld(int fps, String title, Runnable shutdownCallback) {
        super(fps, title, shutdownCallback);
    }

    public GameWorld(int fps, String title, Runnable shutdownCallback, int sprites, Tank playerTank, int currentLevel, GridPane levelTile) {
        super(fps, title, shutdownCallback);

        this.sprites = sprites;
        this.playerTank = playerTank;
        this.currentLevel = currentLevel;
        this.levelTile = levelTile;
        this.score = String.valueOf(this.playerTank.getPoints());
    }

    /**
     * Initialize the game world by adding sprite objects.
     *
     * @param primaryStage The game window or primary stage.
     */
    @Override
    public void initialize(final Stage primaryStage) {

        // Sets the window title
        primaryStage.setTitle(getWindowTitle());
        //primaryStage.setFullScreen(true);

        getSceneNodes().getChildren().add(this.levelTile);
        
        // Create the scene
        setGameSurface(new Scene(getSceneNodes(), 1000, 600));

        // Change the background of the main scene.
        getGameSurface().setFill(new ImagePattern(new Image(ResourcesManager.BACKGROUND)));

        primaryStage.setScene(getGameSurface());

        // Setup Game input
        setupInput(primaryStage);

        // Create many spheres
        generateManySpheres(this.sprites);

        this.currentLevelLabel = new Label("Level " + this.currentLevel);
        this.scoreLabel = new Label("Score " + this.score);
        this.HUD = new HBox(this.currentLevelLabel, this.scoreLabel);
        this.HUD.setLayoutX(900);
        this.playerTank.addId("player");
        this.queueAddSprites(playerTank);

        this.cooldownTimer = new ProgressBar();

        getSceneNodes().getChildren().add(this.cooldownTimer);
        getSceneNodes().getChildren().add(this.HUD);
        // load sound files
        getSoundManager().loadSoundEffects("shoot", getClass().getClassLoader().getResource(ResourcesManager.SOUND_SHOOT));
        getSoundManager().loadSoundEffects("explosion", getClass().getClassLoader().getResource(ResourcesManager.SOUND_EXPLOSION));
    }

    /**
     * Sets up the mouse input.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupInput(Stage primaryStage) {
        HashMap<KeyCode, Boolean> vKeys = new HashMap();

        primaryStage.getScene().setOnMousePressed((MouseEvent event) -> {
            if (!isGameOver()) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    playerTank.fire();
                }
            }
        });

        primaryStage.getScene().setOnKeyPressed((KeyEvent event) -> {
            vKeys.put(event.getCode(), true);
            playerTank.plotCourse(vKeys, true);

            if (event.getCode() == KeyCode.SPACE)
                playerTank.changeWeapon();
            else if (event.getCode() == KeyCode.F)
                playerTank.shieldToggle();
        });

        primaryStage.getScene().setOnKeyReleased(event -> {
            vKeys.put(event.getCode(), false);
            playerTank.plotCourse(vKeys, true);

        });

        // set up stats
        EventHandler showMouseMove = (EventHandler<MouseEvent>) (MouseEvent event) -> {
            playerTank.aimAt(event.getSceneX(), event.getSceneY());
        };

        primaryStage.getScene().setOnMouseMoved(showMouseMove);
    }


    /**
     * Make some more space spheres (Atomic particles)
     *
     * @param numSpheres The number of random sized, color, and velocity atoms
     * to generate.
     */
    private void generateManySpheres(int numSpheres) {
        Random rnd = new Random();
        Scene gameSurface = getGameSurface();
        for (int i = 0; i < numSpheres; i++) {
            ResourcesManager.TankColor colors[] = ResourcesManager.TankColor.values();
            ResourcesManager.BarrelType barrelTypes[] = ResourcesManager.BarrelType.values();

            // random x between 0 to width of scene
            double newX = rnd.nextInt((int) gameSurface.getWidth() - 100);

            if (newX > (gameSurface.getWidth() - (rnd.nextInt(15) + 5 * 2))) {
                newX = gameSurface.getWidth() - (rnd.nextInt(15) + 5 * 2);
            }

            double newY = rnd.nextInt((int) (gameSurface.getHeight() - 300));
            if (newY > (gameSurface.getHeight() - (rnd.nextInt(15) + 5 * 2))) {
                newY = gameSurface.getHeight() - (rnd.nextInt(15) + 5 * 2);
            }

            TankBot atom = new TankBot(
                colors[rnd.nextInt(colors.length)],
                barrelTypes[rnd.nextInt(barrelTypes.length)],
                newX,
                newY
            );

            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            // Randomize the location of each newly generated atom.
            atom.setVelocityX((rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1));
            atom.setVelocityY((rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1));

            // add to actors in play (sprite objects)
            this.queueAddSprites(atom);
        }
    }

    /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        if (isGameOver()) {
            this.shutdown();
        } else {
            this.cooldownTimer.setProgress(this.playerTank.getCooldown());


            // advance object
            sprite.update();
            if (sprite instanceof Missile) {
                removeMissiles((Missile) sprite);
            } else {
                bounceOffWalls(sprite);
            }
        }
    }

    /**
     * Change the direction of the moving object when it encounters the walls.
     *
     * @param sprite The sprite to update based on the wall boundaries.
     */
    private void bounceOffWalls(Sprite sprite) {
        // bounce off the walls when outside of boundaries

        Node displayNode;
        if (sprite == this.playerTank) {
            return;
        } else {
            displayNode = sprite.getNode();
        }
        // Get the group node's X and Y but use the ImageView to obtain the width.
        if (sprite.getNode().getTranslateX() > (getGameSurface().getWidth() - displayNode.getBoundsInParent().getWidth())
                || displayNode.getTranslateX() < 0) {

            // bounce the opposite direction
            sprite.setVelocityX(sprite.getVelocityX() * -1);
        }
        // Get the group node's X and Y but use the ImageView to obtain the height.
        if (sprite.getNode().getTranslateY() > getGameSurface().getHeight() - displayNode.getBoundsInParent().getHeight()
                || sprite.getNode().getTranslateY() < 0) {
            sprite.setVelocityY(sprite.getVelocityY() * -1);
        }
    }

    /**
     * Remove missiles when they reach the wall boundaries.
     *
     * @param missile The missile to remove based on the wall boundaries.
     */
    private void removeMissiles(Missile missile) {
        // bounce off the walls when outside of boundaries
        if (missile.getNode().getTranslateX() > (getGameSurface().getWidth()
                - missile.getNode().getBoundsInParent().getWidth())
                || missile.getNode().getTranslateX() < 0) {

            this.removeSprites(missile);

        }
        if (missile.getNode().getTranslateY() > getGameSurface().getHeight()
                - missile.getNode().getBoundsInParent().getHeight()
                || missile.getNode().getTranslateY() < 0) {

            this.removeSprites(missile);
        }
    }

    /**
     * How to handle the collision of two sprite objects. Stops the particle by
     * zeroing out the velocity if a collision occurred. /** How to handle the
     * collision of two sprite objects. Stops the particle by
     *
     *
     * @param spriteA Sprite from the first list.
     * @param spriteB Sprite from the second list.
     * @return boolean returns a true if the two sprites have collided otherwise
     * false.
     */
    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        if (spriteA != spriteB && spriteA.collide(spriteB)) {
            if (
                spriteA instanceof Missile &&
                spriteB instanceof Tank
            ) {
                if (((Missile)spriteA).getOwner() == spriteB)
                    return false;
                
                spriteA.die();
                if (spriteB instanceof TankBot)
                    this.updateScore();
                ((Tank)spriteB).takeDamage(
                    ((Missile)spriteA).getOwner().getBarrelType().getDamage()
                );
            } else if (
                spriteA instanceof Missile &&
                spriteB instanceof Missile
            ) {
                spriteA.die();
                spriteB.die();

                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isGameOver() {
        return this.playerTank.isDead();
    }

    public void updateScore(){
        this.score = String.valueOf(Integer.valueOf(this.score) + 20);
        this.scoreLabel.setText("Score " + this.score);
    }
}
