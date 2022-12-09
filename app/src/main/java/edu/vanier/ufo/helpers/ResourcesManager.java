package edu.vanier.ufo.helpers;

import java.util.HashMap;


/**
 * A resource manager providing useful resource definitions used in this game.
 *
 * @author Sleiman
 */
public class ResourcesManager {

    public enum TankColor {
        SAND("sand"),
        GREEN("green"),
        RED("red"),
        BLUE("blue"),
        DARK("dark");
        
        private String pathValue;
 
        TankColor(String pathValue) {
            this.pathValue = pathValue;
        }

        public String getPathValue() {
            return this.pathValue;
        }
    }
    
    public enum BarrelType {
        NORMAL(2),
        THICK(1),
        LONG(3);
        
        private int pathIndex;
 
        BarrelType(int pathIndex) {
            this.pathIndex = pathIndex;
        }

        public int getPathIndex() {
            return this.pathIndex;
        }
    }
    
    /**
     * Used to control the speed of the game.
     */
    public static final int FRAMES_PER_SECOND = 85;
    private static final String RESOURCES_FOLDER = "";
    private static final String IMAGES_FOLDER = RESOURCES_FOLDER + "images/tank_kenney/Retina/";
    private static final String SOUNDS_FOLDER = RESOURCES_FOLDER + "sounds/";
    
    // Tank format strings
    private static final String TANK_BODY_FMT = IMAGES_FOLDER + "tankBody_%s_outline.png"; 
    private static final String TANK_BARREL_FMT = IMAGES_FOLDER + "tank%s_barrel%d_outline.png"; 
    private static final String TANK_BULLET_FMT = IMAGES_FOLDER + "bullet%s%d.png";

    // Background
    public static final String BACKGROUND = IMAGES_FOLDER + "tileGrass_transitionS.png";

    // Tanks
    private static final String TANK_FOLDER = IMAGES_FOLDER + "tank/";
    public static final String TANK_1 = TANK_FOLDER + "tanks_tankDesert3.png";
    public static final String TANK_2 = TANK_FOLDER + "tanks_tankGreen4.png";
    public static final String TANK_3 = TANK_FOLDER + "tanks_tankGreen5.png";
    public static final String TANK_4 = TANK_FOLDER + "tanks_tankGrey1.png";
    public static final String TANK_5 = TANK_FOLDER + "tanks_tankGrey2.png";
    public static final String TANK_6 = TANK_FOLDER + "tanks_tankNavy1.png";

    private static final String MISSILE_FOLDER = RESOURCES_FOLDER + IMAGES_FOLDER + "missile/";
    public static final String MISSILE_1 = MISSILE_FOLDER + "tank_bullet3.png";
    public static final String MISSILE_2 = MISSILE_FOLDER + "tank_bullet4.png";
    public static final String MISSILE_3 = MISSILE_FOLDER + "tank_bulletFly3.png";

    //    public static final String ROCKET_SMALL = IMAGES_FOLDER + "rocket.png";
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
        return String.format(TANK_BARREL_FMT, StringHelper.toTitleCase(color.getPathValue()), barrelType.getPathIndex());
    }
    
    public static String getTankBullet(TankColor color, BarrelType barrelType) {
        return String.format(TANK_BULLET_FMT, StringHelper.toTitleCase(color.getPathValue()), barrelType.getPathIndex());
    }
}
