package edu.vanier.ufo.ui;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.*;
import edu.vanier.ufo.game.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
    private Tank playerTank = new Tank(ResourcesManager.TankColor.BLUE, ResourcesManager.BarrelType.NORMAL, 350, 450);
    private ProgressBar cooldownTimer;
    private int currentLevel;
    private List tankBots;
    private boolean finished;












    
    public GameWorld(int fps, String title) {
        super(fps, title);


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


        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 1000, 600));

        // Change the background of the main scene.
        getGameSurface().setFill(new ImagePattern(new Image(ResourcesManager.BACKGROUND)));

        primaryStage.setScene(getGameSurface());

        // Setup Game input
        setupInput(primaryStage);

        // Create many spheres
        this.tankBots = generateManySpheres(5);

        getSpriteManager().addSprites(playerTank);
        getSceneNodes().getChildren().add(0, playerTank.getNode());
        
        this.cooldownTimer = new ProgressBar();
        
        getSceneNodes().getChildren().add(this.cooldownTimer);
        // load sound files
        getSoundManager().loadSoundEffects("laser", getClass().getClassLoader().getResource(ResourcesManager.SOUND_SHOOT));
    }

    /**
     * Sets up the mouse input.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupInput(Stage primaryStage) {
        System.out.println("Ship's center is (" + playerTank.getCenterX() + ", " + playerTank.getCenterY() + ")");

        EventHandler fireOrMove = (EventHandler<MouseEvent>) (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {

                // fire
                Missile missile = playerTank.fire();
                if (missile == null)
                    return;
                
                getSpriteManager().addSprites(missile);

                // play sound
                getSoundManager().playSound("laser");

                getSceneNodes().getChildren().add(0, missile.getNode());

            }
        };
        
        HashMap<KeyCode, Boolean> vKeys = new HashMap();
        primaryStage.getScene().setOnMousePressed(fireOrMove);
        primaryStage.getScene().setOnKeyPressed((KeyEvent event) -> {
            vKeys.put(event.getCode(), true);
            playerTank.plotCourse(vKeys, true);
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
    private List generateManySpheres(int numSpheres) {
        List<TankBot> tankBots = new LinkedList<>();
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
            tankBots.add(atom);
            
            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            // Randomize the location of each newly generated atom.
            atom.setVelocityX((rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1));
            atom.setVelocityY((rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1));

            // add to actors in play (sprite objects)
            getSpriteManager().addSprites(atom);

            // add sprite's 
            getSceneNodes().getChildren().add(atom.getNode());
        }
        return tankBots;
    }

    /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        this.cooldownTimer.setProgress(this.playerTank.getCooldown());
        if (isGameOver()){
            this.finished = true;
            this.shutdown();
    }
        // advance object
        sprite.update();
        if (sprite instanceof Missile) {
            removeMissiles((Missile) sprite);
        } else if (sprite instanceof TankBot){
            bounceOffWalls(sprite);
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
        if (sprite instanceof TankBot) {
            displayNode = sprite.getNode();
        } else {
            return;
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

            getSpriteManager().addSpritesToBeRemoved(missile);
            getSceneNodes().getChildren().remove(missile.getNode());

        }
        if (missile.getNode().getTranslateY() > getGameSurface().getHeight()
                - missile.getNode().getBoundsInParent().getHeight()
                || missile.getNode().getTranslateY() < 0) {

            getSpriteManager().addSpritesToBeRemoved(missile);
            getSceneNodes().getChildren().remove(missile.getNode());
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
        if (
            spriteA != spriteB &&
            spriteA.collide(spriteB) &&
            !(spriteA == playerTank && spriteB instanceof Missile) &&
            !(spriteB == playerTank && spriteA instanceof Missile) &&
            !(spriteA.getClass() == spriteB.getClass())
        ) {
            if (spriteA != playerTank) {
                spriteA.handleDeath(this);
                this.tankBots.remove(spriteA);
            }
            if (spriteB != playerTank) {
                spriteB.handleDeath(this);
                this.tankBots.remove(spriteB);
            }
            return true;
        }
        return false;
    }

    public boolean isGameOver(){
        return tankBots.isEmpty();
    }

    public boolean isFinished() {
        return finished;
    }
}
