package io.github.tkaczenko.incrementalgorithms.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.incrementalgorithms.graphic.Character;
import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class DrawView extends View {
    private static final double DEFAULT_MIN_X = -49.1;
    private static final double DEFAULT_MAX_X = 49.1;
    private static final double DEFAULT_MIN_Y = -40.1;
    private static final double DEFAULT_MAX_Y = 40.0;

    private int mBackgroundColor = Color.WHITE;
    private int mDrawColor = Color.BLUE;
    private float mWidth = 2.0F;

    private Character mLetter = new Character();
    private Character mNumber = new Character();

    private ScreenConverter mScreenConverter = new ScreenConverter();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setMinX(DEFAULT_MIN_X);
        setMaxX(DEFAULT_MAX_X);
        setMinY(DEFAULT_MIN_Y);
        setMaxY(DEFAULT_MAX_Y);

        fillBackground(canvas);
        initScreenConverter(canvas);
        initLetter();
        mLetter.draw(canvas, mPaint);
        initNumber();
        mNumber.draw(canvas, mPaint);
    }

    private void fillBackground(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
    }

    private void initScreenConverter(Canvas canvas) {
        mScreenConverter.setWidth(canvas.getWidth());
        mScreenConverter.setHeight(canvas.getHeight());
    }

    private void initLetter() {
        mPaint.setColor(mDrawColor);
        mPaint.setStrokeWidth(mWidth);

        List<Point<Double>> points = new ArrayList<>();

        // Real letters' coordinates of basic vertexes
        points.add(new Point<>(3.0, 0.0));
        points.add(new Point<>(1.0, 0.0));
        points.add(new Point<>(6.5, 10.0));
        points.add(new Point<>(12.0, 0.0));
        points.add(new Point<>(10.0, 0.0));
        points.add(new Point<>(9.0, 2.0));
        points.add(new Point<>(4.0, 2.0));
        points.add(new Point<>(5.0, 4.0));
        points.add(new Point<>(6.5, 7.0));
        points.add(new Point<>(8.0, 4.0));

        // Adjacency matrix for vertexes
        int size = points.size();
        int[][] orders = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                orders[i][j] = 0;
            }
        }
        orders[0][1] = 1;
        orders[0][6] = 1;
        orders[1][2] = 1;
        orders[2][3] = 1;
        orders[3][4] = 1;
        orders[4][5] = 1;
        orders[5][6] = 1;
        orders[7][8] = 1;
        orders[7][9] = 1;
        orders[8][9] = 1;

        mLetter.setPoints(points);
        mLetter.setOrders(orders);
        mLetter.setScreenConverter(mScreenConverter);
    }

    private void initNumber() {
        mPaint.setColor(mDrawColor);
        mPaint.setStrokeWidth(mWidth);

        List<Point<Double>> points = new ArrayList<>();

        // Real number's coordinates of basic vertexes
        points.add(new Point<>(15.0, 0.0));
        points.add(new Point<>(13.0, 1.0));
        points.add(new Point<>(13.0, 9.0));
        points.add(new Point<>(14.0, 10.0));
        points.add(new Point<>(18.0, 10.0));
        points.add(new Point<>(19.0, 9.0));
        points.add(new Point<>(19.0, 8.0));
        points.add(new Point<>(18.5, 8.0));
        points.add(new Point<>(18.0, 9.0));
        points.add(new Point<>(15.0, 9.0));
        points.add(new Point<>(14.0, 8.0));
        points.add(new Point<>(14.0, 7.0));
        points.add(new Point<>(15.0, 6.0));
        points.add(new Point<>(18.0, 6.0));
        points.add(new Point<>(19.0, 5.0));
        points.add(new Point<>(19.0, 1.0));
        points.add(new Point<>(17.0, 0.0));
        points.add(new Point<>(15.0, 1.0));
        points.add(new Point<>(14.0, 2.0));
        points.add(new Point<>(14.0, 4.0));
        points.add(new Point<>(15.0, 5.0));
        points.add(new Point<>(17.0, 5.0));
        points.add(new Point<>(18.0, 4.0));
        points.add(new Point<>(18.0, 2.0));
        points.add(new Point<>(17.0, 1.0));

        // Adjacency matrix for vertexes
        int size = points.size();
        int[][] orders = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                orders[i][j] = 0;
            }
        }
        int j = 1;
        for (int i = 0; i < size - 8; i++) {
            if (j == size) {
                break;
            }
            orders[i][j] = 1;
            j++;
        }
        j = size - 7;
        for (int i = size - 8; i < size; i++) {
            if (j == size) {
                break;
            }
            orders[i][j] = 1;
            j++;
        }
        orders[size - 9][size - 8] = 0;
        orders[0][size - 9] = 1;
        orders[size - 8][size - 1] = 1;

        mNumber.setPoints(points);
        mNumber.setOrders(orders);
        mNumber.setScreenConverter(mScreenConverter);
    }

    public void setDrawColor(int drawColor) {
        this.mDrawColor = drawColor;
    }

    public double getMinX() {
        return mScreenConverter.getMinX();
    }

    public void setMinX(double minX) {
        mScreenConverter.setMinX(minX);
    }

    public double getMaxX() {
        return mScreenConverter.getMaxX();
    }

    public void setMaxX(double maxX) {
        mScreenConverter.setMaxX(maxX);
    }

    public double getMinY() {
        return mScreenConverter.getMinY();
    }

    public void setMinY(double minY) {
        mScreenConverter.setMinY(minY);
    }

    public double getMaxY() {
        return mScreenConverter.getMaxY();
    }

    public void setMaxY(double maxY) {
        mScreenConverter.setMaxY(maxY);
    }

}
