package io.github.tkaczenko.incrementalgorithms.math.transformations;

/**
 * Created by tkaczenko on 08.10.16.
 */

public class Translate extends Transformation {
    public Translate() {
        initialize();
    }

    public void setTranslationX(double x) {
        getTransformMatrix().set(0, 2, x);
    }

    public double getTranslationX() {
        return getTransformMatrix().get(0, 2);
    }

    public void setTranslationY(double y) {
        getTransformMatrix().set(1, 2, y);
    }

    public double getTranslationY() {
        return getTransformMatrix().get(1, 2);
    }

    private void initialize() {
        Matrix matrix = getTransformMatrix();
        matrix.set(0, 0, 1.0);
        matrix.set(1, 1, 1.0);
        matrix.set(2, 2, 1.0);
    }
}
