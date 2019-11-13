package com.huifer.planar.aset.utils.kriging;

import Jama.Matrix;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title : KriginCore </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-04-19
 */
public class KriginCore {


    //  块金常数
    private static final double C0 = 50;
    // 变程
    private static final double a = 10;
    // sill
    private static final double sill = 13.5;
    // 基台值
    private static final double jt = 80;

    public static void main(String[] args) {
        ArrayList<Kpoint> kpoints = gridData();
        System.out.println();
    }


    public static ArrayList<Kpoint> gridData() {
        // 插值结果
        ArrayList<Kpoint> dem = new ArrayList<>();

        ArrayList<Kpoint> demPoints = border();
        ArrayList<ArrayList<Kpoint>> sums = ReadFile.readFile();

        int count = 0;
        int xLength = sums.size();

        double[] Xs = new double[xLength];
        double[] Ys = new double[xLength];
        double[] Zs = new double[xLength];
        //网格点与离散点之间的距离
        double[] rData = new double[xLength + 1];
        //离散点Z的差值
        double[][] ZD = new double[xLength + 1][xLength + 1];
        //离散点与离散点的距离
        double[][] data = new double[xLength + 1][xLength + 1];
        List<Kpoint> points = null;

        for (int i = 0; i < xLength; i++) {
            points = sums.get(i);
            List<Double> X = points.stream().map(Kpoint::getX).collect(Collectors.toList());
            Xs[i] = X.get(0);
            List<Double> Y = points.stream().map(Kpoint::getY).collect(Collectors.toList());
            Ys[i] = Y.get(0);
            List<Double> Z = points.stream().map(Kpoint::getZ).collect(Collectors.toList());
            Zs[i] = Z.get(0);
        }
        bianchaFunction(xLength, Xs, Ys, Zs, ZD, data);

        Matrix matrix = new Matrix(data);
        Matrix M = matrix.inverse();

        while (count < demPoints.size()) {
            Kpoint p = demPoints.get(count++);
            double xp = p.getX();
            double yp = p.getY();
            double zp = 0;
            for (int k = 0; k <= xLength; k++) {
                if (k < xLength) {
                    double TempDis2 = 0;
                    TempDis2 = Math.sqrt((xp - Xs[k]) * (xp - Xs[k]) + (yp - Ys[k]) * (yp - Ys[k]));
                    rData[k] = qiutiFunction(TempDis2);
                } else {
                    rData[k] = 1;
                }
            }
            // 权重系数λi
            double[] rData2 = new double[xLength + 1];
            for (int k = 0; k < (xLength + 1); k++) {
                for (int k2 = 0; k2 < (xLength + 1); k2++) {
                    rData2[k] += M.get(k, k2) * rData[k2];
                }
            }

            // 得到估计值
            for (int k = 0; k < xLength; k++) {
                zp += Zs[k] * rData2[k];
            }

            DecimalFormat df = new DecimalFormat("###.000");
            // 保留小数点后三位
            double Zp = Double.parseDouble(df.format(zp));
            dem.add(new Kpoint(xp, yp, Zp));
        }

        return dem;
    }

    /**
     * 变差函数
     */
    private static void bianchaFunction(int xLength, double[] xs, double[] ys, double[] zs,
                                        double[][] ZD, double[][] data) {
        // 变差函数的系数矩阵
        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= xLength; j++) {
                if ((i < xLength) && (j < xLength)) {
                    if (i != j) {
                        double TempDis = Math.sqrt(((xs[i] - xs[j]) * (xs[i] - xs[j]))
                                + ((ys[i] - ys[j]) * (ys[i] - ys[j])));
                        ZD[i][j] = Math.abs(zs[i] - zs[j]);
                        data[i][j] = qiutiFunction(TempDis);
                    } else {
                        data[i][j] = 0;
                    }
                } else if ((i == xLength) || (j == xLength)) {
                    data[i][j] = 1;
                } else if ((i == xLength) && (j == xLength)) {
                    data[i][j] = 0;
                }
            }
        }
    }

    /**
     * 半方差函数:球状模型
     *
     * @param h 步长
     */
    private static double qiutiFunction(double h) {

        double r = 0;
        if (h == 0) {
            r = C0;
        } else if (h >= 0 && h <= a) {
            r = C0 + sill * ((3 * Math.pow(h, 4) / 4 * (Math.pow(a, 4))));
        } else {
            r = jt;
        }
        return r;
    }


    /**
     * 坐标范围 demPoints 是需要计算的东西
     */
    public static ArrayList<Kpoint> border() {
        ArrayList<ArrayList<Kpoint>> kPoints = ReadFile.readFile();

        double[] xlist = new double[kPoints.size()];
        double[] ylist = new double[kPoints.size()];

        for (int i = 0; i < kPoints.size(); i++) {
            ArrayList<Kpoint> kpoints = kPoints.get(i);
            List<Double> X = kpoints.stream().map(Kpoint::getX).collect(Collectors.toList());
            xlist[i] = X.get(0);

            List<Double> Y = kpoints.stream().map(Kpoint::getY).collect(Collectors.toList());
            ylist[i] = Y.get(0);

        }

        double maxx = xlist[0];
        double maxy = ylist[0];
        double minx = xlist[0];
        double miny = ylist[0];

        for (int i = 0; i < xlist.length; i++) {
            maxx = maxx > xlist[i] ? maxx : xlist[i];
            minx = minx < xlist[i] ? minx : xlist[i];
        }

        for (int i = 0; i < ylist.length; i++) {
            maxy = maxy > ylist[i] ? maxy : ylist[i];
            miny = miny < ylist[i] ? miny : ylist[i];
        }

        // 上述内容用来判断  计算点是否在范围内
        ArrayList<Kpoint> demPoints = new ArrayList<>();

        demPoints.add(new Kpoint(96776.032, 78973.761));
        demPoints.add(new Kpoint(96775.571, 79110.483));

        return demPoints;


    }

}
