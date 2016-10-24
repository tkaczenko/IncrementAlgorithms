package io.github.tkaczenko.incrementalgorithms.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.github.tkaczenko.incrementalgorithms.math.Algorithm;
import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Transformation;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class Character {
    private static final String TAG = Character.class.getSimpleName();

    private List<Point<Double>> mPoints;
    private int[][] mOrders;
    private Set<Transformation> mTransformations = new LinkedHashSet<>();

    private ScreenConverter mScreenConverter;

    public void draw(Canvas canvas, Paint paint) {
        if (mScreenConverter == null) {
            Log.e(TAG, "ScreenConverter cannot be null");
            return;
        }
        transform();
        Point<Double> startPoint, stopPoint;
        for (int i = 0; i < mOrders.length; i++) {
            for (int j = 0; j < mOrders[0].length; j++) {
                if (mOrders[i][j] == 1) {
                    startPoint = mPoints.get(i);
                    stopPoint = mPoints.get(j);

                    if (startPoint.getY() < stopPoint.getY()) {
                        Point<Double> temp = startPoint;
                        startPoint = stopPoint;
                        stopPoint = temp;
                    }

                    Algorithm.drawLine(mScreenConverter.toScreenX(startPoint.getX()),
                            mScreenConverter.toScreenY(startPoint.getY()),
                            mScreenConverter.toScreenX(stopPoint.getX()),
                            mScreenConverter.toScreenY(stopPoint.getY()),
                            canvas, paint);
                }
            }
        }
        mTransformations.clear();
    }

    private void transform() {
        for (int i = 0; i < mPoints.size(); i++) {
            mPoints.set(i, transform(mPoints.get(i)));
        }
    }

    private Point<Double> transform(Point<Double> point) {
        Point<Double> newPoint = point;
        if (mTransformations == null) {
            return newPoint;
        }
        for (Transformation transformation : mTransformations) {
            newPoint = transformation.transform(newPoint);
        }
        return newPoint;
    }

    public double getMinX() {
        double minX = mPoints.get(0).getX();
        for (Point<Double> point :
                mPoints) {
            if (point.getX() < minX) {
                minX = point.getX();
            }
        }
        return minX;
    }

    public double getMaxX() {
        double maxX = mPoints.get(0).getX();
        for (Point<Double> point :
                mPoints) {
            if (point.getX() > maxX) {
                maxX = point.getX();
            }
        }
        return maxX;
    }

    public double getMinY() {
        double minY = mPoints.get(0).getY();
        for (Point<Double> point :
                mPoints) {
            if (point.getY() < minY) {
                minY = point.getY();
            }
        }
        return minY;
    }

    public double getMaxY() {
        double maxY = mPoints.get(0).getY();
        for (Point<Double> point :
                mPoints) {
            if (point.getY() > maxY) {
                maxY = point.getY();
            }
        }
        return maxY;
    }

    public List<Point<Double>> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point<Double>> points) {
        this.mPoints = points;
    }

    public Set<Transformation> getTransformations() {
        return mTransformations;
    }

    public int[][] getOrder() {
        return mOrders;
    }

    public void setOrders(int[][] orders) {
        this.mOrders = orders;
    }

    public void setScreenConverter(ScreenConverter screenConverter) {
        this.mScreenConverter = screenConverter;
    }
}
