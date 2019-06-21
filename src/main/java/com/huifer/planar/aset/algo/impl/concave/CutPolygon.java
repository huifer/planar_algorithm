package com.huifer.planar.aset.algo.impl.concave;

import com.huifer.planar.aset.utils.AdjacentMatrixUtil;
import com.huifer.planar.aset.utils.CommonUtils;
import com.huifer.planar.aset.utils.simplecycles.SearchCycles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : CutPolygon </p>
 * <p>Description : 切割平面</p>
 *
 * @author huifer
 * @date 2019-03-01
 */
public class CutPolygon {


    /**
     * 面根据线段切割
     *
     * @param polygon 被切割的面
     * @param clipLine 切割线段
     * @param aoPoints 凹点
     */
    public static ArrayList<Polygon> cutPolygonWithLineString(Polygon polygon,
            List<LineString> clipLine,
            List<Point> aoPoints) throws Exception {
        // 面的组成线段
        InitParam initParam = new InitParam(polygon, clipLine, aoPoints).invoke();
        List<LineString> polygonLineString = initParam.getPolygonLineString();
        List<Point> otherPoint = initParam.getOtherPoint();
        List<LineState> initParamLineStateList = initParam.getLineStateList();

//        System.out.println("========================新的面构造完成========================");
        Polygon newPg = calcOutLine(polygonLineString, otherPoint);
        System.out.println("xinmian");
        System.out.println(newPg);
        System.out.println("xinmian");
        // 面上的点
        List<Point> polygonPoint = CommonUtils.polygon2point(newPg);
//        System.out.println("========================内部线段打断========================");
        // initParamLineStateList 内部线段的存放结果
        calcInternalLine(clipLine, initParamLineStateList, polygon);

        // 简单思路：
        // 1. 将所有的点放到 list中
        // 2. 具体点搜索方法
        // 2.1 搜索点被搜索构成面后从 list中删除

        // 最终计算用的线段 包括内部的线段以及轮廓线段
        Set<LineString> calcLine = new HashSet<>();
        // 最终计算用的点 包括轮廓点以及线段的打断点
        Set<LineString> aoPl = new HashSet<>();
        for (int i = 0; i < initParamLineStateList.size(); i++) {
            aoPl.addAll(initParamLineStateList.get(i).getClipLine());
        }
        Set<Point> calcPoint = new HashSet<>();
        calcPoint = getCalcParam(initParamLineStateList, newPg, calcLine);

        ArrayList<Polygon> polygons = calcPointJoinPoint(calcLine, calcPoint);
        return polygons;
    }

    /**
     * 获取每一个点的连接情况
     *
     * @param calcLine 计算用的线段
     * @param calcPoint 计算用的点
     */
    public static ArrayList<Polygon> calcPointJoinPoint(Set<LineString> calcLine,
            Set<Point> calcPoint) {
//        System.out.println("========================最终计算数据========================");

        List<Point> paramPoint = calcPoint.stream().collect(Collectors.toList());
        List<LineString> paramLine = calcLine.stream().collect(Collectors.toList());
//        System.out.println("====================邻接矩阵构造====================");
        List<List<Boolean>> bs = AdjacentMatrixUtil
                .createAdjacentMatrixBoolean(paramPoint, paramLine);

        SearchCycles searchCycles = new SearchCycles(bs, paramPoint);
        List nodeCycles = searchCycles.getNodeCycles();
        ArrayList<Polygon> ppppp = new ArrayList<>();
        for (int i = 0; i < nodeCycles.size(); i++) {
            List cyc = (List) nodeCycles.get(i);
            if (cyc.size() >= 3) {
                cyc.add(cyc.get(0));
                Polygon polygonByPointList = CommonUtils.createPolygonByPointList(cyc);
                Geometry geometry = polygonByPointList.convexHull();
                // 利用构建本身的凸包来避开环
                if (Math.abs(geometry.getArea() - polygonByPointList.getArea()) <= getDistance()) {
                    if (CommonUtils.inPgList(ppppp, polygonByPointList)) {
                        ppppp.add(polygonByPointList);
//                        System.out.println(
//                                polygonByPointList.getCoordinates().length + "\t" + polygonByPointList
//                                        .getCoordinates().length);
                    }
                }
            }
        }

        return ppppp;
    }

    /**
     * 计算需要运算的参数 点&线
     *
     * @param initParamLineStateList {@link LineState}
     * @param newPg 新的面
     * @param calcLine 需要计算参数的线
     * @return calcPoint 需要计算参数的点
     */
    public static Set<Point> getCalcParam(List<LineState> initParamLineStateList, Polygon
            newPg,
            Set<LineString> calcLine) {
        Set<Point> calcPoint;
        // 内部线添加到计算参数中calcLine中
        for (LineState lineState : initParamLineStateList) {
            List<LineString> clipLine1 = lineState.getClipLine();
            for (LineString lineString : clipLine1) {
//                System.out.println(lineString);
                calcLine.add(lineString);
            }
        }
        // 新的面拆成线段添加到计算参数calcLine中

//        calcLine.addAll(CommonUtils.polygonLineString(newPg)); // 成功的方法

        calcLine.addAll(CommonUtils.p2l(newPg));

        calcPoint = CommonUtils.lines2point(calcLine).stream().collect(Collectors.toSet());
        return calcPoint;
    }

    /**
     * 内部线段相交后打断构造新的线段集合
     * <p>计算内部线段的增加点</p>
     *
     * @param clipLine 内部线段
     * @param initParamLineStateList 原始的内部线段
     * @param pg 需要被切割的面
     */
    private static void calcInternalLine(List<LineString> clipLine,
            List<LineState> initParamLineStateList, Polygon pg) {
        Set<Point> pointSet = new HashSet<>();
        // 直线上的点过滤 , 过滤掉面上的点
        List<Point> polygon2point = CommonUtils.polygon2point(pg);
        for (int i = 0; i < clipLine.size(); i++) {
            LineString iLine = clipLine.get(i);
            Point iLineStartPoint = iLine.getStartPoint();
            Point iLineEndPoint = iLine.getEndPoint();
            for (int j = 0; j < clipLine.size(); j++) {
                LineString jLine = clipLine.get(j);
                // 直接把jLine 的起点和终点判断是否在 iLine 上
                Point jStartPoint = jLine.getStartPoint();
                Point jEndPoint = jLine.getEndPoint();

                Geometry jStartBuffer = jStartPoint.buffer(getDistance());
                Geometry jEndBuffer = jEndPoint.buffer(getDistance());
                getLineInPoint(pointSet, iLine, iLineStartPoint, iLineEndPoint, jStartPoint,
                        jStartBuffer, polygon2point, initParamLineStateList);
                getLineInPoint(pointSet, iLine, iLineStartPoint, iLineEndPoint, jEndPoint,
                        jEndBuffer, polygon2point, initParamLineStateList);
            }
        }

//        pointSet.forEach(s -> System.out.println(s));

        //  重新构造新的线段  ，
        //  操作对象 initParamLineStateList ,操作流程 打断线 ，
        //  如果还有 那么打断 list中的线段 最后所有结果保存到list中
        //  从内部线的终点倒着写过去
        for (int i = 0; i < initParamLineStateList.size(); i++) {
            LineState lineState = initParamLineStateList.get(i);
            List<Point> clipPoint = lineState.getClipPoint();
            LineString ls = lineState.getLs();
            List<LineString> lineStrings = CommonUtils.lineSplit(ls, clipPoint);
            lineState.setClipLine(lineStrings);
        }


    }

    private static double getDistance() {
        return 0.000002;
    }

    /**
     * 抽取直线上的节点
     * <p>1. 如果这个点是线段的起点删掉</p>
     * <p>2. 如果这个点是线段的终点删掉</p>
     * <p>3. 其他在这个线段上的点保留</p>
     *
     * @param pointSet 存储点的集合
     * @param iLine i标记线
     * @param iLineStartPoint i标记线的起点
     * @param iLineEndPoint i标记线的终点
     * @param jEndPoint j标记线的终点
     * @param jEndBuffer i标记线的起点的buffer
     * @param polygon2point 面构成的点
     * @param initParamLineStateList 内部线段集合
     */
    private static void getLineInPoint(Set<Point> pointSet, LineString iLine, Point
            iLineStartPoint,
            Point iLineEndPoint, Point jEndPoint, Geometry
            jEndBuffer, List<Point> polygon2point,
            List<LineState> initParamLineStateList) {
        if (jEndBuffer.intersects(iLine)) {
            if (!jEndPoint.equalsExact(iLineStartPoint) && !jEndPoint.equalsExact(iLineEndPoint)
                    && !polygon2point.contains(jEndPoint)) {
                // 当前操作线条是 iLine , 把iLine倒过来从终点开始切割 操作点都是 jEndPoint
                for (int i = 0; i < initParamLineStateList.size(); i++) {
                    if (initParamLineStateList.get(i).getLs().equalsExact(iLine)) {
                        initParamLineStateList.get(i).getClipPoint().add(jEndPoint);
                    }
                }
                pointSet.add(jEndPoint);
            }
        }
    }

    /**
     * 计算新的面所属线段
     * <p>计算外部轮廓的增加点</p>
     *
     * @param polygonLineString 分割面的线段集合
     * @param otherPoint 凹点外的其他点
     * @return 新的面
     */
    @Deprecated
    private static Polygon calcOutLineold(List<LineString> polygonLineString, List<Point> otherPoint) {
        for (int i = 0; i < otherPoint.size(); i++) {
            Point midpoint = otherPoint.get(i);
            Geometry buffer = midpoint.buffer(getDistance());
            for (int i1 = 0; i1 < polygonLineString.size(); i1++) {
                LineString outLine = polygonLineString.get(i1);
                if (buffer.intersects(outLine)) {
                    // 此处打断外部线段
                    Coordinate[] cs = new Coordinate[]{
                            new Coordinate(outLine.getStartPoint().getX(),
                                    outLine.getStartPoint().getY()),
                            new Coordinate(midpoint.getX(), midpoint.getY()),
                    };
                    LineString newLine = new GeometryFactory().createLineString(cs);
                    polygonLineString.set(i1, newLine);
                }
            }
        }

        // 尝试构造新的外部面
        Polygon newPolygon = CommonUtils.createPolygonByLineStrings(polygonLineString);
        return newPolygon;
    }


    /**
     * 计算新的面所属线段
     * <p>计算外部轮廓的增加点</p>
     *
     * @param polygonLineString 分割面的线段集合
     * @param otherPoint 凹点外的其他点
     * @return 新的面
     */
    private static Polygon calcOutLine(List<LineString> polygonLineString, List<Point> otherPoint) {
        //   外部轮廓线的切割
        List<LineString> l = new ArrayList<>();

        for (int i = 0; i < otherPoint.size(); i++) {
            Point midpoint = otherPoint.get(i);
            Geometry buffer = midpoint.buffer(getDistance());

            for (int i1 = 0; i1 < polygonLineString.size(); i1++) {
                LineString outLine = polygonLineString.get(i1);

                if (buffer.intersects(outLine)) {
//                    System.out.println("进入点\t" + midpoint + "\toutline" + outLine);

                    // 此处打断外部线段
                    Coordinate[] outStart2mind = new Coordinate[]{
                            new Coordinate(outLine.getStartPoint().getX(),
                                    outLine.getStartPoint().getY()),
                            new Coordinate(midpoint.getX(), midpoint.getY()),
                    };
                    Coordinate[] mid2OutEnd = new Coordinate[]{
                            new Coordinate(midpoint.getX(), midpoint.getY()),
                            new Coordinate(outLine.getEndPoint().getX(),
                                    outLine.getEndPoint().getY()),
                    };

                    LineString o2m = new GeometryFactory().createLineString(outStart2mind);
                    LineString m2o = new GeometryFactory().createLineString(mid2OutEnd);
//                            polygonLineString.set(i1, o2m);
                    l.add(o2m);
                    l.add(m2o);
                } else {
                    l.add(outLine);
                }
            }
        }
        // 尝试构造新的外部面
        Polygon polygonByLineStrings = CommonUtils.createPolygonByLineStrings(l);


        return polygonByLineStrings;
    }

    /**
     * 内部线段被保留后的数据
     */
    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    static class LineState {

        /**
         * 当前线段
         */
        private LineString ls;

        /**
         * 切割后的线段
         */
        private List<LineString> clipLine = new ArrayList<>();


        /**
         * 这个线段上的切割点
         */
        private List<Point> clipPoint = new ArrayList<>();

        public LineState(LineString ls, List<Point> clipPoint) {
            this.ls = ls;
            this.clipPoint = clipPoint;
        }

        public LineState(LineString ls) {
            this.ls = ls;
        }

    }

    /**
     * 基础运算参数
     */
    @Data
    @NoArgsConstructor
    private static class InitParam {

        /**
         * 需要分割的面
         */
        private Polygon polygon;
        /**
         * 切割元素:线段
         */
        private List<LineString> clipLine;
        /**
         * 凹点集合
         */
        private List<Point> aoPoints;
        /**
         * 分割面的线段集合
         */
        private List<LineString> polygonLineString;
        /**
         * 切割元素线中凹点以外的那个点
         */
        private List<Point> otherPoint;

        private List<LineState> lineStateList;

        public InitParam(Polygon polygon, List<LineString> clipLine, List<Point> aoPoints) {
            this.polygon = polygon;
            this.clipLine = clipLine;
            this.aoPoints = aoPoints;
        }


        public InitParam invoke() {
            polygonLineString = CommonUtils.polygonLineString(polygon);
            // 内部线段除凹点外的一个点
            otherPoint = new ArrayList<>();
            lineStateList = new ArrayList<>();

            // 1. 内部线段的点插入到原生线段上构造节点

            clipLine.forEach(clip -> {
                otherPoint.add(CommonUtils.anotherPoint(clip, aoPoints));
                lineStateList.add(new LineState(clip));
            });
            return this;
        }
    }
}
