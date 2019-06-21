package com.huifer.planar.aset.mappingalgo.coortrans.lib;

/**
 * <p>Title : Ellipsoid </p>
 * <p>Description : 椭球</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class Ellipsoid {


    /**
     * 长半轴
     */
    public double a;

    /***
     * 扁率
     */
    public double f;

    /**
     * 第一偏心率的平方
     */
    public double eccSq;

    /**
     * 第二偏心率的平方
     */
    public double ecc2Sq;

    /**
     * m0
     */
    public double m0;


    public Ellipsoid() {
        a = 6378137.0;
        f = 0.335281066475e-2;
    }

    /***
     * 椭球构造
     * @param a 长半轴
     * @param invf f
     */
    public Ellipsoid(double a, double invf) {
        this.a = a;
        f = 1 / invf;
        init();
    }

    void init() {
        double b = a * (1 - f);
        eccSq = (a * a - b * b) / a / a;
        ecc2Sq = (a * a - b * b) / b / b;
        m0 = a * (1 - eccSq);
    }

    /***
     * 计算W
     * @param b 纬度（以弧度为单位）
     * @return
     */
    public double w(double b) {
        double w = Math.sqrt(1 - eccSq * Math.pow(Math.sin(b), 2));
        return w;
    }

    /***
     * 计算eta
     * @param b 纬度（以弧度为单位）
     * @return
     */
    public double eta(double b) {
        double eta = Math.sqrt((ecc2Sq * Math.pow(Math.cos(b), 2)));
        return eta;
    }


    /***
     * 计算tan
     * @param b 纬度（以弧度为单位）
     * @return
     */
    public double tan(double b) {
        return Math.tan(b);
    }

    /***
     * 计算 卯酉圈的曲率半径N
     * @param b 纬度（以弧度为单位）
     * @return
     */
    public double n(double b) {
        double w = w(b);
        return a / w;
    }

    /***
     * 子午圈曲率半径 m
     * @param b
     * @return
     */
    public double m(double b) {
        double over = a * (1 - eccSq);
        double w = w(b);
        return over / (w * w * w);
    }

}
