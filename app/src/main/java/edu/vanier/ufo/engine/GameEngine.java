package edu.vanier.ufo.engine;

import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This application demonstrates a JavaFX Game Loop. Shown below are the methods
 * which comprise of the fundamentals to a simple game loop in JavaFX:
 * <pre>
 *  <b>initialize()</b> - Initialize the game world.
 *  <b>beginGameLoop()</b> - Creates a JavaFX Timeline object containing the game life cycle.
 *  <b>updateSprites()</b> - Updates the sprite objects each period (per frame)
 *  <b>checkCollisions()</b> - Method will determine objects that collide with each other.
 *  <b>cleanupSprites()</b> - Any sprite objects needing to be removed from play.
 * </pre>
 *
 * @author cdea
 */
public abstract class GameEngine {

    /**
     * All nodes to be displayed in the game window.
     */
    private final Group sceneNodes, gameNodes;
    /**
     * The game loop using JavaFX's <code>Timeline</code> API.
     */
    private Timeline gameLoop;

    /**
     * Number of frames per second.
     */
    private final int framesPerSecond;

    /**
     * Title in the application window.
     */
    private final String windowTitle;

    /**
     * The sprite manager.
     */
    private final SpriteManager spriteManager;

    private final List<Sprite> queuedSprites;
    
    private final SoundManager soundManager;

    private final Runnable shutdownCallback;
    
    private boolean isShutdown;
    /**
     * Constructor that is called by the derived class.This will set the frames
 per second, title, and setup the game loop.
     *
     * @param fps - Frames per second.
     * @param title - Title of the application window.
     * @param shutdownCallback - Callback when shutdown is called
     */
    public GameEngine(final int fps, final String title, Runnable shutdownCallback) {
        this.framesPerSecond = fps;
        this.windowTitle = title;
        this.spriteManager = new SpriteManager();
        this.soundManager = new SoundManager();
        this.gameNodes = new Group();
        this.sceneNodes = new Group(this.gameNodes);
        this.queuedSprites = new LinkedList<>();
        this.shutdownCallback = shutdownCallback;
        this.isShutdown = false;
        // create and set timeline for the game loop

        buildAndSetGameLoop();
    }


    /**
     * Builds and sets the game loop ready to be started.
     */
    protected final void buildAndSetGameLoop() {

        final Duration frameDuration = Duration.millis(1000 / (float) getFramesPerSecond());
        EventHandler<ActionEvent> onFinished = (event) -> {
            // update actors
            updateSprites();
            // check for collision.
            checkCollisions();
            // removed dead sprites.
            cleanupSprites();
            // add queued sprites
            addQueuedSprites();
        };
        final KeyFrame gameFrame = new KeyFrame(frameDuration, onFinished);
        // sets the game world's game loop (Timeline)
        gameLoop = new Timeline(gameFrame);
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Initialize the game world by update the JavaFX Stage.
     */
    public abstract void initialize();
    
    /**
     * Deinitialize the game world by update the JavaFX Stage.
     *
     * @param primaryStage The main window containing the JavaFX Scene.
     */
    protected abstract void deinitialize();

    /**
     * Kicks off (plays) the Timeline objects containing one key frame that
     * simply runs indefinitely with each frame invoking a method to update
     * sprite objects, check for collisions, and cleanup sprite objects.
     */
    public void beginGameLoop() {
        this.sceneNodes.requestFocus();
        getGameLoop().play();
    }

    /**
     * Updates each game sprite in the game world. This method will loop through
     * each sprite and passing it to the handleUpdate() method. The derived
     * class should override handleUpdate() method.
     */
    protected void updateSprites() {
        for (Sprite sprite : spriteManager.getAllSprites()) {
            handleUpdate(sprite);
        }
    }

    /**
     * Updates the sprite object's information to position on the game surface.
     *
     * @param sprite - The sprite to update.
     */
    protected void handleUpdate(Sprite sprite) {
    }

    /**
     * Checks each game sprite in the game world to determine a collision
     * occurred. The method will loop through each sprite and passing it to the
     * handleCollision() method. The derived class should override
     * handleCollision() method.
     */
    protected void checkCollisions() {
        // check each sprite against other sprite objects.
        for (Sprite spriteA : spriteManager.getCollisionsToCheck()) {
            for (Sprite spriteB : spriteManager.getCollisionsToCheck()) {
                if (handleCollision(spriteA, spriteB)) {
                    // The break helps optimize the collisions
                    //  The break statement means one object only hits another
                    // object as opposed to one hitting many objects.
                    // To be more accurate comment out the break statement.
                    break;
                }
            }
        }
    }

    /**
     * When two objects collide this method can handle the passed in sprite
     * objects. By default it returns false, meaning the objects do not collide.
     *
     * @param spriteA - called from checkCollision() method to be compared.
     * @param spriteB - called from checkCollision() method to be compared.
     * @return boolean True if the objects collided, otherwise false.
     */
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        return false;
    }

    /**
     * Sprites to be cleaned up.
     */
    protected void cleanupSprites() {
        spriteManager.getSpritesToBeRemoved().addAll(
            spriteManager.getAllSprites().stream().filter(Sprite::isDead).toList()
        );
        
        spriteManager.getSpritesToBeRemoved().forEach((x) -> this.gameNodes.getChildren().remove(x.getNode()));
        spriteManager.cleanupSprites();
    }

    /**
     * Returns the frames per second.
     *
     * @return int The frames per second.
     */
    protected int getFramesPerSecond() {
        return framesPerSecond;
    }

    /**
     * Returns the game's window title.
     *
     * @return String The game's window title.
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * The game loop (Timeline) which is used to update, check collisions, and
     * cleanup sprite objects at every interval (fps).
     *
     * @return Timeline An animation running indefinitely representing the game
     * loop.
     */
    protected Timeline getGameLoop() {
        return gameLoop;
    }

    /**
     * The sets the current game loop for this game world.
     *
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected void setGameLoop(Timeline gameLoop) {
        this.gameLoop = gameLoop;
    }
    
    public void queueAddSprites(Sprite... sprites) {
        this.queuedSprites.addAll(Arrays.asList(sprites));
    }
    
    private void addQueuedSprites() {
        for (Sprite sprite : this.queuedSprites) {
            sprite.setEngine(this);
            this.gameNodes.getChildren().add(sprite.getNode());
        }
        
        this.spriteManager.addSprites(this.queuedSprites);
        
        this.queuedSprites.clear();
    }
    
    public void removeSprites(Sprite... sprites) {
        this.spriteManager.addSpritesToBeRemoved(sprites);
    }
    
    public List<Sprite> getSpritesById(String id) {
        return this.spriteManager.getAllSprites().stream().filter((s) -> s.isId(id)).toList();
    }

    /**
     * All JavaFX nodes which are rendered onto the game surface(Scene) is a
     * JavaFX Group object.
     *
     * @return Group The root containing many child nodes to be displayed into
     * the Scene area.
     */
    public Group getSceneNodes() {
        return sceneNodes;
    }

    public Group getGameNodes() {
        return gameNodes;
    }

    protected SoundManager getSoundManager() {
        return soundManager;
    }
    
    public void playSound(ResourcesManager.SoundDescriptor sound) {
        this.soundManager.playSound(sound);
    }

    /**
     * Stop threads and stop media from playing.
     */
    public void shutdown() {
        if (this.isShutdown)
            return;
        
        this.isShutdown = true;
        
        this.deinitialize();
        this.soundManager.shutdown();
        
        // Stop the game's animation.
        getGameLoop().stop();
        
        this.shutdownCallback.run();
    }
}
