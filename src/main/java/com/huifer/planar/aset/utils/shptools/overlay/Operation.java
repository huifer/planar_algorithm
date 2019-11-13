package com.huifer.planar.aset.utils.shptools.overlay;

import com.huifer.planar.aset.utils.shptools.triangulation.Vector2D;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>Title : Operation </p>
 * <p>Description : 几何对象操作</p>
 *
 * @author huifer
 * @date 2018/10/10
 */
public class Operation {

    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    public static void main(String[] args) throws Exception {
        Operation op = new Operation();
        //创建一条线
        List<Coordinate> points1 = new ArrayList<Coordinate>();
        points1.add(op.createPoint(20, 10));
        Coordinate point = op.createPoint(0, 0);
        points1.add(op.createPoint(1, 3));
        points1.add(op.createPoint(2, 3));
        LineString line1 = op.createLine(points1);
        // 创建一个点
        Point point1 = op.createPointByWkt("point(20 10 )");
        // 创建一个面
        Polygon polygonByWKT = op.createPolygonByWKT("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry buffer = polygonByWKT.buffer(5);

//
//        // 两个几何对象的交集
//        System.out.println(op.intersectionGeo(point1, line1));
//        System.out.println(op.intersectionGeo(point1, polygonByWKT));
//        System.out.println(op.intersectionGeo(line1, polygonByWKT));
//
//        Polygon circle = op.createCircle(1, 1, 50);
//
//        System.out.println(circle);
//        System.out.println("============");
//        System.out.println(op.intersectionGeo(circle, point1));
//
//        System.out.println("++++++++++++++++");
//        Polygon s_1_2_3 = op.createPolygonByWKT("POLYGON((10 50, 15 58.6603, 20 50,  10 50))");
//        Polygon s_2_3_4 = op.createPolygonByWKT("POLYGON((15 58.6603, 20 50 , 28.6603 55,  15 58.6603))");
//        Polygon s_1_3_4 = op.createPolygonByWKT("POLYGON((10 50, 20 50 , 28.6603 55,  10 50))");
//        Polygon s_1_2_7_4_6_5_3_1 = op.createPolygonByWKT("POLYGON((10 50, 15 58.6603 , 25.4713 63.4454 , 28.6603 55, 32.3205 41.3397 , 25 41.3397 , 20 50 , 10 50))");
//// linestring() 留下
//        System.out.println(op.intersectionGeo(s_1_2_3, s_2_3_4));
//        System.out.println(op.intersectionGeo(s_1_2_3, s_1_3_4));
//        System.out.println(op.intersectionGeo(s_1_2_7_4_6_5_3_1, s_1_3_4));
    }

    /**
     * 画一个点
     *
     * @param x
     * @param y
     * @return
     */
    public Coordinate createPoint(double x, double y) {
        return new Coordinate(x, y);
    }


    /***
     * 根据wkt绘制点
     * @param wkt
     * @return
     * @throws ParseException
     */
    public Point createPointByWkt(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        Point point = (Point) reader.read(wkt);
        return point;
    }

    /***
     * 根据wkt 绘制多点
     * @return
     * @throws ParseException
     */
    public MultiPoint createMulPointByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        MultiPoint mpoint = (MultiPoint) reader.read(wkt);
        return mpoint;
    }


    /**
     * 从点数据中绘制
     *
     * @return
     */
    public LineString createLine(List<Coordinate> points) {
        Coordinate[] coords = (Coordinate[]) points.toArray(new Coordinate[points.size()]);
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }

    /***
     * 预设线
     * @return
     */
    public LineString createLineAAA() {
        Coordinate[] coords = new Coordinate[]{new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }


    /***
     * 根据wkt 绘制线
     * @return
     * @throws ParseException
     */
    public LineString createLineByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        LineString line = (LineString) reader.read(wkt);
        return line;
    }

    /**
     * 根据 wkt 绘制多线
     *
     * @return
     * @throws ParseException
     */
    public MultiLineString createMLineByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        MultiLineString line = (MultiLineString) reader.read(wkt);
        return line;
    }


    /**
     * 根据wkt 绘制面
     *
     * @return
     * @throws ParseException
     */
    public Polygon createPolygonByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        Polygon polygon = (Polygon) reader.read(wkt);
        return polygon;
    }

    /***
     * 根据 wkt 绘制多面
     * @param wkt String
     * @return
     * @throws ParseException
     */
    public MultiPolygon createMulPolygonByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader(geometryFactory);
        MultiPolygon mpolygon = (MultiPolygon) reader.read(wkt);
        return mpolygon;
    }

    public GeometryFactory getGeometryFactory() {
        return geometryFactory;
    }

    /***
     * 数据集合
     * @return
     * @throws ParseException
     */
    public GeometryCollection createGeoCollect() throws ParseException {
        LineString line = createLineAAA();
        Polygon poly = createPolygonByWKT("MULTIPOLYGON(((40 10, 30 0, 40 10, 30 20, 40 10),(30 10, 30 0, 40 10, 30 20, 30 10)))");
        Geometry g1 = geometryFactory.createGeometry(line);
        Geometry g2 = geometryFactory.createGeometry(poly);
        Geometry[] garray = new Geometry[]{g1, g2};
        GeometryCollection gc = geometryFactory.createGeometryCollection(garray);
        return gc;
    }

    /**
     * 创建一个圆，圆心(x,y) 半径RADIUS
     *
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public Polygon createCircle(double x, double y, final double radius) {
        //圆上面的点个数
        final int sides = 32;
        Coordinate[] coords = new Coordinate[sides + 1];
        for (int i = 0; i < sides; i++) {
            double angle = ((double) i / (double) sides) * Math.PI * 2.0;
            double dx = Math.cos(angle) * radius;
            double dy = Math.sin(angle) * radius;
            coords[i] = new Coordinate((double) x + dx, (double) y + dy);
        }
        coords[sides] = coords[0];
        LinearRing ring = geometryFactory.createLinearRing(coords);
        Polygon polygon = geometryFactory.createPolygon(ring, null);
        return polygon;
    }


    /**
     * 返回(A)与(b)中距离最近的两个点的距离
     *
     * @param a
     * @param b
     * @return
     */
    public double distanceGeo(Geometry a, Geometry b) {
        return a.distance(b);
    }

    /**
     * 两个几何对象的交集
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry intersectionGeo(Geometry a, Geometry b) {
        return a.intersection(b);
    }

    /**
     * 几何对象合并
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry unionGeo(Geometry a, Geometry b) {
        return a.union(b);
    }

    /**
     * 在A几何对象中有的，但是B几何对象中没有
     *
     * @param a
     * @param b
     * @return
     */
    public Geometry differenceGeo(Geometry a, Geometry b) {
        return a.difference(b);
    }


    /***
     * 面数据获取点坐标
     * @param polygonByWKT
     * @return
     */
    public List<double[]> polygonToPoint(Polygon polygonByWKT) {
        Coordinate[] coordinates = polygonByWKT.getCoordinates();
        List<double[]> pointList = new ArrayList<>();

        Arrays.stream(coordinates).forEach(
                s -> {
                    pointList.add(new double[]{s.x, s.y});
                }
        );
        return pointList;
    }


    /***
     * 创建三角形
     * @param a
     * @param b
     * @param c
     * @return
     */
    public Polygon createTriangle(Vector2D a, Vector2D b, Vector2D c) {
        String wkt = "POLYGON ((" + a.x + " " + a.y + " , " + b.x + " " + b.y + " , " + c.x + " " + c.y + " , " + a.x + " " + a.y + "))";
        WKTReader reader = new WKTReader(geometryFactory);
        Polygon polygon = null;
        try {
            polygon = (Polygon) reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return polygon;

    }


}
