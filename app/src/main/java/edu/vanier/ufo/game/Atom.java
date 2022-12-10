package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * A spherical looking object (Atom) with a random radius, color, and velocity.
 * When two atoms collide each will fade and become removed from the scene. The
 * method called implode() implements a fade transition effect.
 *
 * @author cdea
 */
public class Atom extends Sprite {

    private final static int TWO_PI_DEGREES = 360;

    /**
     * Number of ship frames and directions the ship is pointing nose
     */
    private final static int NUM_DIRECTIONS = 32;

    /**
     * The angle of one direction (adjacent directions) (11.25 degrees)
     */
    private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);

    /**
     * Amount of time it takes the ship to move 180 degrees in milliseconds.
     */
    private final static int MILLIS_TURN_SHIP_180_DEGREES = 300;

    /**
     * When the ship turns on each direction one amount of time for one frame or
     * turn of the ship. (18.75 milliseconds)
     */
    private final static float MILLIS_PER_FRAME = (float) MILLIS_TURN_SHIP_180_DEGREES / (NUM_DIRECTIONS / 2);

    /**
     * All possible turn directions Clockwise, Counter Clockwise, or Neither
     * when the user clicks mouse around ship
     */
    private enum DIRECTION {
        CLOCKWISE, COUNTER_CLOCKWISE, NEITHER
    }

    /**
     * Velocity amount used vector when ship moves forward. scale vector of
     * ship. See flipBook translateX and Y.
     */
    private final static float THRUST_AMOUNT = 2.3f;

    /**
     *
     */
    private final static float MISSILE_THRUST_AMOUNT = 6.3F;

    /**
     * Angle in degrees to rotate ship.
     */
    /**
     * Current turning direction. default is NEITHER. Clockwise and Counter
     * Clockwise.
     */
    private Atom.DIRECTION turnDirection = Atom.DIRECTION.NEITHER;

    /**
     * The current starting position of the vector or coordinate where the nose
     * of the ship is pointing towards.
     */
    private CustomVector u; // current or start vector

    /**
     * All ImageViews of all the possible image frames for each direction the
     * ship is pointing. ie: 32 directions.
     */
    private final List<RotatedShipImage> directionalShips = new ArrayList<>();

    /**
     * The Timeline instance to animate the ship rotating using images. This is
     * an optical illusion similar to page flipping as each frame is displayed
     * the previous visible attribute is set to false. No rotation is happening.
     */
    private Timeline rotateShipTimeline;

    /**
     * The current index into the list of ImageViews representing each direction
     * of the ship. Zero is the ship pointing to the right or zero degrees.
     */
    private int uIndex = 0;

    /**
     * The end index into the list of ImageViews representing each direction of
     * the ship. Zero is the ship pointing to the right or zero degrees.
     */
    private int vIndex = 0;
    /**
     * Constructor will create a optionally create a gradient fill circle shape.
     * This sprite will contain a JavaFX Circle node.
     */
    private ImageView newAtom;
    private int points;

    public Atom(String imagePath, int points) {
        newAtom = new ImageView();
        Image shipImage = new Image(imagePath, true);
        newAtom.setImage(shipImage);
        this.node = newAtom;
        this.collidingNode = newAtom;
    }

    public Atom(String imagePath) {
        ImageView newAtom = new ImageView();
        Image shipImage = new Image(imagePath, true);
        newAtom.setImage(shipImage);
        this.node = newAtom;
        this.collidingNode = newAtom;
    }

    /**
     * Change the velocity of the current atom particle.
     */
    @Override
    public void update() {
        getNode().setTranslateX(getNode().getTranslateX() + vX);
        getNode().setTranslateY(getNode().getTranslateY() + vY);
    }

    /**
     * Returns a node casted as a JavaFX Circle shape.
     *
     * @return Circle shape representing JavaFX node for convenience.
     */
    public ImageView getImageViewNode() {
        return (ImageView) getNode();
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

    public void plotCourse(double screenX, double screenY, boolean thrust) {
        // get center of ship
        double sx = getCenterX();
        double sy = getCenterY();

        // get user's new turn position based on mouse click
        CustomVector v = new CustomVector(screenX, screenY, sx, sy);
        if (u == null) {
            u = new CustomVector(1, 0);
        }

        double atan2RadiansU = Math.atan2(u.y, u.x);
        double atan2DegreesU = Math.toDegrees(atan2RadiansU);

        double atan2RadiansV = Math.atan2(v.y, v.x);
        double atan2DegreesV = Math.toDegrees(atan2RadiansV);

        double angleBetweenUAndV = atan2DegreesV - atan2DegreesU;

        // if abs value is greater than 180 move counter clockwise
        //(or opposite of what is determined)
        double absAngleBetweenUAndV = Math.abs(angleBetweenUAndV);
        boolean goOtherWay = false;
        if (absAngleBetweenUAndV > 180) {
            if (angleBetweenUAndV < 0) {
                turnDirection = Atom.DIRECTION.COUNTER_CLOCKWISE;
                goOtherWay = true;
            } else if (angleBetweenUAndV > 0) {
                turnDirection = Atom.DIRECTION.CLOCKWISE;
                goOtherWay = true;
            } else {
                turnDirection = Atom.DIRECTION.NEITHER;
            }
        } else {
            if (angleBetweenUAndV < 0) {
                turnDirection = Atom.DIRECTION.CLOCKWISE;
            } else if (angleBetweenUAndV > 0) {
                turnDirection = Atom.DIRECTION.COUNTER_CLOCKWISE;
            } else {
                turnDirection = Atom.DIRECTION.NEITHER;
            }
        }

        double degreesToMove = absAngleBetweenUAndV;
        if (goOtherWay) {
            degreesToMove = TWO_PI_DEGREES - absAngleBetweenUAndV;
        }

        //int q = v.quadrant();
        uIndex = Math.round((float) (atan2DegreesU / UNIT_ANGLE_PER_FRAME));
        if (uIndex < 0) {
            uIndex = NUM_DIRECTIONS + uIndex;
        }
        vIndex = Math.round((float) (atan2DegreesV / UNIT_ANGLE_PER_FRAME));
        if (vIndex < 0) {
            vIndex = NUM_DIRECTIONS + vIndex;
        }
        if (thrust) {
            vX = Math.cos(atan2RadiansV) * THRUST_AMOUNT;
            vY = -Math.sin(atan2RadiansV) * THRUST_AMOUNT;
        }
        turnShip();

        u = v;
    }

    public double getCenterX() {
        return getNode().getTranslateX() + (newAtom.getBoundsInLocal().getWidth() / 2);
    }

    /**
     * The center Y coordinate of the current visible image. See
     * <code>getCurrentShipImage()</code> method.
     *
     * @return The scene or screen Y coordinate.
     */
    public double getCenterY() {
        return getNode().getTranslateY() + (newAtom.getBoundsInLocal().getHeight() / 2);
    }

    public void fire() {
        Missile fireMissile = new Missile(ResourcesManager.MISSILE_1);
        double slowDownAmt = 1.3f;
        double scaleBeginningMissle = 11;
        fireMissile.setVelocityX(Math.cos(Math.toRadians(uIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt));
        fireMissile.setVelocityY(Math.sin(Math.toRadians(-vIndex * UNIT_ANGLE_PER_FRAME)) * (MISSILE_THRUST_AMOUNT - slowDownAmt));

        // make the missile launch in the direction of the current direction of the ship nose. based on the
        // current frame (uIndex) into the list of image view nodes.
        RotatedShipImage shipImage = directionalShips.get(uIndex);

        // start to appear in the center of the ship to come out the direction of the nose of the ship.
        double offsetX = (shipImage.getBoundsInLocal().getWidth() - fireMissile.getNode().getBoundsInLocal().getWidth()) / 2;
        double offsetY = (shipImage.getBoundsInLocal().getHeight() - fireMissile.getNode().getBoundsInLocal().getHeight()) / 2;

        // initial launch of the missile   (multiply vector by 4 makes it appear at the nose of the ship)
        fireMissile.getNode().setTranslateX(getNode().getTranslateX() + (offsetX + (fireMissile.getVelocityX() * scaleBeginningMissle)));
        fireMissile.getNode().setTranslateY(getNode().getTranslateY() + (offsetY + (fireMissile.getVelocityY() * scaleBeginningMissle)));
    }

    private void turnShip() {

        final Duration oneFrameAmt = Duration.millis(MILLIS_PER_FRAME);
        RotatedShipImage startImage = directionalShips.get(uIndex);
        RotatedShipImage endImage = directionalShips.get(vIndex);
        List<KeyFrame> frames = new ArrayList<>();

        RotatedShipImage currImage = startImage;

        int i = 1;
        while (true) {

            final Node displayNode = currImage;

            KeyFrame oneFrame = new KeyFrame(oneFrameAmt.multiply(i), (javafx.event.ActionEvent event) -> {
                // make all ship images invisible
                for (RotatedShipImage shipImg : directionalShips) {
                    shipImg.setVisible(false);
                }
                // make current ship image visible
                displayNode.setVisible(true);

                // update the current index
//                    uIndex = directionalShips.indexOf(displayNode);
            }); // oneFrame

            frames.add(oneFrame);

            if (currImage == endImage) {
                break;
            }
            if (turnDirection == Atom.DIRECTION.CLOCKWISE) {
                currImage = currImage.getPrevRotatedImage();
            }
            if (turnDirection == Atom.DIRECTION.COUNTER_CLOCKWISE) {
                currImage = currImage.getNextRotatedImage();
            }
            i++;
        }

    }
}
