package io.github.tkaczenko.incrementalgorithms.math.transformations;

import io.github.tkaczenko.incrementalgorithms.graphic.Point;

/**
 * Created by tkaczenko on 10.10.16.
 */
public class Rotate extends Transformation {
    private Point<Double> mCenterPoint = null;
    private double angle = 0.0;

    public Rotate() {
        getTransformMatrix().set(2, 2, 1.0);
        setRotationDegree(0.0);
    }

    @Override
    public Point<Double> transform(Point<Double> point) {
        if (mCenterPoint == null) {
            return super.transform(point);
        }
        Point<Double> sourcePoint = new Point<>();
        sourcePoint.setX(point.getX() - mCenterPoint.getX());
        sourcePoint.setY(point.getY() - mCenterPoint.getY());
        Point<Double> result = super.transform(sourcePoint);
        result.setX(result.getX() + mCenterPoint.getX());
        result.setY(result.getY() + mCenterPoint.getY());
        return result;
    }

    public void setRotation(double angle) {
        this.angle = angle;
        initialize();
    }

    public void setRotationDegree(double angleDegree) {
        setRotation(angleDegree / 180.0 * Math.PI);
    }

    public Point getCenterPoint() {
        return mCenterPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.mCenterPoint = centerPoint;
    }

    private void initialize() {
        double sinA = Math.sin(-angle);
        double cosA = Math.cos(-angle);
        getTransformMatrix().set(0, 0, cosA);
        getTransformMatrix().set(0, 1, -sinA);
        getTransformMatrix().set(1, 0, sinA);
        getTransformMatrix().set(1, 1, cosA);
    }

    public double getRotationDegree() {
        return (getRotation() / Math.PI * 180.0);
    }

    private double getRotation() {
        return angle;
    }
}
