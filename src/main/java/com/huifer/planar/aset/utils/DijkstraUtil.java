package com.huifer.planar.aset.utils;

import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;

import java.util.ArrayList;

/**
 * <p>Title : DijkstraUtil </p>
 * <p>Description : dijkstra 邻接矩阵构造器</p>
 *
 * @author huifer
 * @date 2018/12/14
 */
public class DijkstraUtil {


    /**
     * 构造邻接矩阵
     *
     * @param points    点集合
     * @param polylines 线集合
     * @return 邻接矩阵
     */
    public static ArrayList<ArrayList<Double>> getDistances(
            ArrayList<MyPoint> points, ArrayList<MyLine> polylines) {
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
            MyLine polyline = polylines.get(i);
            MyPoint point1 = polyline.getPoint1();
            MyPoint point2 = polyline.getPoint2();
            int index1 = queryIndex(points, point1);
            int index2 = queryIndex(points, point2);
            distances.get(index1).set(index2, polyline.getDistance());
            distances.get(index2).set(index1, polyline.getDistance());
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
    public static int queryIndex(ArrayList<MyPoint> points, MyPoint targetPoint) {
        int index = 0;
        for (int i = 0; i < points.size(); i++) {
            MyPoint point = points.get(i);
            if (point == targetPoint) {
                index = i;
            }
        }
        return index;
    }


}
