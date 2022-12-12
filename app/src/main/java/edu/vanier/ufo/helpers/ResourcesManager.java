package edu.vanier.ufo.helpers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.*;


/**
 * A resource manager providing useful resource definitions used in this game.
 *
 * @author Sleiman
 */
public class ResourcesManager {

    public enum TankColor {
        SAND("sand", 20),
        GREEN("green", 20),
        RED("red", 20),
        BLUE("blue", 20),
        DARK("dark", 20);
        
        private final String pathValue;
        private final int points;
 
        TankColor(String pathValue, int points) {
            this.pathValue = pathValue; this.points = points;
        }

        public String getPathValue() {
            return this.pathValue;
        }

        public int getPoints() {
            return points;
        }
    }
    
    public enum BarrelType {
        NORMAL(2, 1, 25, (int)(0.2 * FRAMES_PER_SECOND)),
        THICK(1, 2, 50, (int)(FRAMES_PER_SECOND)),
        LONG(3, 3, 30, (int)(0.4 * FRAMES_PER_SECOND));
        
        private final int barrelIndex;
        private final int bulletIndex;
        private final double damage;
        private final int cooldownFrames;
 
        BarrelType(int barrelIndex, int bulletIndex, double damage, int cooldownFrames) {
            this.barrelIndex = barrelIndex;
            this.bulletIndex = bulletIndex;
            this.damage = damage;
            this.cooldownFrames = cooldownFrames;
        }

        public int getBarrelIndex() {
            return this.barrelIndex;
        }
        
        public int getBulletIndex() {
            return this.bulletIndex;
        }
        
        public double getDamage() {
            return this.damage;
        }
        
        public int getCooldownFrames() {
            return this.cooldownFrames;
        }
    }
    
    public enum ExplosionKind {
        NORMAL(5, ""),
        SMOKE(5, "Smoke");
        
        private final int length;
        private final String type;
        
        ExplosionKind(int length, String type) {
            this.type = type;
            this.length = length;
        }
        
        public String getType() {
            return this.type;
        }
        
        public int getLength() {
            return this.length;
        }
    }


    /**
     * Used to control the speed of the game.
     */
    public static final int FRAMES_PER_SECOND = 85;
    public static final double MAX_HEALTH = 100;
    
    private static final String RESOURCES_FOLDER = "";
    private static final String IMAGES_FOLDER = RESOURCES_FOLDER + "images/tank_kenney/Retina/";
    private static final String SOUNDS_FOLDER = RESOURCES_FOLDER + "sounds/";
    
    // Tank format strings
    private static final String TANK_BODY_FMT = IMAGES_FOLDER + "tankBody_%s_outline.png"; 
    private static final String TANK_BARREL_FMT = IMAGES_FOLDER + "tank%s_barrel%d_outline.png"; 
    private static final String TANK_BULLET_FMT = IMAGES_FOLDER + "bullet%s%d.png";
    private static final String TANK_SHOT_FMT = IMAGES_FOLDER + "shot%s.png";

    // Background
    public static final String BACKGROUND = IMAGES_FOLDER + "tileGrass_transitionS.png";

    // Sounds
    public static final String SOUND_SHOOT = SOUNDS_FOLDER + "explosions/explosion02.wav";
    public static final String SOUND_EXPLOSION = SOUNDS_FOLDER + "explosions/explosion01.wav";
    
    // Explosions
    private static final String EXPLOSION_FMT = IMAGES_FOLDER + "explosion%s%d.png";
    
    public static HashMap<Integer, String> getInvaderSprites() {
        HashMap<Integer, String> invaders = new HashMap<Integer, String>();
        invaders.put(1, ResourcesManager.IMAGES_FOLDER + "large_invader_b.png");
        invaders.put(2, ResourcesManager.IMAGES_FOLDER + "small_invader_b.png");
        return invaders;
    }
    
    public static String getTankBody(TankColor color) {
        return String.format(TANK_BODY_FMT, color.getPathValue());
    }
    
    public static String getTankBarrel(TankColor color, BarrelType barrelType) {
        return String.format(TANK_BARREL_FMT, StringHelper.toTitleCase(color.getPathValue()), barrelType.getBarrelIndex());
    }
    
    public static String getTankBullet(TankColor color, BarrelType barrelType) {
        return String.format(TANK_BULLET_FMT, StringHelper.toTitleCase(color.getPathValue()), barrelType.getBulletIndex());
    }
    
    public static String getTankShot(BarrelType barrelType) {
        return String.format(TANK_SHOT_FMT,
            switch (barrelType) {
                case LONG -> "Orange";
                case NORMAL -> "Thin";
                case THICK -> "Red";
                default -> "Large";
            }
        );
    }
    
    public static List<String> getExplosion(ExplosionKind explosionType) {
        List<String> explosionSprites = new ArrayList<>(explosionType.getLength());
        
        for (int i = 1; i <= explosionType.getLength(); ++i)
            explosionSprites.add(String.format(EXPLOSION_FMT, explosionType.getType(), i));
        
        return explosionSprites;
    }
}
