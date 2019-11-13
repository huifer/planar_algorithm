package com.huifer.planar.aset.utils;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Double.max;
import static java.lang.Double.min;

/**
 * <p>Title : CommonUtils </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-02-14
 */
public class CommonUtils {


    public static List<LineString> p2l(Polygon polygonByLineStrings) {
        List<LineString> lc = new ArrayList<>();

        for (int i = 0; i < polygonByLineStrings.getCoordinates().length - 1; i++) {
            lc.add(
                    new GeometryFactory().createLineString(
                            new Coordinate[]{
                                    new Coordinate(polygonByLineStrings.getCoordinates()[i].x,
                                            polygonByLineStrings.getCoordinates()[i].y),
                                    new Coordinate(polygonByLineStrings.getCoordinates()[i + 1].x,
                                            polygonByLineStrings.getCoordinates()[i + 1].y)
                            }
                    )
            );
        }
        return lc;

    }


    /**
     * 是否在面列表中
     *
     * @param pglist 面列表
     * @param pg     判断面
     * @return yes/no
     */
    public static Boolean inPgList(List<Polygon> pglist, Polygon pg) {
        for (int i = 0; i < pglist.size(); i++) {
            double area = pglist.get(i).getArea();
            if (Math.abs(area - pg.getArea()) <= 0.000002) {
                return false;
            }
        }
        return true;
    }


    /**
     * 创建面根据点列表
     *
     * @param point 点列表
     * @return 面
     */
    public static Polygon createPolygonByPointList(List point) {
        List<LineString> lc = new ArrayList<>();

        for (int i = 0; i < point.size() - 1; i++) {
            Point o1 = (Point) point.get(i);
            Point o2 = (Point) point.get(i + 1);
            LineString lineString = new GeometryFactory().createLineString(new Coordinate[]{
                    o1.getCoordinate(),
                    o2.getCoordinate()
            });
            lc.add(lineString);

        }
//            lc.add(new GeometryFactory().createLineString(new Coordinate[]{
//                    ((Point) point.get(0)).getCoordinate(),
//                    ((Point) point.get(point.size() - 1)).getCoordinate(),
//            }));
        Polygon pg = createPolygonByLineStrings(lc);
        return pg;

    }


    /**
     * 线段组转换点
     *
     * @param lineStrings 线段集合
     * @return 点集合
     */
    public static List<Point> lines2point(Set<LineString> lineStrings) {
        Set<Point> ps = new HashSet<>();
        for (LineString lineString : lineStrings) {
            ps.add(lineString.getStartPoint());
            ps.add(lineString.getEndPoint());
        }
        return ps.stream().collect(Collectors.toList());
    }


    /**
     * 点是否在线段上容差0.00002
     *
     * @param ls 线段
     * @param p  点
     * @return yes/no
     */
    public static boolean pointOnLine(LineString ls, Point p) {
        return ls.intersects(p.buffer(0.00002));
    }


    /**
     * 直线切割，根据点列表
     *
     * @param ls        直线
     * @param pointList 切割点表
     * @return 切割后的线段
     */
    public static List<LineString> lineSplit(LineString ls, List<Point> pointList) {
        // 过滤不再同一条直线上的点
        Iterator<Point> pointIterator = pointList.iterator();
        while (pointIterator.hasNext()) {
            Point next = pointIterator.next();
            if (!pointOnLine(ls, next)) {
                pointIterator.remove();
            }
        }
        List<LineString> result = new ArrayList<>();
        Point startPoint = ls.getStartPoint();
        Point endPoint = ls.getEndPoint();
        List<Point> allPoint = new ArrayList<>();
        allPoint.addAll(line2point(ls));
        allPoint.addAll(pointList);
        // 线段终点连接 pointLines 中的每一个点 每次找最近的点
        // 直接连接线段即可

        // 如果x都相等，根据y排序
        // 如果y都相等，根据x排序

        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        for (int i = 0; i < allPoint.size(); i++) {
            xList.add(allPoint.get(i).getX());
            yList.add(allPoint.get(i).getY());
        }
        Set<Double> xSet = xList.stream().collect(Collectors.toSet());
        Set<Double> ySet = yList.stream().collect(Collectors.toSet());
        if (xSet.size() == 1) {
            allPoint.sort(Comparator.comparing(Point::getY));
        } else if (ySet.size() == 1) {
            allPoint.sort(Comparator.comparing(Point::getX));

        } else {
            allPoint.sort(Comparator.comparing(Point::getX));
        }

        // 如果排列后的第一个点是终点那么不用逆序
        // 如果排列后的第一个点是起点那么需要逆序
        boolean exact = allPoint.get(0).equalsExact(startPoint);
        if (exact) {
            Collections.reverse(allPoint);
        }

        for (int i = 0; i < allPoint.size() - 1; i++) {
            result.add(
                    new GeometryFactory().createLineString(new Coordinate[]{
                            new Coordinate(allPoint.get(i).getX(), allPoint.get(i).getY()),
                            new Coordinate(allPoint.get(i + 1).getX(), allPoint.get(i + 1).getY())
                    })
            );
        }

        return result;
    }


    /**
     * 直线切割 ， 根据一个点 , 切割后结果是倒过来的
     *
     * @param ls    直线
     * @param point 切割点
     * @return 切割后的线段
     */
    public static List<LineString> lineSplitPoint(LineString ls, Point point) {
        List<LineString> result = new ArrayList<>();
        LineString end2p = new GeometryFactory().createLineString(new Coordinate[]{
                new Coordinate(ls.getEndPoint().getX(), ls.getEndPoint().getY()),
                new Coordinate(point.getX(), point.getY())
        });

        LineString p2start = new GeometryFactory().createLineString(new Coordinate[]{
                new Coordinate(point.getX(), point.getY()),
                new Coordinate(ls.getStartPoint().getX(), ls.getStartPoint().getY())
        });
        result.add(end2p);
        result.add(p2start);
        return result;
    }


    /**
     * 面转点
     *
     * @param pg 面
     * @return 节点集合
     */
    public static List<Point> polygon2point(Polygon pg) {
        Set<Point> sPt = Arrays.stream(pg.getCoordinates())
                .map(coordinate -> new GeometryFactory().createPoint(coordinate))
                .collect(Collectors.toSet());

        return sPt.stream().collect(Collectors.toList());
    }


    /**
     * 从线段集合中构造面
     *
     * @param lineStrings 线段集合
     * @return 面
     */
    public static Polygon createPolygonByLineStrings(List<LineString> lineStrings) {
        Coordinate[] coordinates = lineStrings.stream()
                .flatMap(lineString -> Arrays.stream(lineString.getCoordinates()))
                .toArray(Coordinate[]::new);

        return new GeometryFactory().createPolygon(coordinates);
    }


    /**
     * 线段上的另一个点
     *
     * @param ls 线段
     * @param p  点
     * @return 另一个点
     */
    public static Point anotherPoint(LineString ls, Point p) {
        if (ls.intersects(p)) {
            return null;
        }
        if (ls.getStartPoint().equalsExact(p)) {
            return ls.getEndPoint();
        } else {
            return ls.getStartPoint();
        }

    }


    /***
     * 获取直线的另一个点
     * @param ls 直线
     * @param aoPointList 凹点列表
     * @return 直线的另一个点
     */
    public static Point anotherPoint(LineString ls, List<Point> aoPointList) {
        if (ls.getNumPoints() != 2) {
            return null;
        }
        List<Point> line2Point = line2point(ls);
        line2Point.removeAll(aoPointList);
        return line2Point.get(0);
    }


    /**
     * 直线交点
     *
     * @param l1 直线1
     * @param l2 直线2
     * @return 交点
     */
    public static Point lineIntersectionLine(LineString l1, LineString l2) {
        double x;
        double y;
        double x1 = l1.getStartPoint().getX();
        double y1 = l1.getStartPoint().getY();
        double x2 = l1.getEndPoint().getX();
        double y2 = l1.getEndPoint().getY();
        double x3 = l2.getStartPoint().getX();
        double y3 = l2.getStartPoint().getY();
        double x4 = l2.getEndPoint().getX();
        double y4 = l2.getEndPoint().getY();

        double k1 = Double.MAX_VALUE;
        double k2 = Double.MAX_VALUE;

        boolean flag1 = false;
        boolean flag2 = false;

        if ((x1 - x2) == 0) {
            flag1 = true;
        }
        if ((x3 - x4) == 0) {
            flag2 = true;
        }
        if (!flag1) {
            k1 = (y1 - y2) / (x1 - x2);
        }
        if (!flag2) {
            k2 = (y3 - y4) / (x3 - x4);
        }
        if (k1 == k2) {
            return null;
        }
        if (flag1) {
            if (flag2) {
                return null;
            }
            x = x1;
            if (k2 == 0) {
                y = y3;
            } else {
                y = k2 * (x - x4) + y4;
            }
        } else if (flag2) {
            x = x3;
            if (k1 == 0) {
                y = y1;
            } else {
                y = k1 * (x - x2) + y2;
            }
        } else {
            if (k1 == 0) {
                y = y1;
                x = (y - y4) / k2 + x4;
            } else if (k2 == 0) {
                y = y3;
                x = (y - y2) / k1 + x2;
            } else {
                x = (k1 * x2 - k2 * x4 + y4 - y2) / (k1 - k2);
                y = k1 * (x - x2) + y2;
            }
        }

        return new GeometryFactory().createPoint(new Coordinate(x, y));

    }

    /**
     * 线段转点
     *
     * @param ls 线段
     * @return 点集合
     */
    public static List<Point> line2point(LineString ls) {
        List<Point> ps = new ArrayList<>();
        for (int i = 0; i < ls.getNumPoints(); i++) {
            ps.add(ls.getPointN(i));
        }
        return ps;
    }

    /**
     * wkt 描述列表转换成 Point
     *
     * @param wkts wkt描述集合
     * @return 点集合
     */
    public static List<Point> wkt2Point(List<String> wkts) throws ParseException {
        Set<Point> pgSet = new HashSet<>();
        for (String wkt : wkts) {
            pgSet.add((Point) new WKTReader().read(wkt));
        }
        return pgSet.stream().collect(Collectors.toList());
    }

    /**
     * wkt 描述列表转换成 LineString
     *
     * @param wkts wkt描述集合
     * @return 线集合
     */
    public static List<LineString> wkt2Line(List<String> wkts) throws ParseException {
        Set<LineString> pgSet = new HashSet<>();
        for (String wkt : wkts) {
            pgSet.add((LineString) new WKTReader().read(wkt));
        }
        return pgSet.stream().collect(Collectors.toList());
    }

    /**
     * wkt 描述列表转换成 polygon
     *
     * @param wkts wkt描述集合
     * @return 面集合
     */
    public static List<Polygon> wkt2Polygon(List<String> wkts) throws ParseException {
        Set<Polygon> pgSet = new HashSet<>();
        for (String wkt : wkts) {
            pgSet.add((Polygon) new WKTReader().read(wkt));
        }
        return pgSet.stream().collect(Collectors.toList());
    }

    /***
     * 将线段中的点全部抽出来
     * @param lineStrings 只有两个点的线段
     * @return 点集合
     */
    public static List<Point> lines2point(List<LineString> lineStrings) {
        Set<Point> setPoints = new HashSet<>();
        lineStrings.stream().filter(ls -> ls.getNumPoints() == 2).forEach(ls -> {
            setPoints.add(ls.getStartPoint());
            setPoints.add(ls.getEndPoint());
        });
        return setPoints.stream().collect(Collectors.toList());
    }

    /**
     * 利用向量计算是否同向 ( 两个线在同一条直线上)
     *
     * @param a 线段a
     * @param b 线段b
     */
    public static boolean positiveDirection(LineString a, LineString b) {
        //  利用向量来计算是否在射线的正方向上
        double ax = a.getEndPoint().getX() - a.getStartPoint().getX();
        double ay = a.getEndPoint().getY() - a.getStartPoint().getY();

        double bx = b.getEndPoint().getX() - b.getStartPoint().getX();
        double by = b.getEndPoint().getY() - b.getStartPoint().getY();

        double ac = ax * bx + ay * by;
        return !(ac < 0);
    }

    /**
     * 是否共线
     *
     * @param p p
     * @param q q
     * @param r r
     * @return yes / no
     */
    public static boolean onSegment(Point p, Point q, Point r) {
        return q.getX() <= max(p.getX(), r.getY()) && q.getX() >= min(p.getX(), r.getX()) &&
                q.getY() <= max(p.getY(), r.getY()) && q.getY() >= min(p.getY(), r.getY());
    }

    /**
     * 根据输入点切割线段
     *
     * @param ls 被切割线
     * @param p  切割线的依据点
     * @return 切割后结果
     */
    public static List<LineString> lineStringSplit(LineString ls, Point p) {
        if (ls.getNumPoints() != 2) {
            return null;
        }
        List<LineString> result = new ArrayList<>();

        LineString ac = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(ls.getStartPoint().getX(), ls.getStartPoint().getY()),
                        new Coordinate(p.getX(), p.getY())
                }
        );
        LineString bc = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(p.getX(), p.getY()),
                        new Coordinate(ls.getEndPoint().getX(), ls.getEndPoint().getY())
                }
        );
        result.add(ac);
        result.add(bc);

        return result;
    }

    /**
     * 判断是否是回型面
     *
     * @param pg 面
     * @return yes / no
     */
    public static Boolean isClipPolygon(Polygon pg) {
        Polygonizer pz = new Polygonizer();
        pz.add(pg);
        return pz.getPolygons().size() == 1;
    }

    /**
     * 非回型面转线
     *
     * @param pg 面
     * @return 线集合
     */
    public static List<LineString> polygonLineString(Polygon pg) {
        if (isClipPolygon(pg)) {

            List<LineString> result = new ArrayList<>();
            for (int i = 0; i < pg.getCoordinates().length - 1; i++) {
                result.add(
                        new GeometryFactory().createLineString(
                                new Coordinate[]{
                                        new Coordinate(pg.getCoordinates()[i].x,
                                                pg.getCoordinates()[i].y),
                                        new Coordinate(pg.getCoordinates()[i + 1].x,
                                                pg.getCoordinates()[i + 1].y)
                                }
                        )
                );
            }
            return result;
        } else {
            return null;
        }
    }


    /**
     * 面的最小x
     *
     * @param pg 面
     * @return 最小x
     */
    public static double polygonMinX(Polygon pg) {
        List<Double> xlist = polygonXlist(pg);
        return xlist.stream().reduce(Double::min).get();
    }

    /**
     * 面的最小Y
     *
     * @param pg 面
     * @return 最小Y
     */
    public static double polygonMinY(Polygon pg) {
        List<Double> ylist = polygonYlist(pg);
        return ylist.stream().reduce(Double::min).get();
    }

    /**
     * 面的最大x
     *
     * @param pg 面
     * @return 最大x
     */
    public static double polygonMaxX(Polygon pg) {
        List<Double> xlist = polygonXlist(pg);
        return xlist.stream().reduce(Double::max).get();

    }

    /**
     * 面的最大y
     *
     * @param pg 面
     * @return 最大y
     */
    public static double polygonMaxY(Polygon pg) {
        List<Double> ylist = polygonYlist(pg);
        return ylist.stream().reduce(Double::max).get();
    }

    /**
     * 面的x坐标
     *
     * @param pg 面
     * @return x坐标集合
     */
    private static List<Double> polygonXlist(Polygon pg) {
        List<Double> result = new ArrayList<>();
        Arrays.stream(pg.getCoordinates()).forEach(
                s -> {
                    result.add(s.x);
                }
        );
        return result;
    }


    /**
     * 线段集合中的x坐标
     *
     * @param lss 线段集合
     * @return x坐标
     */
    private static List<Double> lineXlist(List<LineString> lss) {
        List<Double> xlist = new ArrayList<>();
        lss.forEach(
                s -> {
                    xlist.add(s.getStartPoint().getX());
                    xlist.add(s.getEndPoint().getX());
                });
        return xlist;
    }

    /**
     * 线段集合中的y坐标
     *
     * @param lss 线段集合
     * @return y坐标
     */
    private static List<Double> lineYlist(List<LineString> lss) {
        List<Double> yList = new ArrayList<>();
        lss.forEach(
                s -> {
                    yList.add(s.getStartPoint().getY());
                    yList.add(s.getEndPoint().getY());
                });
        return yList;
    }

    /**
     * 线段集合的最大x
     *
     * @param lss 线段集合
     * @return 最大x
     */
    public static double lineMaxX(List<LineString> lss) {
        List<Double> xlist = lineXlist(lss);
        return xlist.stream().reduce(Double::max).get();
    }

    /**
     * 线段集合的最大y
     *
     * @param lss 线段集合
     * @return 最大y
     */
    public static double lineMaxY(List<LineString> lss) {
        List<Double> ylist = lineYlist(lss);
        return ylist.stream().reduce(Double::max).get();
    }

    /**
     * 线段集合的最小x
     *
     * @param lss 线段集合
     * @return 最小x
     */
    public static double lineMinX(List<LineString> lss) {
        List<Double> xlist = lineXlist(lss);
        return xlist.stream().reduce(Double::min).get();
    }

    /**
     * 线段集合的最小y
     *
     * @param lss 线段集合
     * @return 最小y
     */
    public static double lineMinY(List<LineString> lss) {
        List<Double> ylist = lineYlist(lss);
        return ylist.stream().reduce(Double::min).get();
    }


    /**
     * 面的y坐标
     *
     * @param pg 面
     * @return y坐标集合
     */
    private static List<Double> polygonYlist(Polygon pg) {
        List<Double> result = new ArrayList<>();
        Arrays.stream(pg.getCoordinates()).forEach(
                s -> {
                    result.add(s.y);
                }
        );
        return result;
    }

    /**
     * 保留几位小数
     *
     * @param d        一个double值
     * @param saveNumb 保留几位小数
     * @return double保留小数后的值
     */
    public static double reserveDecimal(double d, int saveNumb) {

        BigDecimal bg = new BigDecimal(d);
        double f1 = bg.setScale(saveNumb, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

    /**
     * coordinates 构造出一个保留小数后的面
     *
     * @param coordinates 坐标组
     * @return {@link Geometry}
     */
    public static Polygon reserveDecimalGeometry(Coordinate[] coordinates) {

        Coordinate[] clist = new Coordinate[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            double px = reserveDecimal(coordinates[i].x, 3);
            double py = reserveDecimal(coordinates[i].y, 3);
            clist[i] = new Coordinate(px, py);
        }

        return new GeometryFactory().createPolygon(clist);
    }

}
