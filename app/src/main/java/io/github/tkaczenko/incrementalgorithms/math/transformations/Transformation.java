package io.github.tkaczenko.incrementalgorithms.math.transformations;

import io.github.tkaczenko.incrementalgorithms.graphic.Point;

/**
 * Created by tkaczenko on 08.10.16.
 */

public class Transformation {
    private Matrix transformMatrix = new Matrix(3, 3);

    public Point<Double> transform(Point<Double> point) {
        Matrix sourceXyMatrix = new Matrix(3, 1);
        sourceXyMatrix.set(0, 0, point.getX());
        sourceXyMatrix.set(1, 0, point.getY());
        sourceXyMatrix.set(2, 0, 1.0);
        Matrix resultXyMatrix = transformMatrix.multiply(sourceXyMatrix);
        if (resultXyMatrix == null) {
            return null;
        }
        return new Point(resultXyMatrix.get(0, 0), resultXyMatrix.get(1, 0));
    }

    protected Matrix getTransformMatrix() {
        return transformMatrix;
    }
}
