package edu.vanier.ufo.game;

import edu.vanier.ufo.engine.Sprite;
import edu.vanier.ufo.helpers.ResourcesManager;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class TankBot extends Tank {
    public enum Difficulty {
        EASY(
            1 * ResourcesManager.FRAMES_PER_SECOND,
            3 * ResourcesManager.FRAMES_PER_SECOND
        ),
        MEDIUM(
            (int) (0.5 * ResourcesManager.FRAMES_PER_SECOND),
            2 * ResourcesManager.FRAMES_PER_SECOND),
        HARD(
            0,
            (int) (0.7 * ResourcesManager.FRAMES_PER_SECOND)
        );
        
        private final int minExtraCooldownTicks, maxExtraCooldownTicks;
        
        Difficulty(int minExtraCooldownTicks, int maxExtraCooldownTicks) {
            this.minExtraCooldownTicks = minExtraCooldownTicks;
            this.maxExtraCooldownTicks = maxExtraCooldownTicks;
        }

        public int getMinExtraCooldownTicks() {
            return minExtraCooldownTicks;
        }

        public int getMaxExtraCooldownTicks() {
            return maxExtraCooldownTicks;
        }
    }

    private final Difficulty difficulty;
    private final Random rng;
    
    public TankBot(ResourcesManager.TankColor color, ResourcesManager.BarrelType barrelType, double x, double y, Difficulty difficulty) {
        super(color, barrelType, x, y);
        this.setHealthBarColor(Color.RED);
        this.setPoints(color.getPoints());
        this.addId("tankbot");
        this.difficulty = difficulty;
        this.rng = new Random();
        this.addExtraCooldown();
    }
    
    @Override
    public void handleUpdate() {
        super.handleUpdate();
        
        List<Sprite> players = this.getEngine().getSpritesById("player");
        if (players.size() <= 0 || !(players.get(0) instanceof Tank))
            return;
        
        Tank player = (Tank)players.get(0);
        
        this.aimAt(player.getCenterX(), player.getCenterY());
        
        if (this.fire())
            this.addExtraCooldown();
    }

    private void addExtraCooldown() {
        this.setCooldownTicks(
            this.getCooldownTicks() +
            rng.nextInt(
                this.difficulty.getMinExtraCooldownTicks(),
                this.difficulty.getMaxExtraCooldownTicks()
            )
        );
    }
}
