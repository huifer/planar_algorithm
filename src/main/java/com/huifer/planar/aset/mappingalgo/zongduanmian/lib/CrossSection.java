package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : CrossSection </p>
 * <p>Description : 横断面</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class CrossSection {

    /**
     * 起点
     */
    private PointInfo startPoint;
    /***
     * 终点
     */
    private PointInfo endPoint;

    /**
     * 关键点
     */
    public PointInfo kPoint;
    /***
     * 当前所有点
     */
    public List<PointInfo> crossPoint;

    /**
     * 方向角
     */
    public double direction;
    /**
     * 间隔
     */
    private double interval;
    /**
     * 宽度
     */
    private double width;
    /**
     * 面积
     */
    public double area;

    public CrossSection(PointInfo startPoint, PointInfo endPoint, double direction) {
        // 题目中的值
        interval = 5;
        crossPoint = new ArrayList<>();
        this.direction = direction;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        width = 25;
    }


    /**
     * 所有计算汇总
     * @param cal
     * @param h0
     */
    public void calcDataForCrossSection(HeightCal cal, double h0) {
        //计算中点
        calcKeyPoint();
        //计算插值点里程
        calcK();
        //计算方位角
        calcDirection();
        //计算插值点坐标
        calcPointXY();
        //计算插值高程
        calcHeight(cal);
        //计算面积
        calcArea(h0);

    }

    @Override
    public String toString() {
        return "CrossSection{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", kPoint=" + kPoint +
                ", crossPoint=" + crossPoint +
                ", direction=" + direction +
                ", interval=" + interval +
                ", width=" + width +
                ", area=" + area +
                '}';
    }

    /***
     * 计算面积
     * @param h0
     */
    private void calcArea(double h0) {
        double sum = 0;
        for (int i = 0; i < crossPoint.size() - 1; i++) {
            PointInfo p1 = crossPoint.get(i);
            PointInfo p2 = crossPoint.get(i + 1);
            double area = (p1.hh + p2.hh - 2 * h0) * interval / 2;
            sum += area;
        }
        area = sum;
    }

    /***
     * 计算高度
     * @param cal
     */
    private void calcHeight(HeightCal cal) {
        for (int i = 0; i < crossPoint.size(); i++) {
            PointInfo pointInfo = crossPoint.get(i);
            pointInfo.hh = cal.getHeight(pointInfo);
        }
    }

    /**
     * 计算插值点坐标
     */
    private void calcPointXY() {
        for (int i = 0; i < crossPoint.size(); i++) {
            PointInfo pointInfo = crossPoint.get(i);
            // k 是否需要相等
            if (pointInfo.xx == kPoint.xx && pointInfo.yy == kPoint.yy) {
                continue;
            }
            pointInfo.xx = (pointInfo.k - width) * Math.cos(direction) + kPoint.xx;
            pointInfo.yy = (pointInfo.k - width) * Math.sin(direction) + kPoint.yy;
        }
    }

    /***
     * 计算方位角
     *  表15-2 算法实现
     */
    private void calcDirection() {
        direction = direction - Math.PI / 2;
        if (direction < 0) {
            direction = Math.PI * 2 + direction;
        }
        if (direction > Math.PI * 2) {
            direction = Math.PI * 2 - direction;
        }
    }

    /***
     * 计算k
     */
    private void calcK() {
        int count = (int) Math.ceil(width / interval);
//        int count = 5;
        for (int i = -count; i < 0; i++) {
            PointInfo p = new PointInfo();
            p.k = i * interval + width;
            p.name = "C" + i;
            crossPoint.add(p);
        }
        PointInfo tem = new PointInfo();
        kPoint.k = width;
        crossPoint.add(kPoint);
        for (int i = 1; i <= count; i++) {
            PointInfo p = new PointInfo();
            p.k = i * interval + width;
            p.name = "C+" + i;
            crossPoint.add(p);
        }
    }

    /***
     * 计算中点
     */
    private void calcKeyPoint() {
        double dx;
        double dy;
        double dist;
        dx = endPoint.xx - startPoint.xx;
        dy = endPoint.yy - startPoint.yy;
        dist = Math.sqrt(dx * dx + dy * dy);

        kPoint = new PointInfo();
        kPoint.xx = Math.cos(direction) * (dist / 2) + startPoint.xx;
        kPoint.yy = Math.sin(direction) * (dist / 2) + startPoint.yy;
        kPoint.k = startPoint.k + dist / 2;
        kPoint.name = "中心点";
    }


}
