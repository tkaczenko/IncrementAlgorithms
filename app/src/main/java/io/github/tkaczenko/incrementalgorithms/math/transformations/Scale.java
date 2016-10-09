package io.github.tkaczenko.incrementalgorithms.math.transformations;

/**
 * Created by tkaczenko on 09.10.16.
 */
public class Scale extends Transformation {
    public Scale() {
        initMatrix();
    }

    public void setScaleByX(double scaleByX) {
        getTransformMatrix().set(0, 0, scaleByX);
    }

    public double getScaleByX() {
        return getTransformMatrix().get(0, 0);
    }

    public void setScaleByY(double scaleByY) {
        getTransformMatrix().set(1, 1, scaleByY);
    }

    public double getScaleByY() {
        return getTransformMatrix().get(1, 1);
    }

    private void initMatrix() {
        Matrix matrix = getTransformMatrix();
        matrix.set(0, 0, 1.0);
        matrix.set(1, 1, 1.0);
        matrix.set(2, 2, 1.0);
    }
}
