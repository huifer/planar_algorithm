package com.huifer.planar.aset.utils;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;

/**
 * <p>Title : Miact </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-03-05
 */
public class Miact {

    /**
     * 构造邻接矩阵
     *
     * @param points    点集合
     * @param polylines 线集合
     * @return 邻接矩阵
     */
    public static ArrayList<ArrayList<Double>> getDistances(
            ArrayList<Point> points, ArrayList<LineString> polylines) {
        int n = points.size();
        ArrayList<ArrayList<Double>> distances = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            distances.add(new ArrayList<Double>(n));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distances.get(i).add(100000000.0);
            }
        }
        for (int i = 0; i < polylines.size(); i++) {
            LineString polyline = polylines.get(i);
            Point point1 = polyline.getStartPoint();
            Point point2 = polyline.getEndPoint();
            int index1 = queryIndex(points, point1);
            int index2 = queryIndex(points, point2);
            distances.get(index1).set(index2, polyline.getLength());
            distances.get(index2).set(index1, polyline.getLength());
        }
        return distances;
    }


    /**
     * 查找索引
     *
     * @param points      点集合
     * @param targetPoint 查询点
     * @return 索引值
     */
    public static int queryIndex(ArrayList<Point> points, Point targetPoint) {
        int index = 0;
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.equals(targetPoint)) {
                index = i;
            }
        }
        return index;
    }


}
