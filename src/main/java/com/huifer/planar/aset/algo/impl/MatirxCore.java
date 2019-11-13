package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.MatirxInterface;
import com.huifer.planar.aset.entity.RefObject;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * <p>Title : MatirxCore </p>
 * <p>Description : 矩阵运算</p>
 *
 * @author huifer
 * @date 2018/12/11
 */
@Slf4j
public class MatirxCore implements MatirxInterface {

    /**
     * 判断是否是矩阵
     *
     * @param matrix 矩阵
     * @return boolean
     */
    private boolean isMatrix(ArrayList<ArrayList<Double>> matrix) {
        boolean flag = true;
        int n = matrix.get(0).size();
        for (int i = 1; i < n; i++) {
            if (matrix.get(i).size() != n) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    /***
     * 填充一个 全是0的矩阵
     * @param matrix 矩阵
     * @param row 行数
     * @param col 列数
     */
    private void initMatix(RefObject<ArrayList<ArrayList<Double>>> matrix, int row, int col) {
        matrix.argvalue = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < row; i++) {
            ArrayList<Double> elem = new ArrayList<>();
            for (int j = 0; j < col; j++) {
                elem.add(0.0);
            }
            matrix.argvalue.add(elem);
        }
    }


    @Override
    public double getRowColValue(ArrayList<ArrayList<Double>> matrix) {
        if (!isMatrix(matrix)) {
            throw new RuntimeException("参数异常");
        }
        double result = 0;
        int n = matrix.size();
        // 1*1 矩阵
        if (n == 1) {
            return matrix.get(0).get(0);
        }

        ArrayList<ArrayList<Double>> tem = null;
        RefObject<ArrayList<ArrayList<Double>>> temRef = new RefObject<>(tem);
        initMatix(temRef, n - 1, n - 1);
        tem = temRef.argvalue;
        // 行列式运算流程
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                for (int k = 0; k < n - 1; k++) {
                    int flag;
                    if (j < i) {
                        flag = 0;
                    } else {
                        flag = 1;
                    }
                    tem.get(j).set(k, matrix.get(j + flag).get(k + 1));
                }
            }
            int flag2 = -1;
            if (i % 2 == 0) {
                flag2 = 1;
            }
            result += flag2 * matrix.get(i).get(0) * getRowColValue(tem);
        }
        return result;
    }

    @Override
    public ArrayList<ArrayList<Double>> getInverseMatrix(ArrayList<ArrayList<Double>> matrix) {
        if (!isMatrix(matrix)) {
            throw new RuntimeException("参数异常");
        }
        int n = matrix.size();
        ArrayList<ArrayList<Double>> result = null;
        RefObject<ArrayList<ArrayList<Double>>> resultTem = new RefObject<>(result);
        initMatix(resultTem, n, n);
        result = resultTem.argvalue;
        double resultSum = getRowColValue(matrix);
        ArrayList<ArrayList<Double>> tem = null;
        RefObject<ArrayList<ArrayList<Double>>> tempRefTemp = new RefObject<>(tem);
        initMatix(tempRefTemp, n - 1, n - 1);
        tem = tempRefTemp.argvalue;
        // 求逆运算流程
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n - 1; k++) {
                    for (int m = 0; m < n - 1; m++) {
                        int flag1 = 0;
                        int flag2 = 0;
                        if (k < i) {
                            flag1 = 0;
                        } else {
                            flag1 = 1;
                        }
                        if (m < j) {
                            flag2 = 0;
                        } else {
                            flag2 = 1;
                        }
                        tem.get(k).set(m, matrix.get(k + flag1).get(m + flag2));
                    }
                }
                int flag3 = -1;
                if ((i + j) % 2 == 0) {
                    flag3 = 1;
                }
                result.get(j).set(i, (float) flag3 * getRowColValue(tem) / resultSum);
            }
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<Double>> getMultipMatrix(ArrayList<ArrayList<Double>> matrixA,
                                                        ArrayList<ArrayList<Double>> matrixB) {

        if (!isMatrix(matrixA)) {
            log.error("{}", new RuntimeException("参数异常"));
            throw new RuntimeException("参数异常");
        }
        int row1 = matrixA.size();
        int col1 = matrixA.get(0).size();
        int row2 = matrixB.size();
        int col2 = matrixB.get(0).size();
        if (col1 != row2) {
            log.error("{}", new RuntimeException("输入矩阵行和列不相等"));
            throw new RuntimeException("输入矩阵行和列不相等");
        }
        ArrayList<ArrayList<Double>> result = null;
        RefObject<ArrayList<ArrayList<Double>>> resultTem = new RefObject<>(result);
        initMatix(resultTem, row1, col2);
        result = resultTem.argvalue;
        // 乘数运算
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                double tem = 0;
                for (int k = 0; k < col1; k++) {
                    tem += matrixA.get(i).get(k) * matrixB.get(k).get(j);
                }
                result.get(i).set(j, tem);
            }
        }
        return result;
    }

    @Override
    public ArrayList<ArrayList<Double>> getAddRemoveMatrix(ArrayList<ArrayList<Double>> matrixA,
                                                           ArrayList<ArrayList<Double>> matrixB, double opreation) {

        if (!isMatrix(matrixA)) {
            log.error("{}", new RuntimeException("参数异常"));
            throw new RuntimeException("参数异常");
        }
        int row1 = matrixA.size();
        int col1 = matrixA.get(0).size();
        int row2 = matrixB.size();
        int col2 = matrixB.get(0).size();
        if (col1 != row2) {
            log.error("{}", new RuntimeException("输入矩阵行和列不相等"));
            throw new RuntimeException("输入矩阵行和列不相等");
        }

        ArrayList<ArrayList<Double>> result = null;
        RefObject<ArrayList<ArrayList<Double>>> resultTem = new RefObject<>(result);
        initMatix(resultTem, row1, col1);
        result = resultTem.argvalue;
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                result.get(i).set(j, matrixA.get(i).get(j) + opreation * matrixA.get(i).get(j));
            }
        }

        return result;
    }

    @Override
    public ArrayList<ArrayList<Double>> getKMatrix(ArrayList<ArrayList<Double>> matrix,
                                                   double k) {

        if (!isMatrix(matrix)) {
            log.error("{}", new RuntimeException("参数异常"));
            throw new RuntimeException("参数异常");
        }

        int row1 = matrix.size();
        int col1 = matrix.get(0).size();
        ArrayList<ArrayList<Double>> result = null;
        RefObject<ArrayList<ArrayList<Double>>> resultTem = new RefObject<>(result);
        initMatix(resultTem, row1, col1);
        result = resultTem.argvalue;
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                result.get(i).set(j, k * (matrix.get(i).get(j)));
            }
        }

        return result;
    }
}
