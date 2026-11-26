package tests;

import src.math.*;

public class TensorTest {
    public static void main(String[] args) {
        testMatmul();
        testSoftmax();
        testTranspose();

        System.out.println("All tests passed!");
    }

    static void testMatmul() {
        Tensor a = Tensor.fromArray(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        Tensor b = Tensor.fromArray(new double[][]{
                {7, 8},
                {9, 10},
                {11, 12}
        });

        Tensor result = a.matmul(b);

        assert result.get(0, 0) == 58;
        assert result.get(0, 1) == 64;
        assert result.get(1, 0) == 139;
        assert result.get(1, 1) == 154;
    }

    static void testSoftmax() {
        Tensor t = Tensor.fromArray(new double[]{2, 1, 0});
        Tensor result = t.softmax();

        assert Math.abs(result.sum() - 1.0) < 1e-10;
        assert result.get(0) > result.get(1);
        assert result.get(1) > result.get(2);
    }

    static void testTranspose() {
        Tensor t = Tensor.fromArray(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        Tensor result = t.transpose();

        assert result.get(0, 1) == 4;
        assert result.get(2, 0) == 3;
    }
}