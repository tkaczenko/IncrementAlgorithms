package io.github.tkaczenko.incrementalgorithms.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.tkaczenko.incrementalgorithms.enumerations.Mode;
import io.github.tkaczenko.incrementalgorithms.graphic.Character;
import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Rotate;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Scale;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Translate;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class DrawView extends View implements View.OnTouchListener {
    private static final double DEFAULT_MIN_X = -49.1;
    private static final double DEFAULT_MAX_X = 49.1;
    private static final double DEFAULT_MIN_Y = -40.1;
    private static final double DEFAULT_MAX_Y = 40.0;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private Mode mode = Mode.NONE;
    private double[] lastEvent;
    private double d;
    private double newRot;

    private ScaleGestureDetector mScaleDetector;
    private Point<Double> start;

    private int mBackgroundColor = Color.WHITE;
    private int mDrawColor = Color.BLUE;
    private float mWidth = 2.0F;

    private Character mLetter = new Character();
    private Character mNumber = new Character();

    private Translate mTranslate = new Translate();
    private Scale mScale = new Scale();

    private Rotate mRotate = new Rotate();

    private ScreenConverter mScreenConverter = new ScreenConverter();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DrawView(Context context) {
        super(context);
        setOnTouchListener(this);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        initialize();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        initialize();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        fillBackground(canvas);
        initScreenConverter(canvas);
        mPaint.setColor(mDrawColor);
        mPaint.setStrokeWidth(mWidth);
        mLetter.draw(canvas, mPaint);
        mNumber.draw(canvas, mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        double x = mScreenConverter.toWorldX((int) event.getX());
        double y = mScreenConverter.toWorldY((int) event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                searchMinMaxOfAxises();
                if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                    mode = Mode.DRAG;
                    start = new Point<>(x, y);
                }
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Toast.makeText(getContext(), "ACTION_POINTER_DOWN", Toast.LENGTH_SHORT).show();
                lastEvent = new double[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
            case MotionEvent.ACTION_MOVE:
                if (mode.equals(Mode.DRAG)) {
                    searchMinMaxOfAxises();
                    double deltaX = x - start.getX();
                    double deltaY = y - start.getY();
                    if (isFitToScreen(deltaX, deltaY, true)) {
                        mTranslate.setTranslationX(deltaX);
                        mTranslate.setTranslationY(deltaY);
                        mLetter.getTransformations().add(mTranslate);
                        mNumber.getTransformations().add(mTranslate);

                        start.setX(x);
                        start.setY(y);
                        invalidate();
                    }
                } else if (lastEvent != null && event.getPointerCount() == 3) {
                    Toast.makeText(getContext(), "ACTION_ROTATE", Toast.LENGTH_SHORT).show();
                    newRot = rotation(event);
                    double r = newRot - d;
                    mRotate.setRotation(r);
                    mLetter.getTransformations().add(mRotate);
                    mNumber.getTransformations().add(mRotate);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = Mode.NONE;
                break;
        }
        mScaleDetector.onTouchEvent(event);
        return true;
    }

    public void rotate() {
        mLetter.getTransformations().add(mRotate);
        mNumber.getTransformations().add(mRotate);
        invalidate();
    }

    private double rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        return Math.atan2(delta_y, delta_x);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private double lastSpanX;
        private double lastSpanY;

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastSpanX = detector.getCurrentSpanX();
            lastSpanY = detector.getCurrentSpanY();

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            double spanX = detector.getCurrentSpanX();
            double spanY = detector.getCurrentSpanY();

            double newWidth = spanX / lastSpanX;
            double newHeight = spanY / lastSpanY;

            searchMinMaxOfAxises();

            if (isFitToScreen(newWidth, newHeight, false)) {
                mScale.setScaleByX(newWidth);
                mScale.setScaleByY(newHeight);

                mLetter.getTransformations().add(mScale);
                mNumber.getTransformations().add(mScale);

                lastSpanX = spanX;
                lastSpanY = spanY;
                invalidate();
            }
            return true;
        }

    }

    private boolean isFitToScreen(double deltaX, double deltaY, boolean translate) {
        return (translate) ? mScreenConverter.toScreenX(minX + deltaX) > 0
                && mScreenConverter.toScreenX(maxX + deltaX) <
                mScreenConverter.getWidth()
                && mScreenConverter.toScreenY(maxY + deltaY) > 0
                && mScreenConverter.toScreenY(minY + deltaY) <
                mScreenConverter.getHeight()
                : mScreenConverter.toScreenX(minX * deltaX) > 0
                && mScreenConverter.toScreenX(maxX * deltaX) <
                mScreenConverter.getWidth()
                && mScreenConverter.toScreenY(maxY * deltaY) > 0
                && mScreenConverter.toScreenY(minY * deltaY) <
                mScreenConverter.getHeight();
    }

    private void searchMinMaxOfAxises() {
        double a = mLetter.getMinX();
        double b = mNumber.getMinX();
        minX = Math.min(a, b);

        a = mLetter.getMaxX();
        b = mNumber.getMaxX();
        maxX = Math.max(a, b);

        a = mLetter.getMinY();
        b = mNumber.getMinX();
        minY = Math.min(a, b);

        a = mLetter.getMaxY();
        b = mNumber.getMaxY();
        maxY = Math.max(a, b);
    }

    private void fillBackground(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
    }

    private void initScreenConverter(Canvas canvas) {
        mScreenConverter.setWidth(canvas.getWidth());
        mScreenConverter.setHeight(canvas.getHeight());
    }

    private void initLetter() {
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

    private void initialize() {
        setMinX(DEFAULT_MIN_X);
        setMaxX(DEFAULT_MAX_X);
        setMinY(DEFAULT_MIN_Y);
        setMaxY(DEFAULT_MAX_Y);

        initLetter();
        initNumber();
    }

    public Rotate getmRotate() {
        return mRotate;
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
