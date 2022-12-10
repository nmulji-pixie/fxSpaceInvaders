package edu.vanier.ufo.game;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public final class RotatedImageView extends Group {
    private final double baseRotate;
    private final ImageView imageView;
    private final Rotate rotation;
    
    public RotatedImageView(String imagePath, double baseRotate, Point2D pivot) {
        super();
        
        this.rotation = new Rotate();
        this.imageView = new ImageView(imagePath);
        this.imageView.getTransforms().add(this.rotation);

        this.setPivot(pivot);
        this.baseRotate = baseRotate;
        
        
        this.getChildren().add(imageView);
                
    }
    
    public void turnToScene(double sceneX, double sceneY) {
        Point2D scene = this.imageView.localToScene(
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
        this.rotation.setPivotX(pivot.getX() * this.getWidth());
        this.rotation.setPivotY(pivot.getY() * this.getHeight());
        this.imageView.setTranslateX(-this.rotation.getPivotX());
        this.imageView.setTranslateY(-this.rotation.getPivotY());
    }
    
    public double getWidth() {
        return this.imageView.getImage().getWidth();
    }
    
    public double getHeight() {
        return this.imageView.getImage().getHeight();
    }
}
