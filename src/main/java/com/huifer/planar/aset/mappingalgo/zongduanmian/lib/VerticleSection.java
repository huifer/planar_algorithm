package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : VerticleSection </p>
 * <p>Description : 纵断面</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class VerticleSection {

    /**
     * 关键点
     *
     * @see PointInfo
     */
    public List<PointInfo> kPoint;

    /**
     * 所有点
     *
     * @see PointInfo
     */
    public List<PointInfo> allPoint;
    /**
     * 方位角
     */
    public double direction1, direction2;
    /**
     * 面积
     */
    public double area;
    /**
     * 间隔
     */
    private double interval;
    /**
     * 距离d
     */
    private double dist1, dist2;
    /***
     * 三个点
     * @see PointInfo
     */
    private PointInfo p1, p2, p3;

    public VerticleSection() {
        this.interval = 10;
        allPoint = new ArrayList<>();
        kPoint = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "VerticleSection{" +
                "kPoint=" + kPoint +
                ", allPoint=" + allPoint +
                ", interval=" + interval +
                ", direction1=" + direction1 +
                ", direction2=" + direction2 +
                ", dist1=" + dist1 +
                ", dist2=" + dist2 +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                ", area=" + area +
                '}';
    }

    /**
     * 所有计算值
     *
     * @param cal
     * @param h0
     */
    public void calcVerticleSection(HeightCal cal, double h0) {
        //计算方向角
        calcDirection();
        //计算插值点里程
        calcK();
        //计算坐标
        calcPointXY();
        //计算高程
        calcHeight(cal);
        //计算面积
        calcArea(h0);
    }

    /**
     * 计算面积
     */
    private void calcArea(double h0) {
        double sum = 0;
        for (int i = 0; i < allPoint.size() - 1; i++) {
            PointInfo p1 = allPoint.get(i);
            PointInfo p2 = allPoint.get(i + 1);
            double area = (p1.hh + p2.hh - 2 * h0) * (p2.k - p1.k) / 2;
            sum += area;
        }
        area = sum;
    }

    /**
     * 计算高程
     */
    private void calcHeight(HeightCal cal) {
        for (int i = 0; i < allPoint.size(); i++) {
            PointInfo p = allPoint.get(i);
            if (p.equals(p1) || p.equals(p2) || p.equals(p3)) {
                continue;
            } else {
                p.hh = cal.getHeight(p);
            }
        }
    }

    /**
     * 计算坐标
     */
    private void calcPointXY() {
        for (int i = 1; i < allPoint.size() - 1; i++) {

            PointInfo p = allPoint.get(i);
            if (p.k < p2.k) {
                p.xx = p1.xx + Math.cos(direction1) * p.k;
                p.yy = p1.yy + Math.sin(direction1) * p.k;
                continue;
            }
            if (p.k > p2.k) {
                p.xx = p2.xx + Math.cos(direction2) * (p.k - p2.k);
                p.yy = p2.yy + Math.sin(direction2) * (p.k - p2.k);
                continue;
            }
        }
    }

    /**
     * 计算插值点里程
     */
    private void calcK() {
        double count = Math.floor((dist1 + dist2) / interval);
        allPoint.add(p1);
        boolean flag = false;
        for (int i = 1; i <= count; i++) {
            PointInfo p = new PointInfo();
            p.name = "V- " + i;
            p.k = i * interval + p1.k;
            if (p.k > p2.k && flag == false) {
                allPoint.add(p2);
                flag = true;
            }
            allPoint.add(p);
        }
        if (allPoint.get(allPoint.size() - 1).k != p3.k) {
            allPoint.add(p3);
        }
    }

    /**
     * 计算方向角
     */
    private void calcDirection() {
        double dx, dy;
        p1 = kPoint.get(0);
        p2 = kPoint.get(1);
        p3 = kPoint.get(2);

        dx = p2.xx - p1.xx;
        dy = p2.yy - p1.yy;
        // start calc direction1
        direction1 = startCalcDirection(dx, dy);
        // end calc direction1
        dist1 = Math.sqrt(dx * dx + dy * dy);

        // start calc direction2
        dx = p3.xx - p2.xx;
        dy = p3.yy - p2.yy;

        direction2 = startCalcDirection(dx, dy);

        dist2 = Math.sqrt(dx * dx + dy * dy);

        p1.k = 0;
        p2.k = dist1;
        p3.k = dist1 + dist2;
    }

    /**
     * 计算 direction
     *
     * @param dx
     * @param dy
     * @return
     */
    private double startCalcDirection(double dx, double dy) {
        double direction = 0;
        if (dx == 0) {
            if (dx < 0) {
                direction = 3 * Math.PI / 2;
            }
            if (dy > 0) {
                direction = Math.PI / 2;
            }
        } else {
            direction = Math.atan(Math.abs(dy / dx));
            if (dx > 0 && dy > 0) {
                direction = direction + 0;
            }
            if (dx < 0 && dy > 0) {
                direction = Math.PI - direction;
            }
            if (dx < 0 && dy < 0) {
                direction = direction + Math.PI;
            }
            if (dx > 0 && dy < 0) {
                direction = Math.PI * 2 - direction;
            }
        }
        return direction;
    }


}
