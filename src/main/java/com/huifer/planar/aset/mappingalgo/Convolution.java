package com.huifer.planar.aset.mappingalgo;

/**
 * <p>Title : Convolution </p>
 * <p>Description : 矩阵卷积</p>
 *
 * @author huifer
 * @date 2018/10/19
 */
public class Convolution {

    public double[][] m;
    public double[][] n;


    public Convolution(double[][] m, double[][] n) {
        this.m = m;
        this.n = n;
    }

    public static void main(String[] args) {
        double[][] m = new double[][]{
                {0.20, 0.30, 0.20},
                {0.25, 0.50, 0.35},
                {0.10, 0.30, 0.20}
        };

        double[][] n = new double[][]{
                {10.00, 13.50, 14.00, 13.80, 13.90, 15.60, 13.30, 14.50, 13.70, 14.40},
                {13.50, 13.30, 15.10, 16.40, 15.40, 14.90, 11.30, 13.50, 17.70, 13.30},
                {15.70, 14.00, 16.30, 18.60, 16.80, 16.60, 12.50, 15.50, 16.70, 14.80},
                {16.50, 15.90, 15.20, 17.40, 17.60, 17.70, 14.30, 14.50, 18.50, 15.60},
                {12.60, 13.30, 14.40, 16.50, 18.40, 18.40, 17.30, 16.50, 19.70, 17.40},
                {14.10, 17.70, 16.00, 15.40, 14.50, 19.60, 15.20, 18.50, 14.70, 18.30},
                {18.50, 14.50, 14.70, 13.10, 15.40, 14.30, 12.30, 17.50, 12.40, 13.20},
                {22.30, 15.20, 15.80, 18.00, 17.20, 13.50, 13.70, 16.50, 14.70, 15.30},
                {17.50, 16.30, 16.30, 13.60, 18.40, 15.70, 16.30, 15.50, 15.70, 16.40},
                {13.20, 17.30, 15.00, 12.80, 19.10, 16.60, 17.60, 16.50, 13.30, 17.30},
        };

        Convolution c = new Convolution(m, n);
        double[][] doubles = c.algo1();
        double[][] doubles1 = c.algo2();
        System.out.println();
    }


    public double[][] algo1() {

        int n = this.n[0].length;
        double[][] v = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                v[i][j] = v1(i, j);
            }
        }
        return v;
    }

    public double[][] algo2() {

        int n = this.n[0].length;
        double[][] v = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                v[i][j] = v2(i, j);
            }
        }
        return v;
    }


    private double v1(int iI, int jJ) {
        int m = 3;
        double v = 0;
        double upper = 0;
        double under = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                double mij = mIj(iI, jJ, i, j);
                double eps = 1e-10;
                if (Math.abs(mij) > eps) {
                    upper += mij * n[iI - i - 1][jJ - j - 1];
                    under += mij;
                }

            }
        }
        return upper / under;
    }


    private double v2(int iI, int jJ) {
        int m = 3;
        double v = 0;
        double upper = 0;
        double under = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                double mij = mIj(iI, jJ, i, j);
                double eps = 1e-10;
                if (Math.abs(mij) > eps) {
                    upper += mij * n[9 - (iI - i - 1)][9 - (jJ - j - 1)];
                    under += mij;
                }

            }
        }
        return upper / under;
    }

    private double mIj(int iI, int jJ, int i, int j) {
        int n = 10;
        double res = 0;
        if (iI - i - 1 < 0 || jJ - j - 1 < 0 || iI - i - 1 > n - 1 || jJ - j - 1 > 9) {

            res = 0;
        } else {
            res = m[i][j];
        }
        return res;
    }
}
