package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.List;
import javafx.scene.paint.Color;

public class TankBot extends Tank {
    public TankBot(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y) {
        super(color, barrelType, x, y);
        this.setHealthBarColor(Color.RED);
        this.setPoints(color.getPoints());
        this.setCooldownTicks(5 * ResourcesManager.FRAMES_PER_SECOND);
    }
    
    @Override
    public void handleUpdate() {
        super.handleUpdate();
        
        List<Sprite> players = this.getEngine().getSpritesById("player");
        if (players.size() <= 0 || !(players.get(0) instanceof Tank))
            return;
        
        Tank player = (Tank)players.get(0);
        
        this.aimAt(player.getCenterX(), player.getCenterY());
        
        this.fire();
    }


}
