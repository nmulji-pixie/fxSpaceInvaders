package edu.vanier.ufo.game;

import javafx.scene.image.ImageView;

/**
 * Represents a double link list to assist in the rotation of the ship. This
 * helps to move clockwise and counter clockwise.
 */
public class RotatedShipImage extends ImageView {

    private RotatedShipImage next;
    private RotatedShipImage prev;

    public RotatedShipImage getNextRotatedImage() {
        return next;
    }

    public void setNextRotatedImage(RotatedShipImage next) {
        this.next = next;
    }

    public RotatedShipImage getPrevRotatedImage() {
        return prev;
    }

    public void setPrevRotatedImage(RotatedShipImage prev) {
        this.prev = prev;
    }

}
