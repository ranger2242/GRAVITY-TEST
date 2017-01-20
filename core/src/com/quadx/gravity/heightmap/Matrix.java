package com.quadx.gravity.heightmap;

/**
 * Created by Chris Cavazos on 1/19/2017.t
 */
public class Matrix<T> {
    private final Class<? extends T> cls;

    public Matrix(Class<? extends T> cls) {
        this.cls = cls;

    }

    public int[][] rotateMatrix(int[][] t, int res, boolean left) {
        int[][] n = new int[res][res];
        int[][] n2 = new int[res][res];

        if (!left) {
            for (int i = 0; i < res; ++i) {
                for (int j = 0; j < res; ++j) {
                    n[i][j] = t[res - j - 1][i];

                }
            }
        } else {
                for (int i = 0; i < res; ++i) {
                    for (int j = 0; j < res; ++j) {
                            n[i][j] = t[j][res - i - 1];}
                    }
                }
        return n;
    }
}

