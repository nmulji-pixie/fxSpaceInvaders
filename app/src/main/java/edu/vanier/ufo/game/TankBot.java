package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.GameEngine;
import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TankBot extends Tank {
    public TankBot(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y) {
        super(color, barrelType, x, y);
    }

    public void implode(GameEngine gameEngine){

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.node.opacityProperty(), 1), new KeyValue(this.node.visibleProperty(), true)), new KeyFrame(Duration.millis(200), new KeyValue(this.node.opacityProperty(), 0), new KeyValue(this.node.visibleProperty(), false)));
        timeline.setOnFinished(e -> {
            this.isDead = true;
        });
        timeline.play();
    }
    public void handleDeath(GameEngine gameWorld) {
        this.implode(gameWorld);
        gameWorld.getSpriteManager().addSpritesToBeRemoved(this);
    }
}
