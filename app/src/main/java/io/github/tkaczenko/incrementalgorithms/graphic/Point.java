package io.github.tkaczenko.incrementalgorithms.graphic;

/**
 * Created by tkaczenko on 23.09.16.
 */
public class Point<T> {
    private final T x;
    private final T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}
