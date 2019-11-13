package com.huifer.planar.aset.algo.impl.concave;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : IsConcave </p>
 * <p>Description : 是否是凹多边形,以及凹点计算</p>
 *
 * @author huifer
 * @date 2019-02-26
 */
public class IsConcave {


    /**
     * 凹点获取
     *
     * @param pg polygon
     * @return 凹点列表
     */
    public static List<Point> troughPoint(Polygon pg) {
        Coordinate[] cs = pg.getCoordinates();
        int n = cs.length - 1;
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double zcrossproduct = getZcrossproduct(cs, n, i);
            if (zcrossproduct < 0) {
                points.add(new GeometryFactory().createPoint(cs[((i + 1) % n)]));
            }
        }

        return points;
    }


    /**
     * 是否是凹多边形
     *
     * @param polygon polygon
     * @return yes / no
     */
    public static boolean isConcave(Polygon polygon) {
        boolean sign = false;

        Coordinate[] cs = polygon.getCoordinates();
        int n = cs.length - 1;
        for (int i = 0; i < n; i++) {
            double zcrossproduct = getZcrossproduct(cs, n, i);
            if (i == 0) {
                sign = zcrossproduct > 0;
            } else if (sign != (zcrossproduct > 0)) {
                return false;
            }

        }
        return true;
    }

    private static double getZcrossproduct(Coordinate[] cs, int n, int i) {
        double dx1 = cs[(i + 2) % n].x - cs[(i + 1) % n].x;
        double dy1 = cs[(i + 2) % n].y - cs[(i + 1) % n].y;
        double dx2 = cs[i].x - cs[(i + 1) % n].x;
        double dy2 = cs[i].y - cs[(i + 1) % n].y;
        return dx1 * dy2 - dy1 * dx2;
    }


    /***
     * 是否是凹多边形
     * <p>i 是起点</p>
     * <p>((i + 1) % n) 是起中间点</p>
     * <p>((i + 2) % n) 是终点点</p>
     * 叉积结论对应中间点
     * @param points 点集合
     * @return 是/否
     */
    public static boolean isConcave(List<List<Double>> points) {

        boolean sign = false;
        int n = points.size();

        for (int i = 0; i < n; i++) {
            double dx1 = points.get((i + 2) % n).get(0) - points.get((i + 1) % n).get(0);
            double dy1 = points.get((i + 2) % n).get(1) - points.get((i + 1) % n).get(1);
            double dx2 = points.get(i).get(0) - points.get((i + 1) % n).get(0);
            double dy2 = points.get(i).get(1) - points.get((i + 1) % n).get(1);
            double zcrossproduct = dx1 * dy2 - dy1 * dx2;
//            System.out.println(i + "\t" + ((i + 1) % n) + "\t" + ((i + 2) % n) + "\t\t\t\t\t\t"
//                    + zcrossproduct);
            if (i == 0) {
                sign = zcrossproduct > 0;
            } else if (sign != (zcrossproduct > 0)) {
                return false;
            }
        }
        return true;
    }
}
