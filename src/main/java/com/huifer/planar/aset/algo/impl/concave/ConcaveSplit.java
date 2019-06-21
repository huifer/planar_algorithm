package com.huifer.planar.aset.algo.impl.concave;

import com.huifer.planar.aset.utils.CommonUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : ConcaveSplit </p>
 * <p>Description : 凹多边形拆分算法</p>
 *
 * @author huifer
 * @date 2019-02-26
 */
//@Slf4j
public class ConcaveSplit {

    /**
     * 凹多边形的凸多边形拆分
     * @param polygon 凹多边形
     * @return 拆分关键线段 {@link TroughLineWithSort}
     * @throws Exception
     */
    public static List<TroughLineWithSort> split(Polygon polygon) throws Exception {
        // 坐标集合
        Coordinate[] cs = polygon.getCoordinates();
        // 获取凹点集合
        List<Point> troughList = IsConcave.troughPoint(polygon);
        // 获取凹点射线 , 射线的起点是凹点的上一个点 ， 射线方向是凹点  获取凹点射线的直线方程
        List<Radial> radialList = createRadial(cs, troughList);

        // 坐标集合最小最大点
        double minX = CommonUtils.polygonMinX(polygon);
        double minY = CommonUtils.polygonMinY(polygon);
        double maxX = CommonUtils.polygonMaxX(polygon);
        double maxY = CommonUtils.polygonMaxY(polygon);

        // 获取每一条射线的延长点获得新的线段
        List<LineString> newRadial = calcNewLine(minX, minY, maxX, maxY, radialList);
        // 面内凹点射线的切割保留结果
        Set<TroughLine> keepLine = keepLine(radialList, polygon);
        // 求每一个凹点距离最短的面交点
        List<TroughLineWithSort> troughLineWithSorts = calcSortLine(keepLine);

//        troughLineWithSorts.forEach(
//                s -> System.out.println(
//                        s.getAoLineResult()
//                )
//        );



////        切割凹点的最短线段 固定最后的结果线
//        calcResultLineString(troughList, troughLineWithSorts);
//        // 内部线段
//        List<LineString> internalLine = new ArrayList<>();
//        List<Point> aoPoint = new ArrayList<>();
//        for (int i = 0; i < troughLineWithSorts.size(); i++) {
//            LineString aoLineResult = troughLineWithSorts.get(i).getAoLineResult();
//            internalLine.add(aoLineResult);
//            aoPoint.add(troughLineWithSorts.get(i).getAoPoint());
//        }
//
//
//        ArrayList<Polygon> polygons = CutPolygon
//                .cutPolygonWithLineString(polygon, internalLine, aoPoint);


        return troughLineWithSorts;
    }

    /**
     * 根据凹点的出现顺序，给凹点在面内的最短距离线段做打断处理
     *
     * @param troughList 凹点列表
     * @param ts 凹点的最短距离线
     */
    private static void calcResultLineString(List<Point> troughList,
            List<TroughLineWithSort> ts) {
        // 求每一条直线的交点troughLineWithSorts
        List<Point> intersectionPoints = new ArrayList<>();
        for (int i = 0; i < ts.size(); i++) {
            for (int j = 1; j < ts.size(); j++) {
                Geometry intersection = ts.get(i).getAoLineResult()
                        .intersection(ts.get(j).getAoLineResult());
                if (intersection instanceof Point) {
                    intersectionPoints.add((Point) intersection);
                    ///////////////
                    // 这个节点要操作
                    Point iAoPoint = ts.get(i).getAoPoint();
                    Point jAoPoint = ts.get(j).getAoPoint();
                    int i1 = troughList.indexOf(iAoPoint);
                    int j1 = troughList.indexOf(jAoPoint);
                    // 谁大操作谁
                    double max = Double.max(i1, j1);
                    if (i1 == max) {
                        LineString lineString = new GeometryFactory()
                                .createLineString(new Coordinate[]{
                                        new Coordinate(ts.get(i).getAoPoint().getX(),
                                                ts.get(i).getAoPoint().getY()),
                                        new Coordinate(((Point) intersection).getX(),
                                                ((Point) intersection).getY())
                                });
                        ts.get(i).setAoLineResult(lineString);
                    } else {
                        LineString lineString = new GeometryFactory()
                                .createLineString(new Coordinate[]{
                                        new Coordinate(ts.get(j).getAoPoint().getX(),
                                                ts.get(j).getAoPoint().getY()),
                                        new Coordinate(((Point) intersection).getX(),
                                                ((Point) intersection).getY())
                                });
                        ts.get(j).setAoLineResult(lineString);
                    }
                    ///////////////
                }
            }
        }



    }


    /**
     * 凹点的最短线段
     *
     * @param keepLine {@link TroughLine} 凹点+凹点延长线
     * @return {@link TroughLineWithSort} 凹点-最短线段
     */
    private static List<TroughLineWithSort> calcSortLine(Set<TroughLine> keepLine) {
        // TODO: 2019/3/8/0008 线段互相切割问题
        // 凹点 -> 凹点射线组
        Map<Point, List<TroughLine>> aoToLine = keepLine.stream()
                .collect(Collectors.groupingBy(TroughLine::getAoPoint));

        List<TroughLineWithSort> troughLineWithSorts = new ArrayList<>();
        for (Point aoPoint : aoToLine.keySet()) {
            List<TroughLine> value = aoToLine.get(aoPoint);
            List<TroughLine> collect = value.stream().filter(ss -> ss.getAoLine().getLength() > 0)
                    .collect(Collectors.toList());
            int max = 0;
            int min = 0;
            for (int i = 0; i < collect.size(); i++) {
                if (collect.get(i).getAoLine().getLength() > collect.get(max).getAoLine()
                        .getLength()) {
                    max = i;
                }
                if (collect.get(i).getAoLine().getLength() < collect.get(min).getAoLine()
                        .getLength()) {
                    min = i;
                }
            }
            troughLineWithSorts.add(
                    new TroughLineWithSort(aoPoint, collect.get(min).getAoLine())
            );
        }
        return troughLineWithSorts;

    }

    /**
     * 凹点长线段切割
     *
     * @param radialList {@link Radial} 过程变量
     * @param polygon 外围面
     * @return 面内线段
     */
    private static Set<TroughLine> keepLine(List<Radial> radialList, Polygon polygon) {
        Set<TroughLine> result = new HashSet<>();
        for (int i = 0; i < radialList.size(); i++) {
            Radial radial = radialList.get(i);
            // 凹点
            Point aoPoint = radial.getEndPoint();
            // 凹点和延长线切割
            List<LineString> lineStringSplit = CommonUtils
                    .lineStringSplit(radial.getLongLine(), aoPoint);
            LineString oldLineString = radial.getOldLineString();
            // 凹点和每一个线段的方向性
            for (int j = 0; j < lineStringSplit.size(); j++) {
                LineString l = lineStringSplit.get(j);
                LineString ao2start = new GeometryFactory().createLineString(new Coordinate[]{
                        new Coordinate(aoPoint.getX(), aoPoint.getY()),
                        new Coordinate(l.getStartPoint().getX(), l.getStartPoint().getY())
                });
                LineString ao2end = new GeometryFactory().createLineString(new Coordinate[]{
                        new Coordinate(aoPoint.getX(), aoPoint.getY()),
                        new Coordinate(l.getEndPoint().getX(), l.getEndPoint().getY())
                });
                boolean b = CommonUtils.positiveDirection(oldLineString, ao2start);
                boolean c = CommonUtils.positiveDirection(oldLineString, ao2end);
                Set<TroughLine> sl1 = saveLineInPolygon(polygon, ao2start, b, aoPoint);
                Set<TroughLine> sl2 = saveLineInPolygon(polygon, ao2end, c, aoPoint);
                result.addAll(sl1);
                result.addAll(sl2);
            }

        }
        return result;
    }

    /**
     * 保留凹点到面的切割点
     *
     * @param polygon 外围面
     * @param ocLine 需要切割线段
     * @param b 切割线段的方向性是否和射线段一样
     * @param aoPoint 凹点
     */
    private static Set<TroughLine> saveLineInPolygon(Polygon polygon, LineString ocLine, boolean b,
            Point aoPoint) {
        Set<LineString> ls = new HashSet<>();
        Set<TroughLine> troughLines = new HashSet<>();

        if (b) {
            //  2019/2/28/0028 面内部线段保留
            // 将获得的线段和面 取交点 ,得到交点后切割成为新的线段
            List<LineString> lineStrings = CommonUtils.polygonLineString(polygon);
            lineStrings.stream().map(s -> s.intersection(ocLine))
                    .filter(intersection -> intersection instanceof Point)
                    .map(intersection -> new GeometryFactory().createLineString(
                            new Coordinate[]{
                                    new Coordinate(aoPoint.getX(), aoPoint.getY()),
                                    new Coordinate(((Point) intersection).getX(),
                                            ((Point) intersection).getY())
                            })).forEach(lineString -> {
                troughLines.add(new TroughLine(aoPoint, lineString));
                ls.add(lineString);
            });
        }
        return troughLines;
    }


    /**
     * 求射线两端延长后的新线段
     *
     * @param minX 最小x
     * @param minY 最小y
     * @param maxX 最大x
     * @param maxY 最大y
     * @param radialList 直线方程组
     * @return 新线段
     */
    private static List<LineString> calcNewLine(double minX, double minY, double maxX, double maxY,
            List<Radial> radialList) {

        List<LineString> result = new ArrayList<>();
        for (int i = 0; i < radialList.size(); i++) {
            Radial radial = radialList.get(i);
            double sx = radial.calcXX(minY);
            double sy = radial.calcYY(minX);
            double ex = radial.calcXX(maxY);
            double ey = radial.calcYY(maxX);
            if (radial.B == 0) {
                // 平行Y轴
//                System.out.println("平行Y轴");
                LineString lineString = new GeometryFactory().createLineString(new Coordinate[]{
                        new Coordinate( radial.endPoint.getX(),minY),
                        new Coordinate( radial.endPoint.getX(),maxY)
                });
                radial.setLongLine(lineString);
                result.add(lineString);
//                System.out.println(lineString);
            } else if (radial.A == 0) {
                // 平行X轴
//                System.out.println("xxxxxxxxxxxxxxxxxxxxxx");
                calcLine(minX, maxX, result, radial, sy, ey);
            } else {
                // OK
//                System.out.println("正常正常正常正常正常正常正常");
                calcLine(minX, maxX, result, radial, sy, ey);
            }


        }

        return result;
    }

    /**
     * 计算线段值
     *
     * @param minX 面的最小x
     * @param maxX 面的最大x
     * @param result 结果
     * @param radial {@link Radial }
     * @param sy 起点y
     * @param ey 终点Y
     */
    private static void calcLine(double minX, double maxX, List<LineString> result, Radial radial,
            double sy, double ey) {
        LineString lineString = new GeometryFactory().createLineString(new Coordinate[]{
                new Coordinate(minX, sy),
                new Coordinate(maxX, ey)
        });
        radial.setLongLine(lineString);
        result.add(lineString);
//        System.out.println(lineString);
    }


    /**
     * 计算所有射线 , 此处不考虑打断问题 ， 直接绘制所有射线
     *
     * @param cs polygon的点
     * @param troughList 凹点集合
     * @return {@link Radial} 射线列表
     */
    private static List<Radial> createRadial(Coordinate[] cs, List<Point> troughList) {
        List<Coordinate> csList = Arrays.stream(cs).collect(Collectors.toList());
        Set<Radial> result = new HashSet<>();
        for (int i = 0; i < csList.size(); i++) {
            for (int j = 0; j < troughList.size(); j++) {
                Point oldPoint = new GeometryFactory().createPoint(csList.get(i));
                Point trough = troughList.get(j);
                if (oldPoint.equals(trough)) {
                    if (i != 0) {
                        Radial radial = new Radial(
                                new GeometryFactory().createPoint(csList.get(i - 1)),
                                trough);
                        result.add(radial);
                    } else {
                        Radial radial = new Radial(
                                new GeometryFactory().createPoint(csList.get(csList.size() - 2)),
                                trough);
                        result.add(radial);
                    }


                }
            }
        }

        return result.stream().collect(Collectors.toList());
    }


    /**
     * 凹点+ 凹点的最短线段
     * <p>凹点到面的最短距离线段</p>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
   public static class TroughLineWithSort {

        /**
         * 凹点
         */
        private Point aoPoint;
        /**
         * 凹点的最短线段
         */
        private LineString aoLineResult;

    }


    /**
     * 凹点+ 凹点线(延长的)
     * <p>凹点到面四至的线段</p>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class TroughLine {

        /**
         * 凹点
         */
        private Point aoPoint;
        /**
         * 凹点线段
         */
        private LineString aoLine;
    }


    /**
     * 射线
     */
    @Data
    @NoArgsConstructor
    @ToString
    static class Radial {

        /**
         * 射线起点
         */
        private Point startPoint;
        /**
         * 射线终点, 凹点
         */
        private Point endPoint;


        /**
         * 直线方程系数A
         */
        private double A;
        /**
         * 直线方程系数B
         */
        private double B;
        /**
         * 直线方程常量C
         */
        private double C;
        /**
         * 凹点+ 上一个点
         */
        private LineString oldLineString;


        /**
         * 在范围内的直线
         */
        private LineString longLine;

        /**
         * 从凹点触发的线
         */
        private List<LineString> splitLine;


        public Radial(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;

            double AA = endPoint.getY() - startPoint.getY();
            double BB = startPoint.getX() - endPoint.getX();
            double CC = endPoint.getX() * startPoint.getY() - startPoint.getX() * endPoint.getY();
            this.oldLineString = new GeometryFactory().createLineString(
                    new Coordinate[]{
                            new Coordinate(startPoint.getX(), startPoint.getY()),
                            new Coordinate(endPoint.getX(), endPoint.getY()),
                    }
            );
            this.A = AA;
            this.B = BB;
            this.C = CC;
        }


        public double calcXX(double y) {
            return (-B * y - C) / A;
        }

        public double calcYY(double x) {
            return ((-A * x) - C) / B;
        }

    }


}
