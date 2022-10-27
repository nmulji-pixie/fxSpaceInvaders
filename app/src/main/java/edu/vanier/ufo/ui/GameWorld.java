package edu.vanier.ufo.ui;

import edu.vanier.ufo.helpers.ResourcesManager;
import edu.vanier.ufo.engine.*;
import edu.vanier.ufo.game.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

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

    // mouse pt label
    Label mousePtLabel = new Label();

    // mouse press pt label
    Label mousePressPtLabel = new Label();

    TextField xCoordinate = new TextField("234");
    TextField yCoordinate = new TextField("200");
    Button moveShipButton = new Button("Rotate ship");

    Ship spaceShip = new Ship();

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

        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);        

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 900, 600));

        getGameSurface().setFill(
            new ImagePattern(new Image(ResourcesManager.BACKGROUND))
        );
        
        primaryStage.setScene(getGameSurface());
        // Setup Game input
        setupGameControl(primaryStage);

        // Create many spheres
        // TODO: change this. It must be implemented as a new game level.
        generateInvaders(20);

        // Display the number of spheres visible.
        // Create a button to add more spheres.
        // Create a button to freeze the game loop.
        //final Timeline gameLoop = getGameLoop();
        getSpriteManager().addSprites(spaceShip);
        getSceneNodes().getChildren().add(0, spaceShip.getNode());

        // mouse point
        VBox stats = new VBox();

        HBox row1 = new HBox();
        mousePtLabel.setTextFill(Color.WHITE);
        row1.getChildren().add(mousePtLabel);
        HBox row2 = new HBox();
        mousePressPtLabel.setTextFill(Color.WHITE);
        row2.getChildren().add(mousePressPtLabel);

        stats.getChildren().add(row1);
        stats.getChildren().add(row2);

        // mouse point
        HBox enterCoord1 = new HBox();
        enterCoord1.getChildren().add(xCoordinate);
        enterCoord1.getChildren().add(yCoordinate);
        enterCoord1.getChildren().add(moveShipButton);
        stats.getChildren().add(enterCoord1);
        // moveShipButton.setOnAction(new EventHandler<ActionEvent>() {
        moveShipButton.setOnAction((ActionEvent actionEvent) -> {
            double x = Double.parseDouble(xCoordinate.getText());
            double y = Double.parseDouble(yCoordinate.getText());
            spaceShip.plotCourse(x, y, false);
        });
        // load sound file        
        //TODO: load your sound file(s) here. 
        getSoundManager().loadSoundEffects("laser", getClass().getResource(ResourcesManager.SOUND_LASER));                
    }

    /**
     * Sets up the mouse and keyboard inputs.
     *
     * @param primaryStage The primary stage (app window).
     */
    private void setupGameControl(Stage primaryStage) {
        System.out.println("Ship's center is (" + spaceShip.getCenterX() + ", " + spaceShip.getCenterY() + ")");

        EventHandler<MouseEvent> onFireOrMove = (MouseEvent event) -> {
            mousePressPtLabel.setText("Mouse Press PT = (" + event.getX() + ", " + event.getY() + ")");
            // Left mouse button click.
            if (event.getButton() == MouseButton.PRIMARY) {
                // Aim
                spaceShip.plotCourse(event.getX(), event.getY(), false);
                // fire
                Missile missile = spaceShip.fire();
                getSpriteManager().addSprites(missile);

                // play sound
                getSoundManager().playSound("laser");

                getSceneNodes().getChildren().add(0, missile.getNode());

            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Right mouse button click.
                // determine when all atoms are not on the game surface. Ship should be the only sprite left.
                // GAME OVER case!
                if (getSpriteManager().getAllSprites().size() <= 1) {
                    //TODO: change the number of sprites to be generated depending on the game level.
                    //TODO: All sprites have beend destroyed. Genereate a new set of sprites.                    
                    System.out.println("Game over or current level is over!");
                }
                // stop ship from moving forward
                spaceShip.applyTheBrakes(event.getX(), event.getY());
                // move forward and rotate ship
                spaceShip.plotCourse(event.getX(), event.getY(), true);
            }
        };

        // Initialize input
        primaryStage.getScene().setOnMousePressed(onFireOrMove);
        //addEventHandler(MouseEvent.MOUSE_PRESSED, me);

        // set up stats
        EventHandler changeWeapons = (EventHandler<KeyEvent>) (KeyEvent event) -> {
            if (KeyCode.SPACE == event.getCode()) {
                spaceShip.shieldToggle();
                return;
            }
            spaceShip.changeWeapon(event.getCode());
        };
        primaryStage.getScene().setOnKeyPressed(changeWeapons);
        // set up stats
        EventHandler showMouseMove = (EventHandler<MouseEvent>) (MouseEvent event) -> {
            mousePtLabel.setText("Mouse PT = (" + event.getX() + ", " + event.getY() + ")");
        };
        primaryStage.getScene().setOnMouseMoved(showMouseMove);
    }

    /**
     * Generate n number of invader sprites of random size, color and movement
     * directions.
     *
     * @param numSprites The number of invader sprites to be generated. to
     * generate.
     */
    private void generateInvaders(int numSprites) {
        Random rand = new Random();
        Scene gameSurface = getGameSurface();
        for (int i = 0; i < numSprites; i++) {
            //TODO: genereate different types of invader sprites.            
            Atom atom = new Atom(ResourcesManager.INVADER_UFO);
            ImageView atomSprite = atom.getImageViewNode();
            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            // Randomize the location of each newly generated atom.
            atom.setVelocityX((rand.nextInt(2) + rand.nextDouble()) * (rand.nextBoolean() ? 1 : -1));
            atom.setVelocityY((rand.nextInt(2) + rand.nextDouble()) * (rand.nextBoolean() ? 1 : -1));

            // random x between 0 to width of scene
            double newX = rand.nextInt((int) gameSurface.getWidth());
            // TODO: configure the size of the generated images.
            // check for the right of the width newX is greater than width 
            // minus radius times 2(width of sprite)
            if (newX > (gameSurface.getWidth() - (rand.nextInt(15) + 5 * 2))) {
                newX = gameSurface.getWidth() - (rand.nextInt(15) + 5 * 2);
            }
            // check for the bottom of screen the height newY is greater than height 
            // minus radius times 2(height of sprite)
            double newY = rand.nextInt((int) gameSurface.getHeight());
            if (newY > (gameSurface.getHeight() - (rand.nextInt(15) + 5 * 2))) {
                newY = gameSurface.getHeight() - (rand.nextInt(15) + 5 * 2);
            }
            atomSprite.setTranslateX(newX);
            atomSprite.setTranslateY(newY);
            atomSprite.setVisible(true);
            atomSprite.setId(atom.toString());
            atomSprite.setCache(true);
            atomSprite.setCacheHint(CacheHint.SPEED);
            atomSprite.setManaged(false);
            // Add the newly created invader sprite to list of sprites. 
            getSpriteManager().addSprites(atom);
            // Inject the newly created sprite into the scene.
            getSceneNodes().getChildren().add(atom.getNode());
        }
    }

    /**
     * Each sprite will update it's velocity and bounce off wall borders.
     *
     * @param sprite - An atomic particle (a sphere).
     */
    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object
        sprite.update();
        if (sprite instanceof Missile) {
            removeMissiles((Missile) sprite);
        } else {
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
        //FIXME: The ship movement has got issues.
        Node displayNode;
        if (sprite instanceof Ship) {
            displayNode = sprite.getNode();//((Ship)sprite).getCurrentShipImage();
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
     * zeroing out the velocity if a collision occurred.
     *
     * @param spriteA Sprite from the first list.
     * @param spriteB Sprite from the second list.
     * @return boolean returns a true if the two sprites have collided otherwise
     * false.
     */
    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
        if (spriteA != spriteB) {
            if (spriteA.collide(spriteB)) {

                if (spriteA != spaceShip) {
                    spriteA.handleDeath(this);
                }
                if (spriteB != spaceShip) {
                    spriteB.handleDeath(this);
                }
            }
        }
        return false;
    }
}
