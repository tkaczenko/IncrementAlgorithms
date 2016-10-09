package io.github.tkaczenko.incrementalgorithms.math.transformations;

/**
 * Created by tkaczenko on 08.10.16.
 */
public class Matrix {
    public double defaultValue = 0.0;
    public double[][] matrix;

    public Matrix(int rowsCount, int colsCount) {
        matrix = new double[rowsCount][colsCount];
    }

    public Matrix multiply(Matrix matrix) {
        if (matrix == null) {
            return null;
        }
        if (getColLength() != matrix.getRowLenght()) {
            return null;
        }
        Matrix result = new Matrix(getRowLenght(), getColLength());
        for (int i = 0; i < result.getRowLenght(); i++) {
            for (int j = 0; j < result.getColLength(); j++) {
                double cellValue = 0.0;
                for (int k = 0; k < getColLength(); k++) {
                    cellValue += get(i, k) * matrix.get(k, j);
                }
                result.set(i, j, cellValue);
            }
        }
        return result;
    }

    public boolean set(int i, int j, double value) {
        if (matrix == null) {
            return false;
        }
        if (i >= matrix.length) {
            return false;
        }
        if (j >= matrix[i].length) {
            return false;
        }
        matrix[i][j] = value;
        return true;
    }

    public double get(int i, int j) {
        return get(i, j, defaultValue);
    }

    public int getRowLenght() {
        if (matrix == null) {
            return 0;
        }
        return matrix.length;
    }

    public int getColLength() {
        if (matrix == null) {
            return 0;
        }
        if (getRowLenght() == 0) {
            return 0;
        }
        return matrix[0].length;
    }

    private double get(int i, int j, double defaultValue) {
        if (matrix == null) {
            return defaultValue;
        }
        if (i >= matrix.length) {
            return defaultValue;
        }
        if (j >= matrix[i].length) {
            return defaultValue;
        }
        return matrix[i][j];
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Matrix\n");
        for (double[] arr :
                matrix) {
            for (double val :
                    arr) {
                sb.append(val);
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
