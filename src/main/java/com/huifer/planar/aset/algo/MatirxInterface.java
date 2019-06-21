package com.huifer.planar.aset.algo;

import java.util.ArrayList;

/**
 * 矩阵操作
 *
 * @author huifer
 */
public interface MatirxInterface {

    /**
     * 求行列式
     *
     * @param matrix 矩阵
     * @return 行列式
     */
    double getRowColValue(ArrayList<ArrayList<Double>> matrix);

    /**
     * 求逆矩阵
     *
     * @param matrix 矩阵
     * @return 逆矩阵
     */
    ArrayList<ArrayList<Double>> getInverseMatrix(ArrayList<ArrayList<Double>> matrix);

    /**
     * 矩阵 乘数
     *
     * @param matrixA 矩阵A
     * @param matrixB 矩阵B
     * @return 矩阵乘积
     */
    ArrayList<ArrayList<Double>> getMultipMatrix(ArrayList<ArrayList<Double>> matrixA,
            ArrayList<ArrayList<Double>> matrixB);


    /**
     * 矩阵 加减
     *
     * @param matrixA 矩阵A
     * @param matrixB 矩阵B
     * @param opreation 操作值
     * @return 矩阵加减
     */
    ArrayList<ArrayList<Double>> getAddRemoveMatrix(ArrayList<ArrayList<Double>> matrixA,
            ArrayList<ArrayList<Double>> matrixB, double opreation);

    /**
     * 矩阵 倍法运算
     *
     * @param matrix 矩阵
     * @param k 倍法值
     * @return 倍法运算结果
     */
    ArrayList<ArrayList<Double>> getKMatrix(ArrayList<ArrayList<Double>> matrix, double k);


}
