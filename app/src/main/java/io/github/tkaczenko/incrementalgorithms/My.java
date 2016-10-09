package io.github.tkaczenko.incrementalgorithms;

import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Matrix;

/**
 * Created by tkaczenko on 09.10.16.
 */

public class My {
    private Matrix transformMatrix = new Matrix(3, 3);

    public Point<Double> transform(Point<Double> point) {
        Matrix sourceXYMatrix = new Matrix(3, 1);
        sourceXYMatrix.set(0, 0, point.getX());
        sourceXYMatrix.set(1, 0, point.getY());
        sourceXYMatrix.set(2, 0, 1.0);
        Matrix result = transformMatrix.multiply(sourceXYMatrix);
        if (result == null) {
            return null;
        }
        return new Point<Double>(
                (double) Math.round(result.get(0, 0)),
                (double) Math.round(result.get(1, 0)));
    }


    protected Matrix getTransformMatrix() {
        return transformMatrix;
    }
}
