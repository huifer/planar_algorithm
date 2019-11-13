package com.huifer.planar.aset.algo.impl.ovlayer;

import com.huifer.planar.aset.algo.GraphInterface;
import com.huifer.planar.aset.algo.MatirxInterface;
import com.huifer.planar.aset.algo.impl.MatirxCore;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.MyPolygon;
import com.huifer.planar.aset.entity.OvlayerEnum;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>Title : GraphCore </p>
 * <p>Description : 基本空间分析实现类</p>
 *
 * @author huifer
 * @date 2018/12/11
 */
public class GraphCore implements GraphInterface {

    private int minValue = -1;
    /**
     * 误差值
     */
    private int rightValue = 1;

    /**
     * 右手法则判断关系 1. 在线上 2. 在线段延长线上 3. 在线段右侧 4. 在线段左侧
     *
     * @param point    判断点
     * @param polyline 判断线段
     */
    @Override
    public final OvlayerEnum rightHandRule(MyPoint point, MyLine polyline) {

        if (point == null) {
            return null;
        }

        MatirxInterface matirx = new MatirxCore();
        ArrayList<ArrayList<Double>> arrays = new ArrayList<>();
        ArrayList<Double> first = new ArrayList<>(
                Arrays.asList(new Double[]{point.getX(), point.getY(), 1.0}));
        ArrayList<Double> second = new ArrayList<>(Arrays.asList(
                new Double[]{polyline.getPoint1().getX(), polyline.getPoint1().getY(), 1.0}));
        ArrayList<Double> third = new ArrayList<>(Arrays.asList(
                new Double[]{polyline.getPoint2().getX(), polyline.getPoint2().getY(), 1.0}));

        arrays.add(first);
        arrays.add(second);
        arrays.add(third);
        double value = matirx.getRowColValue(arrays);
        if (Math.abs(value) <= rightValue) {
            if (isInLineCenter(point, polyline)) {
                return OvlayerEnum.ON_THE_EXTENSION_LINE;
            } else {
                return OvlayerEnum.ON_LINE;
            }
        } else if (value > rightValue) {
            return OvlayerEnum.ON_THE_RIGHT_OF_THE_LINE;
        } else {
            return OvlayerEnum.ON_THE_LEFT_OF_THE_LINE;
        }

    }

    @Override
    public final MyPoint getIntersectionPoint(MyLine linea, MyLine lineb) {

        MyPoint px1 = linea.getPoint1();
        MyPoint py1 = linea.getPoint2();
        MyPoint px2 = lineb.getPoint1();
        MyPoint py2 = lineb.getPoint2();
        // 求解线性方程
        double denominator = (py1.getY() - px1.getY()) * (py2.getX() - px2.getX())
                - (px1.getX() - py1.getX()) * (px2.getY() - py2.getY());
        // 如果分母为0 则平行或共线, 不相交
        if (denominator == 0) {
            return null;
        }
        // 计算交点 xy
        double x = ((py1.getX() - px1.getX()) * (py2.getX() - px2.getX()) * (
                px2.getY() - px1.getY()) + (py1.getY() - px1.getY()) * (
                py2.getX() - px2.getX()) * px1.getX()
                - (py2.getY() - px2.getY()) * (py1.getX() - px1.getX()) * px2
                .getX()) / denominator;
        double y = -((py1.getY() - px1.getY()) * (py2.getY() - px2.getY()) * (
                px2.getX() - px1.getX()) + (py1.getX() - px1.getX()) * (
                py2.getY() - px2.getY()) * px1.getY()
                - (py2.getX() - px2.getX()) * (py1.getY() - px1.getY()) * px2
                .getY()) / denominator;
        // 判断交点是否在 两条线上
        if ((x - px1.getX()) * (x - py1.getX()) <= 0
                && (y - px1.getY()) * (y - py1.getY()) <= 0
                && (x - px2.getX()) * (x - py2.getX()) <= 0
                && (y - px2.getY()) * (y - py2.getY()) <= 0) {
            return new MyPoint(x, y);
        }
        // 不相交
        return null;
    }


    @Override
    public final OvlayerEnum pointWithPolygon(MyPoint point, MyPolygon polygon) {
        // 射线法判断 奇内外偶
        int n = polygon.getPolylines().size();
        // 奇偶计数器
        int count = 0;
        // 射线
        MyLine line = new MyLine();

        line.setPoint1(point);
        line.setPoint2(new MyPoint(this.minValue, point.getY()));

        for (int i = 0; i < n; i++) {
            // 获取多边形边
            MyLine side = polygon.getPolylines().get(i);
            // 点在线上 返回
            if (rightHandRule(point, side) == OvlayerEnum.ON_LINE) {
//                System.out.println("在多边形线上");
                return OvlayerEnum.ON_THE_POLYGON_LINE;
            }
            if (rightHandRule(side.getPoint1(), line) == OvlayerEnum.ON_LINE) {
                if (side.getPoint1().getY() > side.getPoint2().getY()) {
                    count++;
                }
            } else if (rightHandRule(side.getPoint2(), line) == OvlayerEnum.ON_LINE) {
                if (side.getPoint2().getY() > side.getPoint1().getY()) {
                    count++;
                }
            } else if (lineWithLine(line, side) == 1) {
                count++;
            }
        }
        // 结果
        if (count % 2 == 1) {
//            System.out.println("在多边形内");
            return OvlayerEnum.INSIDE_THE_POLYGON;
        } else {
//            System.out.println("在多边形外");
            return OvlayerEnum.OUTSIDE_THE_POLYGON;
        }

    }


    @Override
    public final int lineWithLine(MyLine linea, MyLine lineb) {
        if ((Math.max(linea.getPoint1().getX(), linea.getPoint2().getX()) >= Math
                .min(lineb.getPoint1().getX(), lineb.getPoint2().getX()))
                && (Math.max(lineb.getPoint1().getX(), lineb.getPoint2().getX()) >= Math
                .min(linea.getPoint1().getX(), linea.getPoint2().getX()))
                && ((Math.max(linea.getPoint1().getY(), linea.getPoint2().getY()) >= Math
                .min(lineb.getPoint1().getY(), lineb.getPoint2().getY()))
                && (Math.max(lineb.getPoint1().getY(), lineb.getPoint2().getY()) >= Math
                .min(linea.getPoint1().getY(), linea.getPoint2().getY()))
                && (multiply(lineb.getPoint1(), linea.getPoint2(), linea.getPoint1()) * multiply(
                linea.getPoint2(), lineb.getPoint2(), linea.getPoint1()) >= 0)
                && (multiply(linea.getPoint1(), lineb.getPoint2(), lineb.getPoint1()) * multiply(
                lineb.getPoint2(), linea.getPoint2(), lineb.getPoint1()) >= 0))) {
            // 相交
            return 1;
        } else {
            // 不相交
            return 0;
        }
    }

    /**
     * 计算叉积
     */
    private double multiply(MyPoint a, MyPoint b, MyPoint c) {

        return ((a.getX() - c.getX()) * (b.getY() - c.getY()) - (b.getX() - c.getX()) * (
                a.getY() - c.getY()));

    }

    /**
     * 点是否处于线中间 (x 坐标)
     */
    private boolean isInLineCenter(MyPoint point, MyLine polyline) {
        double x1 = polyline.getPoint1().getX();
        double x2 = polyline.getPoint2().getX();
        if (x1 > x2) {
            double tem = x2;
            x2 = x1;
            x1 = tem;
        }
        if (point.getY() >= x1 && point.getX() <= x2) {
            return true;
        } else {
            return false;
        }
    }

}
