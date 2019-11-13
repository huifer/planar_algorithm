package com.huifer.planar.aset.algo.impl.polygon;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

/**
 * <p>Title : PolygonSegmentationCore </p>
 * <p>Description : 多边形分割</p>
 *
 * @author huifer
 * @date 2019-02-22
 */
public class PolygonSegmentationCore {

    public static void main(String[] args) throws Exception {

        // 求解凹点
        // 求多边形 逆时针方向排列点
//        // 从 节点(x最小值,Y最小值) 开始
        // 如果这个点是凹点
        // 凹点和上一个点组成凹边 ，当作射线 ( 凹点的上一个点作为射线起点，凹点作为射线经过的点 )，称为凹边射线
        // 求凹边射线 和原始多边形的交点 ,取 距离凹点近的点
//        String pg = "POLYGON ((87623.0828822501 73753.4143904365,87620.1073981973 73739.213216548,87629.1690996309 73730.4220136646,87641.882531493 73727.3112803367,87643.0997749692 73714.8683470248,87662.0346734872 73725.0120426595,87669.0676357939 73735.1557382941,87655.9484561064 73735.9672339449,87676.9120937514 73747.4634223308,87651.8909778525 73740.8362078495,87659.4649372597 73755.4431295634,87644.4522677204 73748.680665807,87645.5342619215 73760.7178512935,87635.2553170117 73750.9799034842,87630.5215923822 73760.3121034681,87623.0828822501 73753.4143904365))";
        String pg = "POLYGON ((87592.0300009156 73752.0959527557,87587.2192131532 73746.5368202303,87590.4264049948 73745.307396691,87596.252803507 73744.9866775068,87598.7651037829 73747.8731501643,87597.9633058225 73750.2250908481,87595.5579119413 73753.2719230976,87594.1146756126 73753.8064550712,87592.0300009156 73752.0959527557))";
        Polygon testPolygon = (Polygon) new WKTReader().read(pg);
        boolean convex = isConvex(testPolygon);

        System.out.println(convex);
//        Coordinate[] coordinates = testPolygon.getCoordinates();
//        Arrays.stream(coordinates).forEach(
//                s -> {
//                    System.out.println(new GeometryFactory().createPoint(s));
//                }
//        );

//        doReversedOrderPoint(testPolygon);
//        System.out.println();
    }

    /**
     * 凹多边形节点逆时针排序
     *
     * @param testPolygon 平面
     * @return 凹多边形节点逆时针排序
     */
    private static List<Point> doReversedOrderPoint(Polygon testPolygon) {
        sort(testPolygon);
        return null;
    }

    private static boolean pointCmp(Point a, Point b, Point center) {
        if (a.getY() >= 0 && b.getX() < 0) {
            return true;
        }
        if (a.getX() == 0 && b.getX() == 0) {
            return a.getY() > b.getY();
        }
        double det = (a.getX() - center.getX()) * (b.getY() - center.getY())
                - (b.getX() - center.getX()) * (a.getY() - center.getY());
        if (det < 0) {
            return true;
        }
        if (det > 0) {
            return false;
        }

        double d1 = (a.getX() - center.getX()) * (a.getX() - center.getX())
                + (a.getY() - center.getY()) * (a.getY() - center.getY());
        double d2 = (b.getX() - center.getX()) * (b.getX() - center.getY())
                + (b.getY() - center.getY()) * (b.getY() - center.getY());
        return d1 > d2;
    }

    private static void sort(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();
        double x = 0;
        double y = 0;
        for (int i = 0; i < coordinates.length; i++) {
            x += coordinates[i].x;
            y += coordinates[i].y;
        }
        double centerX = 0;
        double centerY = 0;
        centerX = x / coordinates.length;
        centerY = y / coordinates.length;

        for (int i = 0; i < coordinates.length - 1; i++) {
            for (int j = 0; j < coordinates.length - i - 1; j++) {
                Point a = new GeometryFactory().createPoint(coordinates[j]);
                Point b = new GeometryFactory().createPoint(coordinates[j + 1]);
                Point center = new GeometryFactory().createPoint(new Coordinate(centerX, centerY));

                if (pointCmp(a, b, center)) {
                    Coordinate tem = coordinates[j];
                    coordinates[j] = coordinates[j + 1];
                    coordinates[j + 1] = tem;
                }
            }
        }

//        Arrays.stream(coordinates).forEach(s -> {
//            Point aaa = new GeometryFactory().createPoint(s);
//            System.out.println(aaa);
//        });


    }

    /**
     * 寻找最小点
     *
     * @param coures 过程点集合
     * @return 最小点
     */
    private static Point doFindMinPoint(List<Point> coures) {
        Point minPoint = coures.get(0);
        for (int i = 1; i < coures.size(); i++) {
            if (coures.get(i).getY() < minPoint.getY()) {
                minPoint = coures.get(i);
            } else if (coures.get(i).getY() == minPoint.getY()) {
                if (coures.get(i).getX() < minPoint.getX()) {
                    minPoint = coures.get(i);
                }
            }
        }
        return minPoint;
    }

    /**
     * 是否是凸多边形
     *
     * @param polygon 测试面
     * @return 是/不是
     */
    private static boolean isConvex(Polygon polygon) {
        LinearRing linearRing = (LinearRing) polygon.getExteriorRing();
        int sign = 0;
        for (int i = 0; i < linearRing.getNumPoints(); i++) {
            Coordinate c0 = linearRing
                    .getCoordinateN(i == 0 ? linearRing.getNumPoints() - 1 : i - 1);
            Coordinate c1 = linearRing.getCoordinateN(i);
            Coordinate c2 = linearRing
                    .getCoordinateN(i == linearRing.getNumPoints() - 1 ? 0 : i + 1);
            double dx1 = c1.x - c0.x;
            double dy1 = c1.y - c0.y;
            double dx2 = c2.x - c1.x;
            double dy2 = c2.y - c2.y;
            double z = dx1 * dy2 - dy1 * dx2;
            int s = z >= 0.0 ? 1 : -1;
            if (sign == 0) {
                sign = s;
            } else if (sign != s) {
                return false;
            }
        }
        return true;
    }


}
