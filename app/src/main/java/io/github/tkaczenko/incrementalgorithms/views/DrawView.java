package io.github.tkaczenko.incrementalgorithms.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.github.tkaczenko.incrementalgorithms.enumerations.Mode;
import io.github.tkaczenko.incrementalgorithms.graphic.Character;
import io.github.tkaczenko.incrementalgorithms.graphic.Point;
import io.github.tkaczenko.incrementalgorithms.math.ScreenConverter;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Scale;
import io.github.tkaczenko.incrementalgorithms.math.transformations.Transformation;
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

    private double prevDist;
    private Mode mode = Mode.NONE;

    // remember some things for zooming
    private Point<Double> start;
    private Point<Double> mid;

    private double[] lastEvent = null;
    private int mBackgroundColor = Color.WHITE;
    private int mDrawColor = Color.BLUE;
    private float mWidth = 2.0F;

    private Translate mTranslate = new Translate();
    private Scale mScale = new Scale();

    private Character mLetter = new Character();
    private Character mNumber = new Character();

    private ScreenConverter mScreenConverter = new ScreenConverter();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public DrawView(Context context) {
        super(context);
        setOnTouchListener(this);
        initialize();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        initialize();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
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

    private void initialize() {
        setMinX(DEFAULT_MIN_X);
        setMaxX(DEFAULT_MAX_X);
        setMinY(DEFAULT_MIN_Y);
        setMaxY(DEFAULT_MAX_Y);

        initLetter();
        initNumber();
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
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                prevDist = spacing(event);
                midPoint(mid, event);
                mode = Mode.ZOOM;
            case MotionEvent.ACTION_MOVE:
                if (mode.equals(Mode.DRAG)) {
                    searchMinMaxOfAxises();
                    double deltaX = x - start.getX();
                    double deltaY = y - start.getY();
                    if (mScreenConverter.toScreenX(minX + deltaX) > 0
                            && mScreenConverter.toScreenX(maxX + deltaX) <
                            mScreenConverter.getWidth()
                            && mScreenConverter.toScreenY(maxY + deltaY) > 0
                            && mScreenConverter.toScreenY(minY + deltaY) <
                            mScreenConverter.getHeight()
                            ) {
                        mTranslate.setTranslationX(deltaX);
                        mTranslate.setTranslationY(deltaY);
                        mLetter.getTransformations().add(mTranslate);
                        mNumber.getTransformations().add(mTranslate);

                        start.setX(x);
                        start.setY(y);
                        invalidate();
                    }
                } else if (mode.equals(Mode.ZOOM)) {
                    double newDist = spacing(event);
                    if (newDist > 10D) {
                        double scale = (newDist / prevDist);
                        mScale.setScaleByX(mid.getX());
                        mScale.setScaleByY(mid.getY());
                        mLetter.getTransformations().add(mScale);
                        mNumber.getTransformations().add(mScale);
                        invalidate();
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        // TODO: 09.10.16 Implement rotation
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = Mode.NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = Mode.NONE;
                lastEvent = null;
                break;
        }
        return true;
    }

    private double spacing(MotionEvent event) {
        double x = mScreenConverter.toWorldX((int) event.getX(0))
                - mScreenConverter.toWorldX((int) event.getX(1));
        double y = mScreenConverter.toWorldY((int) event.getY(0))
                - mScreenConverter.toWorldY((int) event.getY(1));
        return Math.sqrt(x * x + y * y);
    }

    private void midPoint(Point<Double> point, MotionEvent event) {
        double x = mScreenConverter.toWorldX((int) event.getX(0))
                + mScreenConverter.toWorldX((int) event.getX(1));
        double y = mScreenConverter.toWorldY((int) event.getY(0))
                + mScreenConverter.toWorldY((int) event.getY(1));
        point = new Point<>(x / 2, y / 2);
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

    ;

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
