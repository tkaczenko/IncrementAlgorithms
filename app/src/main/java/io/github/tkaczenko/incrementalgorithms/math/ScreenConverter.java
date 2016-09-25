package io.github.tkaczenko.incrementalgorithms.math;

/**
 * Created by tkaczenko on 23.09.16.
 */

public class ScreenConverter {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    private int width;
    private int height;

    public int toScreenX(double x) {
        return (int) (width * (x - minX) / (maxX - minX));
    }

    public int toScreenY(double y) {
        return (int) (height * (1 - (y - minY) / (maxY - minY)));
    }

    public double toWorldX(int xs) {
        return 1.0 * xs / width * (maxX - minX) + minX;
    }

    public double toWorldY(int ys) {
        return (1.0 * ys - height) / (-height) * (maxY - minY) + minY;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
