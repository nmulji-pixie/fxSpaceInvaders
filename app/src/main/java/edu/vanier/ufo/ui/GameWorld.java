package edu.vanier.ufo.ui;
import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.game.Missile;
import edu.vanier.ufo.game.Tank;
import edu.vanier.ufo.game.TankBot;
import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.level.Level;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
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
    private ProgressBar cooldownTimer;
    private HBox HUD;
    private Label currentLevelLabel;
    private int score;
    private Label scoreLabel;
    private boolean isWon;
    private final Level level;

    public GameWorld(int fps, String title, Runnable shutdownCallback, Level level) {
        super(fps, title, shutdownCallback);

        this.level = level;
        this.score = 0;
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

        getSceneNodes().getChildren().add(this.level.getBackground());
        
        // Create the scene
        setGameSurface(new Scene(getSceneNodes(), 1000, 600));

        // Change the background of the main scene.
        getGameSurface().setFill(new ImagePattern(new Image(ResourcesManager.BACKGROUND)));
        primaryStage.setScene(getGameSurface());

        // Setup Game input
        setupInput(primaryStage);

        // Create many spheres
        generateManySpheres(this.level.getSprites());

        this.currentLevelLabel = new Label("Level " + this.level.getLevelNumber());
        this.currentLevelLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        this.scoreLabel = new Label( String.valueOf(this.score));
        this.scoreLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 15));
        this.HUD = new HBox(new StackPane((new ImageView(new Image(getClass().getResource("/images/score_label.png").toExternalForm()))), this.currentLevelLabel), new StackPane((new ImageView(new Image(getClass().getResource("/images/score_label.png").toExternalForm()))), this.scoreLabel));
        this.HUD.setLayoutX(750);
        this.level.getTank().addId("player");
        this.queueAddSprites(this.level.getTank());

        this.cooldownTimer = new ProgressBar();

        getSceneNodes().getChildren().add(this.cooldownTimer);
        getSceneNodes().getChildren().add(this.HUD);
        // load sound files
        this.playSound(this.level.getMusic());
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
                    this.level.getTank().fire();
                }
            }
        });

        primaryStage.getScene().setOnKeyPressed((KeyEvent event) -> {
            vKeys.put(event.getCode(), true);
            this.level.getTank().plotCourse(vKeys, true);

            if (event.getCode() == KeyCode.SPACE)
                this.level.getTank().changeWeapon();
            else if (event.getCode() == KeyCode.F)
                this.level.getTank().shieldToggle();
        });

        primaryStage.getScene().setOnKeyReleased(event -> {
            vKeys.put(event.getCode(), false);
            this.level.getTank().plotCourse(vKeys, true);

        });

        // set up stats
        EventHandler showMouseMove = (EventHandler<MouseEvent>) (MouseEvent event) -> {
            this.level.getTank().aimAt(event.getSceneX(), event.getSceneY());
        };

        primaryStage.getScene().setOnMouseMoved(showMouseMove);
    }
    
    @Override
    protected void deinitialize() {
        this.getGameSurface().setOnMousePressed(null);
        this.getGameSurface().setOnMousePressed(null);
        this.getGameSurface().setOnKeyReleased(null);
        this.getGameSurface().setOnMouseMoved(null);
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
                newY,
                TankBot.Difficulty.EASY
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
            this.cooldownTimer.setProgress(this.level.getTank().getCooldown());

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
        if (sprite == this.level.getTank()) {
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
                
                if (spriteB.isId("enemy"))
                    this.score += 100;
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

    private boolean isGameOver() {
        if (this.level.getTank().isDead()){
            this.isWon = false;
            return true;
        } else if (
            this.getSpritesById("enemy").stream().allMatch(
                (x) -> x instanceof TankBot && ((TankBot)x).isDead()
            )
        ) {
            this.isWon = true;
            return true;
        }
        
        return false;
    }

    public void updateScore(){
        this.scoreLabel.setText(String.valueOf(this.score));
    }

    public boolean isWon() {
        return isWon;
    }
}
