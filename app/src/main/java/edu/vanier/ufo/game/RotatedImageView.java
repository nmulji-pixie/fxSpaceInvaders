package edu.vanier.ufo.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public final class RotatedImageView extends ImageView {
    private final double baseRotate;
    private final Rotate rotation;
    private Point2D pivotMultiplier;
    
    public RotatedImageView(String imagePath, double baseRotate, Point2D pivot) {
        super(imagePath);
        
        this.rotation = new Rotate();
        this.getTransforms().add(this.rotation);

        this.setPivot(pivot);
        this.baseRotate = baseRotate;
        
        this.imageProperty().addListener((observable, oldValue, newValue) -> {
            this.setPivot(this.pivotMultiplier);
        });
    }
    
    public void turnToScene(double sceneX, double sceneY) {
        Point2D scene = this.localToScene(
            this.rotation.getPivotX(),
            this.rotation.getPivotY()
        );
        
        this.turnToDirection(sceneX - scene.getX(), sceneY - scene.getY());
    }
    
    public void turnToDirection(double x, double y) {
        if (x == 0 && y == 0)
            return;
        
        final double angle = Math.atan2(y, x);
        
        this.rotation.setAngle(Math.toDegrees(angle) + baseRotate);
    }
    
    public void setPivot(Point2D pivot) {
        this.pivotMultiplier = pivot;
        
        this.rotation.setPivotX(pivot.getX() * this.getWidth());
        this.rotation.setPivotY(pivot.getY() * this.getHeight());
        
        this.setTranslateX(this.getWidth() / 2 - this.rotation.getPivotX());
        this.setTranslateY(this.getHeight() / 2 - this.rotation.getPivotY());
    }
    
    public double getWidth() {
        return this.getImage().getWidth();
    }
    
    public double getHeight() {
        return this.getImage().getHeight();
    }
    
    public double getRotation() {
        return this.rotation.getAngle() - this.baseRotate;
    }
}
