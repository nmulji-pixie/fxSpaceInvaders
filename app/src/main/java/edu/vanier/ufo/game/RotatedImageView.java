package edu.vanier.ufo.game;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class RotatedImageView extends Group {
    private final Point2D pivotPoint;
    private final double baseRotate;
    private final ImageView imageView;
    
    public RotatedImageView(String imagePath, double baseRotate, Point2D pivot) {
        super();
        
        this.imageView = new ImageView(imagePath);
        
        this.getChildren().add(imageView);
        
        this.baseRotate = baseRotate;
        this.pivotPoint = pivot;
        
        this.getChildren().add(new Circle(1, this.getWidth() * this.pivotPoint.getX(), this.getHeight() * this.pivotPoint.getY()));
    }
    
    public void turnToScene(double sceneX, double sceneY) {
        double px = this.getWidth() * this.pivotPoint.getX();
        double py = this.getHeight() * this.pivotPoint.getY();
        
        Point2D scene = this.imageView.localToScene(px, py);
        
        this.turnToDirection(scene.getX() - sceneX, scene.getY() - sceneY);
    }
    
    public void turnToDirection(double x, double y) {
        double angle = Math.atan2(y, x);
        
        this.imageView.setTranslateX(
            Math.cos(angle) * (this.imageView.getTranslateX() - this.pivotPoint.getX()) -
            Math.sin(angle) * (this.imageView.getTranslateY() - this.pivotPoint.getY()) +
            this.pivotPoint.getX()
        );
        
        this.imageView.setTranslateY(
            Math.sin(angle) * (this.imageView.getTranslateX() - this.pivotPoint.getX()) +
            Math.cos(angle) * (this.imageView.getTranslateY() - this.pivotPoint.getY()) +
            this.pivotPoint.getY()
        );
        
        this.imageView.setRotate(Math.toDegrees(angle) + baseRotate);
    }
    
    public double getWidth() {
        return this.imageView.getImage().getWidth();
    }
    
    public double getHeight() {
        return this.imageView.getImage().getHeight();
    }
}
