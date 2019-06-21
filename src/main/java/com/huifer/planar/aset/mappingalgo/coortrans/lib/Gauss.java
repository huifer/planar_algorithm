package com.huifer.planar.aset.mappingalgo.coortrans.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : Gauss </p>
 * <p>Description : 高斯算法</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class Gauss {


    /**
     * 椭球
     */
    private Ellipsoid ell;

    /**
     * 中央子午线
     */
    private double l0;
    /**
     * y 方向偏移量
     */
    private double y0 = 500000.0;

    /***
     * 构造器
     * @param ellipsoid
     * @param midLon
     */
    public Gauss(Ellipsoid ellipsoid, double midLon) {
        this.ell = ellipsoid;
        l0 = midLon;
    }

    /***
     * 高斯正算
     * @param b 大地坐标B
     * @param l 大地坐标L
     * @return double[]
     */
    public double[] bl2Xy(double b, double l) {
        double dl = l - l0;
        double x = 0.0;
        double y = 0.0;
        List<Double> c = coefficient();
        double xxxx = 0;
        xxxx = c.get(0) * b + c.get(1) * Math.sin(2 * b) + c.get(2) * Math.sin(4 * b)
                + c.get(3) * Math
                .sin(6 * b)
                + c.get(4) * Math.sin(8 * b) + c.get(5) * Math.sin(10 * b);
        List<Double> a = coeffA(xxxx, b);
        x = a.get(0) + a.get(2) * dl * dl + a.get(4) * Math.pow(dl, 4) + a.get(6) * Math.pow(dl, 6);
        y = a.get(1) * dl + a.get(3) * Math.pow(dl, 3) + a.get(5) * Math.pow(dl, 5);
        y = y + y0;
        return new double[]{x, y};
    }


    /***
     * 高斯反算
     * @param x
     * @param y
     * @return
     */
    public double[] xy2BL(double x, double y) {
        double bbb = 0.0;
        double lll = 0.0;

        List<Double> c = coefficient();
        y = y - y0;
        double bf1 = x / c.get(0);
        double v = endPointLat(c, x, bf1);
        List<Double> b = coeffB(v);
        bbb = b.get(0) + b.get(2) * y * y + b.get(4) * Math.pow(y, 4) + b.get(6) * Math.pow(y, 6);
        double dl = b.get(1) * y + b.get(3) * Math.pow(y, 3) + b.get(5) * Math.pow(y, 5);
        lll = l0 + dl;
        return new double[]{bbb, lll};
    }

    /***
     * 计算底点纬度
     * @param coeff 系数
     * @param x x
     * @param bf1 底店唯独
     * @return
     */
    private double endPointLat(List<Double> coeff, double x, double bf1) {

        double xxx = x, dX = 0;
        double bf0 = 0;
        do {
            bf0 = bf1;
            dX = coeff.get(1) * Math.sin(2 * bf0) + coeff.get(2) * Math.sin(4 * bf0)
                    + coeff.get(3) * Math.sin(6 * bf0) + coeff.get(4) * Math.sin(8 * bf0)
                    + coeff.get(5) * Math
                    .sin(10 * bf0);
            bf1 = (xxx - dX) / coeff.get(0);
        } while (Math.abs(bf1 - bf0) > 0.0000000000001);
        return bf1;
    }


    /***
     * 计算系数a数组
     * @param xxx 弧度长
     * @param bbb 维度
     * @return double[] 系数
     */
    private List<Double> coeffA(double xxx, double bbb) {

        List<Double> aCoeff = new ArrayList<>();

        double a0, a1, a2, a3, a4, a5, a6;
        double n, t, eta;
        n = ell.n(bbb);
        t = ell.tan(bbb);
        eta = ell.eta(bbb);
        a0 = xxx;
        a1 = n * Math.cos(bbb);
        a2 = n * t * Math.pow(Math.cos(bbb), 2) / 2.0;
        a3 = n * (1 - t * t + eta * eta) * Math.pow(Math.cos(bbb), 3) / 6.0;
        a4 = n * t * (5 - t * t + 9 * eta * eta + 4 * Math.pow(eta, 4)) * Math.pow(Math.cos(bbb), 4)
                / 24.0;
        a5 = n * (5 - 18 * t * t + Math.pow(t, 4) + 14 * eta * eta - 58 * Math.pow(eta, 2) * Math
                .pow(t, 2)) * Math.pow(Math.cos(bbb), 5) / 120.0;
        a6 = n * t * (61 - 58 * t * t + Math.pow(t, 4) + 270 * eta * eta - 330 * Math.pow(eta, 2)
                * Math.pow(t, 2)) * Math.pow(Math.cos(bbb), 6) / 720.0;

        aCoeff.add(a0);
        aCoeff.add(a1);
        aCoeff.add(a2);
        aCoeff.add(a3);
        aCoeff.add(a4);
        aCoeff.add(a5);
        aCoeff.add(a6);
        return aCoeff;
    }


    /***
     * 计算系数b数组
     * @param bf 底点纬度
     * @return double[]
     */
    private List<Double> coeffB(double bf) {
        List<Double> bCoeff = new ArrayList<>();
        double nf = ell.n(bf);
        double mf = ell.m(bf);
        double tf = ell.tan(bf);
        double etaf = ell.eta(bf);
        double b0, b1, b2, b3, b4, b5, b6;
        b0 = bf;
        b1 = 1.0 / (nf * Math.cos(bf));
        b2 = -tf / (2 * mf * nf);
        b3 = -(1 + 2 * tf * tf + etaf * etaf) * b1 / 6.0 / nf / nf;
        b4 = -(5 + 3 * tf * tf + etaf * etaf - 9 * tf * tf * etaf * etaf) * b2 / 12.0 / nf / nf;
        b5 = -(5 + 28 * tf * tf + 24 * Math.pow(tf, 4) + 6 * etaf * etaf
                + 8 * tf * tf * etaf * etaf) * b1 / 120.0 / Math.pow(nf, 4);
        b6 = (61 + 90 * tf * tf + 45 * Math.pow(tf, 4)) * b2 / 360.0 / Math.pow(nf, 4);

        bCoeff.add(b0);
        bCoeff.add(b1);
        bCoeff.add(b2);
        bCoeff.add(b3);
        bCoeff.add(b4);
        bCoeff.add(b5);
        bCoeff.add(b6);

        return bCoeff;
    }


    /***
     * 求计算参数 子午弧长参数
     * @return
     */
    private List<Double> coefficient() {
        double m0 = ell.m0;
        double a, b, c, d, e, f;

        List<Double> coef = new ArrayList<>();

        double e2 = ell.eccSq;
        double e4 = e2 * e2;
        double e6 = e4 * e2;
        double e8 = e4 * e4;
        double e10 = e6 * e4;

        a = 1 + 3.0 / 4.0 * e2 + 45.0 / 64.0 * e4 + 175.0 / 256.0 * e6 +
                11025.0 / 16384.0 * e8 + 43659.0 / 65536.0 * e10;
        b = 3.0 / 4.0 * e2 + 15.0 / 16.0 * e4 + 525.0 / 512.0 * e6 +
                2205.0 / 2048.0 * e8 + 72765.0 / 65536.0 * e10;
        c = 15.0 / 64.0 * e4 + 105.0 / 256.0 * e6 + 2205.0 / 4096.0 * e8 +
                10395.0 / 16384.0 * e10;
        d = 35.0 / 512.0 * e6 + 315.0 / 2048.0 * e8 + 31185.0 / 131072.0 * e10;
        e = 315.0 / 16384.0 * e8 + 3465.0 / 65536.0 * e10;
        f = 693.0 / 131072.0 * e10;
        coef.add(a * m0);
        coef.add(-b * m0 / 2.0);
        coef.add(c * m0 / 4.0);
        coef.add(-d * m0 / 6.0);
        coef.add(e * m0 / 8.0);
        coef.add(-f * m0 / 10.0);

        return coef;
    }

}
