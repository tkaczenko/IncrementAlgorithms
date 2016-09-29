package io.github.tkaczenko.incrementalgorithms.graphic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import io.github.tkaczenko.incrementalgorithms.math.Algorithm;
import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class Character {
    private static final String TAG = Character.class.getSimpleName();

    private List<Point<Double>> mPoints;
    private int[][] mOrders;

    private ScreenConverter mScreenConverter;

    public void draw(Canvas canvas, Paint paint) {
        if (mScreenConverter == null) {
            Log.e(TAG, "ScreenConverter cannot be null");
            return;
        }
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
    }

    public List<Point<Double>> getPoints() {
        return mPoints;
    }

    public void setPoints(List<Point<Double>> points) {
        this.mPoints = points;
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
