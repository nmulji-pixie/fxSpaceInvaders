package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Explosion extends Sprite {
    private final static int INTERVAL_TICKS = (int)(0.05 * ResourcesManager.FRAMES_PER_SECOND);
    private final List<Image> explosions;
    private int intervalTicks;
    private ImageView view;
    
    public Explosion(ResourcesManager.ExplosionKind explosionKind, double centerX, double centerY) {
        this.intervalTicks = 0;
        this.explosions = ResourcesManager.getExplosion(explosionKind).stream().map(Image::new).toList();
        this.view = new ImageView();
        
        this.view.setTranslateX(centerX);
        this.view.setTranslateY(centerY);
        
        this.view.imageProperty().addListener((observable, oldValue, newValue) -> {
            this.view.setTranslateX(centerX - newValue.getWidth() / 2);
            this.view.setTranslateY(centerY - newValue.getHeight() / 2);
        });
        
        this.setNode(this.view);
        this.setCollisionBounds(null);
    }

    @Override
    public void update() {
        if (this.intervalTicks / INTERVAL_TICKS >= this.explosions.size())
            this.die();
        else if (this.intervalTicks % INTERVAL_TICKS == 0)
            this.view.setImage(this.explosions.get(this.intervalTicks / INTERVAL_TICKS));
        
        ++intervalTicks;
    }
    
    @Override
    protected void handleDeath() {
    }
    
    public void setScale(double scale) {
       this.node.setScaleX(scale); 
       this.node.setScaleY(scale); 
    }
}
