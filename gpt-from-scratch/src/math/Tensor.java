package src.math;

import java.util.Arrays;

public class Tensor {
    private final double[] data;
    private final int[] shape;
    private final int[] strides;


    public Tensor(int... shape) {
        this.shape = shape.clone();
        this.strides = calculateStrides(shape);

        int size = calculateSize(shape);
        this.data = new double[size];
    }
    private Tensor(final double[] data, final int... shape) {
        this.shape = shape.clone();
        this.strides = calculateStrides(shape);

        if(data.length != calculateSize(shape)) throw new IllegalArgumentException("Data size does not match shape");

        this.data = data.clone();
    }


    public static Tensor zeros(final int... shape) {
        return new Tensor(shape);
    }

    public static Tensor ones(final int... shape) {
        Tensor tensor = new Tensor(shape);
        Arrays.fill(tensor.data, 1.0);
        return tensor;
    }

    public static Tensor fromArray(final double[] values) {
        return new Tensor(values, values.length);
    }

    public static Tensor fromArray(final double[][] values) {
        final int rows = values.length;
        final int cols = values[0].length;

        final Tensor tensor = new Tensor(rows, cols);

        for(int i = 0; i < rows; i++) {
            if(values[i].length != cols) {
                throw new IllegalArgumentException("Jagged array");
            }

            for(int j = 0; j < cols; j++) {
                tensor.set(values[i][j], i, j);
            }
        }

        return tensor;
    }

    public int[] shape() {
        return shape.clone();
    }

    public int rank() {
        return shape.length;
    }

    public int size() {
        return data.length;
    }

    public int dimension(int axis) {
        return shape[axis];
    }

    public double get(final int... indices) {
        return data[calculateIndex(indices)];
    }

    public void set(final double value, final int... indices) {
        data[calculateIndex(indices)] = value;
    }

    public double getFlat(int index) {
        return data[index];
    }

    public void setFlat(int index, double value) {
        data[index] = value;
    }

    public Tensor add(final Tensor other) {
        checkSameShape(other);

        final Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) {
            result.data[i] = data[i] + other.data[i];
        }

        return result;
    }

    public Tensor subtract(final Tensor other) {
        checkSameShape(other);

        final Tensor result = new Tensor(shape);

        for (int i = 0; i < data.length; i++) {
            result.data[i] = data[i] - other.data[i];
        }

        return result;
    }

    public Tensor multiply(final Tensor other) { // element wise
        checkSameShape(other);

        final Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) {
            result.data[i] = data[i] * other.data[i];
        }

        return result;
    }

    public Tensor divide(final Tensor other) {
        checkSameShape(other);

        final Tensor result = new Tensor(shape);

        for (int i = 0; i < data.length; i++) {
            result.data[i] = data[i] / other.data[i];
        }

        return result;
    }

    public Tensor multiply(final double scalar) { // scalar mult (overlaod multipy)
        final Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) {
            result.data[i] = data[i] * scalar;
        }

        return result;
    }

    public Tensor matmul(final Tensor other) {
        if(rank() != 2 || other.rank() != 2) throw new IllegalArgumentException("matmul currently only supports 2D tensors");

        int rows = shape[0];
        int cols = shape[1];
        int otherRows = other.shape[0];
        int otherCols = other.shape[1];

        if(cols != otherRows) {
            throw new IllegalArgumentException(
                    "Incompatible shapes: "
                    + Arrays.toString(shape)
                    + " and "
                    + Arrays.toString(other.shape)
            );
        }

        final Tensor result = new Tensor(rows, otherCols);

        for(int i =0; i < rows; i++) {
            for (int j = 0; j < otherCols; j++) {
                double sum = 0.0;

                for(int k = 0; k < cols; k++) sum += get(i, k) * other.get(k, j);

                result.set(sum, i, j);
            }
        }

        return result;
    }

    public Tensor transpose() {
        if(rank() != 2) throw new IllegalArgumentException("Transpose currently only supports 2D tensors");

        final int rows = shape[0];
        final int cols = shape[1];

        final Tensor result = new Tensor(cols, rows);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                result.set(get(i, j), j, i);
            }
        }

        return result;
    }

    public Tensor reshape(final int... newShape) {
        if(calculateSize(newShape) != size()) {
            throw new IllegalArgumentException(
                    "Cannot reshape "
                    + Arrays.toString(shape)
                    + " to "
                    + Arrays.toString(newShape)
            );
        }

        return new Tensor(data, newShape);
    }

    public Tensor exp() {
        Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) {
            result.data[i] = Math.exp(data[i]);
        }

        return result;
    }

    public Tensor log() {
        final Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) result.data[i] = Math.log(data[i]);

        return result;
    }

    public Tensor relu() {
        final Tensor result = new Tensor(shape);

        for(int i = 0; i < data.length; i++) result.data[i] = Math.max(0.0, data[i]);

        return result;
    }

    public Tensor softmax() {
        if(rank() != 1) {
            throw new IllegalArgumentException(
                    "Softmax currently only supports vectors"
            );
        }

        final Tensor result = new Tensor(shape);

        double max = data[0];

        for(double value : data) {
            max = Math.max(max, value);
        }

        double sum = 0.0;

        for(int i = 0; i < data.length; i++) {
            result.data[i] = Math.exp(data[i] - max);
            sum += result.data[i];
        }

        for(int i = 0; i < data.length; i++) {
            result.data[i] /= sum;
        }

        return result;
    }

    public double sum() {
        double sum = 0.0;

        for(double value : data) {
            sum += value;
        }

        return sum;
    }

    public double mean() {
        return sum() / size();
    }

    
    private void checkSameShape(final Tensor other) {
        if(!Arrays.equals(shape, other.shape)) {
            throw new IllegalArgumentException(
                    "Shape mismatch: "
                    + Arrays.toString(shape)
                    + " vs "
                    + Arrays.toString(other.shape)
            );
        }
    }

    private int calculateIndex(final int... indices) {
        if(indices.length != rank()) {
            throw new IllegalArgumentException(
                    "Expected " + rank() + " indices, got " + indices.length
            );
        }

        int index = 0;
        for(int i = 0; i < indices.length; i++) {
            if(indices[i] < 0 || indices[i] >= shape[i]) {
                throw new IndexOutOfBoundsException(
                        "Index " + indices[i] + " out of bounds for dimension " + i
                );
            }

            index += indices[i] * strides[i];
        }

        return index;
    }

    private static int calculateSize(final int... shape) {
        int size = 1;

        for(int dim : shape) {
            if (dim <= 0) {
                throw new IllegalArgumentException(
                        "Dimensions must be positive"
                );
            }

            size = Math.multiplyExact(size, dim);
        }

        return size;
    }

    private static int[] calculateStrides(int... shape) {
        int[] strides = new int[shape.length];

        int stride = 1;

        for(int i = shape.length - 1; i >= 0; i--) {
            strides[i] = stride;
            stride *= shape[i];
        }

        return strides;
    }

    @Override public String toString() {
        return   "Tensor{"
                + "shape=" + Arrays.toString(shape)
                + ", data=" + Arrays.toString(data)
                + '}';
    }
}