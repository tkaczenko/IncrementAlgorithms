package io.github.tkaczenko.incrementalgorithms.math;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class Algorithm {
    public static void drawLine(int startX, int startY,
                                int stopX, int stopY, Canvas canvas, Paint paint) {
        int incX, incY, d;
        int xErr = 0, yErr = 0;
        int dx = stopX - startX;
        int dy = stopY - startY;
        if (dx > 0) {
            incX = 1;
        } else if (dx < 0) {
            incX = -1;
        } else {
            incX = 0;
        }
        if (dy > 0) {
            incY = 1;
        } else if (dx < 0) {
            incY = -1;
        } else {
            incY = 0;
        }
        dx = Math.abs(dx);
        dy = Math.abs(dy);
        d = Math.max(dx, dy);
        int x = startX, y = startY;
        canvas.drawPoint(x, y, paint);
        for (int i = 0; i < d; i++) {
            xErr += dx;
            yErr += dy;
            if (xErr > d) {
                xErr -= d;
                x += incX;
            }
            if (yErr > d) {
                yErr -= d;
                y += incY;
            }
            canvas.drawPoint(x, y, paint);
        }
    }

    public static void drawCircle(int centerX, int centerY, int radius,
                                  Canvas canvas, Paint paint) {
        int x = 0;
        int y = radius;
        int d = 3 - 2 * y;
        while (x <= y) {
            sim(x, y, centerX, centerY, canvas, paint);
            if (d < 0) {
                d = d + 4 * x + 6;
            } else {
                d = d + 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
    }

    private static void sim(int x, int y,
                            int centerX, int centerY, Canvas canvas, Paint paint) {
        canvas.drawPoint(x + centerX, y + centerY, paint);
        canvas.drawPoint(x + centerX, -y + centerY, paint);
        canvas.drawPoint(-x + centerX, -y + centerY, paint);
        canvas.drawPoint(-x + centerX, y + centerY, paint);
        canvas.drawPoint(y + centerX, x + centerY, paint);
        canvas.drawPoint(y + centerX, -x + centerY, paint);
        canvas.drawPoint(-y + centerX, -x + centerY, paint);
        canvas.drawPoint(-y + centerX, x + centerY, paint);
    }

}
