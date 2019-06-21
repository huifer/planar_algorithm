package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.VoronoiInterface;
import com.huifer.planar.aset.algo.impl.polygon.VoronoiInterfaceImpl;
import com.huifer.planar.aset.entity.Kmeans;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : KmeanPolygon </p>
 * <p>Description : KmeanPolygon</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public class KmeanPolygon {


    @Test
    public  void main() throws ParseException {
        // 打算随机多少个点
        int setp = 20;
        // k = 簇数
        int k = 5;
        String polygonWkt = "POLYGON((411 80, 569 125, 675 238, 795 321, 917 416, 866 597, 600 699, 443 614, 500 483, 399 338, 411 80))";
        HashMap hashMap = polygonKmeanData(polygonWkt, setp, k);

    }


    public static HashMap polygonKmeanData(String polygonWkt, int setp, int k)
            throws ParseException {

        Operation op = new Operation();

        // 构造一个面
        Polygon polygon = op.createPolygonByWKT(polygonWkt);
        Coordinate[] coordinates = polygon.getCoordinates();
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();

        Arrays.stream(coordinates).forEach(
                s -> {
                    xList.add(s.x);
                    yList.add(s.y);
                }
        );
        // xy 最大最小值
        Double xMax = xList.stream().reduce(Double::max).get();
        Double xMin = xList.stream().reduce(Double::min).get();
        Double yMax = yList.stream().reduce(Double::max).get();
        Double yMin = yList.stream().reduce(Double::min).get();

        // 当前点数量
        int pointCount = 0;
        ArrayList<Point> pointArrayList = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 最大最小值随机
            if (pointCount <= setp) {
                double rx = random(xMax, xMin);
                double ry = random(yMax, yMin);
                Point nowPoint = op.createPointByWkt("POINT(" + rx + " " + ry + ")");
                boolean contains = polygon.contains(nowPoint);
                if (contains) {
                    pointArrayList.add(nowPoint);
                    pointCount++;
                }
            } else {
                break;
            }
        }
        // k-means 数据 构造
        double[][] kmData = new double[pointArrayList.size()][2];
        for (int i = 0; i < pointArrayList.size(); i++) {
            Point point = pointArrayList.get(i);
            double[] oneData = new double[2];
            oneData[0] = point.getX();
            oneData[1] = point.getY();
            kmData[i] = oneData;
        }

        // k-means 结果

        Kmeans kmeans = new Kmeans(kmData, k);

        // 构造泰森多边形
        VoronoiInterface vo = new VoronoiInterfaceImpl();
        List<Geometry> voronoi = vo.voronoi(kmeans.getCentroids());

        HashMap result = new HashMap(10);
        result.put("polygon", polygon);
        result.put("pointList", pointArrayList);
        result.put("k-means", kmeans.getAssignments());
        result.put("k-center", kmeans.getCentroids());
        result.put("xlist", xList);
        result.put("ylist", yList);
        result.put("voronoi", voronoi);
        return result;
    }


    private static double random(double max, double min) {
        double d = (Math.random() * (max - min) + min);
        return d;
    }


}
