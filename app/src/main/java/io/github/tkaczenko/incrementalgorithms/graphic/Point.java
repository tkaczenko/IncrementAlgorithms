package io.github.tkaczenko.incrementalgorithms.graphic;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class Point<T> {
    private T x;
    private T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public void setX(T x) {
        this.x = x;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point: " + x + " " + y;
    }
}
