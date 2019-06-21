package com.huifer.planar.aset.mappingalgo.grid.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Title : Convex </p>
 * <p>Description : 凸包算法</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class Convex {

    /**
     * 顶点列表
     */
    public List<PointInfo> vertexs;

    /**
     * 所有点
     */
    private List<PointInfo> points;

    /**
     * 排序点
     */
    private List<PointInfo> sortPoints;


    /**
     * 操作堆
     */
    private Stack stack;

    /**
     * 最小点
     */
    private PointInfo minPoint;

    public Convex(List<PointInfo> pointInfos) {
        this.points = pointInfos;
        // 最小点
        findMinPoint();
        // 角度排序
        sortPointWithAngle();
        // 凸包构造
        createConvex();
    }

    private void createConvex() {
        stack = new Stack();

        stack.pushStack(minPoint);
        stack.pushStack(sortPoints.get(0));
        stack.pushStack(sortPoints.get(1));

        for (int i = 2; i < sortPoints.size(); i++) {
            // 左转 弹
            while (isLeft(stack.data.get(stack.top - 1), stack.data.get(stack.top),
                    sortPoints.get(i))) {
                stack.popStack();
            }
            stack.pushStack(sortPoints.get(i));
        }
        vertexs = stack.data;

    }

    /**
     * 根据角度（极角）排序
     */
    private void sortPointWithAngle() {

        sortPoints = new ArrayList<PointInfo>();
        PointInfo newMinPoint = new PointInfo();
        newMinPoint.x = minPoint.x;
        newMinPoint.y = minPoint.y - 1;

        // 角度计算
        for (int i = 0; i < points.size(); i++) {
            PointInfo p = points.get(i);
//            if (p.equals(minPoint)) {
//                continue;
//            }
            p.angle = calcAngle(newMinPoint, minPoint, p);
            sortPoints.add(p);
        }
        sortPoints.remove(minPoint);

        Collections.sort(sortPoints, Comparator.comparingDouble(s -> s.angle));
        sortPoints.add(minPoint);

    }


    /**
     * 点集中找出最小点
     */
    private void findMinPoint() {
        minPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y < minPoint.y) {
                minPoint = points.get(i);
            } else if (points.get(i).y == minPoint.y) {
                if (points.get(i).x < minPoint.x) {
                    minPoint = points.get(i);
                }
            }
        }
    }


    /**
     * 是否左旋转
     */
    private boolean isLeft(PointInfo start, PointInfo mid, PointInfo end) {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        v1.x = start.x - mid.x;
        v1.y = start.y - mid.y;
        v2.x = end.x - mid.x;
        v2.y = end.y - mid.y;
        if (GeoCalc.crossMul(v1, v2) > 0) {
            return true;
        }
        if ((GeoCalc.crossMul(v1, v2) == 0) && GeoCalc.model(v1) < GeoCalc.model(v2)) {
            return true;
        }
        return false;
    }


    /***
     * 计算极角
     * @param start
     * @param mid
     * @param end
     * @return
     */
    private double calcAngle(PointInfo start, PointInfo mid, PointInfo end) {
        double dx = mid.x - end.x;
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        v1.x = start.x - mid.x;
        v1.y = start.y - mid.y;
        v2.x = end.x - mid.x;
        v2.y = end.y - mid.y;

        double anglee = GeoCalc.dotMul(v1, v2);
        double dist1 = GeoCalc.model(v1);
        double dist2 = GeoCalc.model(v2);
        anglee = Math.acos(anglee / (dist1 * dist2));
        if (dx > 0) {
            return 2 * Math.PI - anglee;
        }
        return anglee;
    }

    @Override
    public String toString() {
        return "Convex{" +
                "vertexs=" + vertexs +
                ", points=" + points +
                ", sortPoints=" + sortPoints +
                ", stack=" + stack +
                ", minPoint=" + minPoint +
                '}';
    }
}
