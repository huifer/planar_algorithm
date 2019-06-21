package com.huifer.planar.aset.utils.geo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/**
 * <p>Title : GeomtryUtil </p>
 * <p>Description : geotools geomtry 工具包</p>
 *
 * @author huifer
 * @date 2019-01-16
 */
public class GeomtryUtil {

    public GeomtryUtil() {
    }


    /**
     * 普通图形转换成  集合类
     *
     * @param g 普通图形 点、线、面
     * @return 多点、多线、多面
     */
    public static Geometry toMulti(Geometry g) {
        if (g instanceof GeometryCollection) {
            return g;
        } else if (g instanceof Polygon) {
            return g.getFactory().createMultiPolygon(new Polygon[]{(Polygon) g});
        } else if (g instanceof Point) {
            return g.getFactory().createMultiPoint(new Point[]{(Point) g});
        } else if (g instanceof LineString) {
            return g.getFactory().createMultiLineString(new LineString[]{(LineString) g});
        }
        return null;
    }


    /**
     * 获取geometry集合
     * <p> 专门针对 GeometryCollection</p>
     *
     * @param geometry geometry
     * @return geometry集合
     */
    public static List<Geometry> getGeometries(Geometry geometry) {
        List<Geometry> gs = new ArrayList<>();
        if (!(geometry instanceof GeometryCollection)) {
            gs.add(geometry);
            return gs;
        }
        GeometryCollection gco = (GeometryCollection) geometry;
        for (int i = 0; i < gco.getNumGeometries(); i++) {
            Geometry geometryN = gco.getGeometryN(i);
            if (geometryN.isEmpty()) {
                continue;
            }
            if (geometryN instanceof GeometryCollection) {
                gs.addAll(getGeometries(geometryN));
            } else {
                gs.add(geometryN);
            }
        }
        return gs;
    }

    /**
     * 确定 vertex 到对边的投影 （ 角平分线的垂直投影）
     *
     * @param vertex 待测投影点
     * @param opposingEdge 对边
     * @param intersectionPoint 线段延长后交点
     * @return 点
     */
    public static Coordinate getProjectedPoint(Coordinate vertex, LineSegment opposingEdge,
            IntersectionCoordinate intersectionPoint) {
        // 绘制一条垂直余对边的直线 并且穿过交点 情况分为2种
        //1. 顶点和顶点在这个支线的不同侧
        // 针对第一点将投影落在对边外即交点的另一侧
        //2. 顶点和顶点在一侧
        // 针对第二点移除垂线将另一侧的边缘部分来缩短对边 在交点处绘制垂直于对边的直线 ,
        // 将端点跟着交点旋转90度

        if (intersectionPoint != null) {

            if (intersectionPoint.belongsToOneOfTheEdges) {
                // 根据交点旋转90
                AffineTransformation roat90deg = AffineTransformation
                        .rotationInstance(1, 0, intersectionPoint.x, intersectionPoint.y);
                Coordinate pointAlongPerpendicularLine = roat90deg
                        .transform(opposingEdge.p0, new Coordinate());
                // 旋转后的直线
                LineSegment perpendicularLine = new LineSegment(intersectionPoint,
                        pointAlongPerpendicularLine);

                int orientationIndexOfVertex = perpendicularLine.orientationIndex(vertex);
                if (isPointOnLineSegmentStartOrEnd(intersectionPoint, opposingEdge)) {
                    // 交点在边缘
                    int orientationIndexOfP0 = perpendicularLine.orientationIndex(opposingEdge.p0);
                    if (orientationIndexOfVertex == orientationIndexOfP0) {
                        opposingEdge = new LineSegment(opposingEdge.p0, intersectionPoint);

                    } else {
                        opposingEdge = new LineSegment(intersectionPoint, opposingEdge.p1);
                    }

                } else {
                    int orientationIndexOfEdge = perpendicularLine
                            .orientationIndex(opposingEdge);
                    boolean vertexIsOnPerpendicularLine = false;
                    if (!vertexIsOnPerpendicularLine
                            && orientationIndexOfVertex != orientationIndexOfEdge) {
                        return null;
                    }
                }
            }
            //   vertex 到交点的距离
            double distVertex = vertex.distance(intersectionPoint);

            double distV1 = intersectionPoint.distance(opposingEdge.p0);
            double distV2 = intersectionPoint.distance(opposingEdge.p1);

            if (distVertex >= Math.max(distV1, distV2)
                    || distVertex <= Math.min(distV1, distV2)) {
                return null;
            }

            Coordinate furtherPoint = getFurtherEnd(intersectionPoint, opposingEdge);
            LineSegment extendedOpposingEdge = new LineSegment(intersectionPoint, furtherPoint);
            return extendedOpposingEdge
                    .pointAlong(distVertex / extendedOpposingEdge.getLength());
        } else {
            Coordinate closestPointOnOpposingLine = opposingEdge.project(
                    vertex);
            return isPointOnLineSegmentStartOrEnd(closestPointOnOpposingLine, opposingEdge)
                    ? closestPointOnOpposingLine : null;
        }


    }

    /**
     * 是否在容差范围内
     *
     * @param a 值
     * @param b 值
     * @return boolean
     */
    public static boolean equalWithDelta(double a, double b) {
        double es = 1e-7;
        return Math.abs(a - b) < es;
    }

    /**
     * 从 lineString 中获取第 index个
     *
     * @param lineString lineString
     * @param index index
     * @return LineSegment
     */
    public static LineSegment getLineSegment(LineString lineString, int index) {
        return getLineSegment(lineString, index, false);
    }

    /**
     * 从 lineString 中获取第 index个 可否修改
     *
     * @param lineString lineString
     * @param index 索引值
     * @param revamp 是否修改
     * @return LineSegment
     */
    public static LineSegment getLineSegment(LineString lineString, int index, boolean revamp) {
        LineSegment lineSegment = new LineSegment(lineString.getCoordinateN(index),
                lineString.getCoordinateN(index + 1));
        if (revamp) {
            lineSegment.reverse();
        }
        return lineSegment;
    }

    /**
     * 求解两条线段延长后的交点
     *
     * @param lineA 线段A
     * @param lineB 线段B
     * @return {@link IntersectionCoordinate}
     */
    public static IntersectionCoordinate getIntersectionCoordinatePoint(LineSegment lineA,
            LineSegment lineB) {
        double x1 = lineA.p0.x;
        double y1 = lineA.p0.y;
        double x2 = lineA.p1.x;
        double y2 = lineA.p1.y;

        double x3 = lineB.p0.x;
        double y3 = lineB.p0.y;
        double x4 = lineB.p1.x;
        double y4 = lineB.p1.y;

        double det1And2 = determinant(x1, y1, x2, y2);
        double det3And4 = determinant(x3, y3, x4, y4);
        double x1LessX2 = x1 - x2;
        double y1LessY2 = y1 - y2;
        double x3LessX4 = x3 - x4;
        double y3LessY4 = y3 - y4;

        double det1Less2And3Less4 = determinant(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
        if (det1Less2And3Less4 == 0) {
            return null;
        }

        double x = determinant(det1And2, x1LessX2, det3And4, x3LessX4) / det1Less2And3Less4;
        double y = determinant(det1And2, y1LessY2, det3And4, y3LessY4) / det1Less2And3Less4;
        return new IntersectionCoordinate(x, y, lineA, lineB);
    }

    /**
     * 根据测试点返回距离测试线远的顶点
     *
     * @param point 测试点
     * @param lineSegment 测试线
     * @return 点
     */
    private static Coordinate getFurtherEnd(Coordinate point, LineSegment lineSegment) {
        return point.distance(lineSegment.p0) > point.distance(lineSegment.p1) ? lineSegment.p0
                : lineSegment.p1;
    }

    /**
     * 判断线是否在面内
     *
     * @param lineSegment 待测线
     * @param polygon 平面
     * @return boolean
     */
    public static boolean isLineIntersectingPolygon(LineSegment lineSegment, Polygon polygon) {
        LineIntersector lineIntersector = new RobustLineIntersector();
        List<LineSegment> edges = getLineSegment(polygon.getExteriorRing());
        for (LineSegment edge : edges) {
            lineIntersector.computeIntersection(lineSegment.p0, lineSegment.p1, edge.p0, edge.p1);
            if (lineIntersector.hasIntersection() && lineIntersector.isProper()) {
                return true;
            }
        }
        return false;
    }

    /**
     * lineString 2 LineSegment
     *
     * @param lineString lineString
     * @return LineSegment list
     */
    public static List<LineSegment> getLineSegment(LineString lineString) {
        int numPoints = lineString.getNumPoints();
        List<LineSegment> lineSegmentList = new ArrayList<>();
        Coordinate start = lineString.getStartPoint().getCoordinate();
        for (int i = 1; i < numPoints; i++) {
            Coordinate end = lineString.getCoordinateN(i);
            lineSegmentList.add(new LineSegment(start, end));
            start = end;
        }
        return lineSegmentList;
    }

    /**
     * 判断给定点是否在直线端点上
     *
     * @param point 待测点
     * @param lineSegment 直线
     * @return boolean
     */
    public static boolean isPointOnLineSegmentStartOrEnd(Coordinate point,
            LineSegment lineSegment) {
        if (point.equals(lineSegment.p0) || point.equals(lineSegment.p1)) {
            return false;
        }
        return isPointOnLineSegment(point, lineSegment);
    }

    /**
     * 判断给定点是否在直线端点上
     *
     * @param point 待测点
     * @param lineSegment 直线
     * @return boolean
     */
    public static boolean isPointOnLineSegment(Coordinate point, LineSegment lineSegment) {
        double length = lineSegment.getLength();
        double distP0 = point.distance(lineSegment.p0);
        double distP1 = point.distance(lineSegment.p1);
        // 长度相等 true
        if (distP0 + distP1 == length) {
            return true;
        }
        LineIntersector lineIntersector = new RobustLineIntersector();
        lineIntersector.computeIntersection(point, lineSegment.p0, lineSegment.p1);
        return lineIntersector.hasIntersection();
    }

    /**
     * 行列式
     *
     * @param a 值
     * @param b 值
     * @param c 值
     * @param d 值
     * @return 行列式
     */
    private static Double determinant(double a, double b, double c, double d) {
        return a * d - b * c;
    }

    /**
     * 获取 Geometry 的 x列表
     *
     * @param coordinates 坐标组
     * @return 坐标组中的x
     */
    public static ArrayList<Double> getGeometryX(Coordinate[] coordinates) {
        HashMap<Integer, ArrayList<Double>> integerListHashMap = coordinatesXY(coordinates);
        return integerListHashMap.get(0);
    }

    /**
     * 获取 Geometry 的 y列表
     *
     * @param coordinates 坐标组
     * @return 坐标组中的y
     */
    public static ArrayList<Double> getGeometryY(Coordinate[] coordinates) {
        HashMap<Integer, ArrayList<Double>> integerListHashMap = coordinatesXY(coordinates);
        return integerListHashMap.get(1);
    }

    /***
     * 获取坐标组的 xy
     * @param coordinates 坐标组
     * @return {0：x , 1：y}
     */
    private static HashMap<Integer, ArrayList<Double>> coordinatesXY(Coordinate[] coordinates) {
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            double x = coordinate.x;
            double y = coordinate.y;
            xList.add(x);
            yList.add(y);
        }
        HashMap<Integer, ArrayList<Double>> result = new HashMap<>(2);
        result.put(0, xList);
        result.put(1, yList);
        return result;
    }

    /**
     * 投影点
     */
    public static class IntersectionCoordinate extends Coordinate {

        private static final long serialVersionUID = 1L;

        private final boolean belongsToOneOfTheEdges;

        public IntersectionCoordinate(double x, double y, LineSegment edgeA, LineSegment edgeB) {
            super(x, y);
            this.belongsToOneOfTheEdges =
                    isPointOnLineSegmentStartOrEnd(this, edgeA) || isPointOnLineSegmentStartOrEnd(
                            this, edgeB);
        }
    }

}
