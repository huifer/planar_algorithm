package com.huifer.planar.aset.utils;

import com.huifer.planar.aset.utils.geo.GeomtryUtil;
import com.huifer.planar.aset.utils.geo.GeomtryUtil.IntersectionCoordinate;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class GeomtryUtilTest {

    private static final double EXACT_PRECISION = 0;

    /**
     * 判断给定点是否在直线端点上 {@link GeomtryUtil isPointOnLineSegmentStartOrEnd}
     */
    public static class PointOnLineSegmentStartOrEnd {

        @Test
        public void isPointOnLineSegmentStartOrEnd() {
            Coordinate startPoint = new Coordinate(0, 0);
            Coordinate endPoint = new Coordinate(10, 20);
            LineSegment line = new LineSegment(startPoint, endPoint);
            // 点在线上
            Coordinate pointOnLine = new Coordinate(2, 4);
            // 点在延长线上
            Coordinate pointOnExtendedLine = new Coordinate(12, 14);
            // 直线外一个点
            Coordinate pointOutLine = new Coordinate(12, 14);

            assertTrue(GeomtryUtil.isPointOnLineSegment(startPoint, line));
            assertTrue(GeomtryUtil.isPointOnLineSegment(endPoint, line));
            assertTrue(GeomtryUtil.isPointOnLineSegment(pointOnLine, line));

            assertFalse(GeomtryUtil.isPointOnLineSegment(pointOnExtendedLine, line));
            assertFalse(GeomtryUtil.isPointOnLineSegment(pointOutLine, line));
        }
    }


    /**
     * 测试线是否在面内 {@link GeomtryUtil isLineIntersectingPolygon}
     */
    public static class LineIntersectingPolygon {

        @Test
        public void isLineIntersectingPolygon() throws Exception {
            Polygon polygon = (Polygon) new WKTReader()
                    .read("POLYGON ((0 0, 10 0, 10 10, 0 10,  0 0))");
            assertFalse(GeomtryUtil.isLineIntersectingPolygon(
                    new LineSegment(new Coordinate(5, 6), new Coordinate(3, 3)), polygon));
            assertTrue(GeomtryUtil.isLineIntersectingPolygon(
                    new LineSegment(new Coordinate(-1, 3), new Coordinate(6, 13)), polygon));
        }

    }


    /**
     * linestring LineSegment 测试 {@link GeomtryUtil getLineSegment}
     */
    public static class LineSegmentTest {

        /**
         * 测试 getLineSegment
         */
        @Test
        public void getLineSegmentTest() throws Exception {
            LineString line = (LineString) new WKTReader().read("LINESTRING(0 0, 2 2, 4 4, 7 7)");
            LineSegment lineSegment = GeomtryUtil.getLineSegment(line, 0);
            assertEquals(0, lineSegment.p0.x, EXACT_PRECISION);
            assertEquals(0, lineSegment.p0.y, EXACT_PRECISION);

            lineSegment = GeomtryUtil.getLineSegment(line, 2);
            assertEquals(4, lineSegment.p0.x, EXACT_PRECISION);
            assertEquals(4, lineSegment.p0.y, EXACT_PRECISION);
        }

    }


    /**
     * 投影测试 {@link GeomtryUtil getProjectedPoint }
     */
    public static class ProjectedPointTest {

        /**
         * 平行投影
         */
        @Test
        public void projectRectanglePoint() {
            LineSegment lineA = new LineSegment(new Coordinate(0, 0), new Coordinate(10, 0));
            LineSegment LineB = new LineSegment(new Coordinate(0, 1), new Coordinate(10, 1));
            Coordinate projectedPoint = GeomtryUtil.getProjectedPoint(LineB.p0, lineA, null);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(LineB.p1, lineA, null);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p0, LineB, null);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p1, LineB, null);
            assertNull(projectedPoint);
        }

        /**
         * 外边梯形的投影
         */
        @Test
        public void projectTrapezoidPoint() {
            LineSegment lineA = new LineSegment(new Coordinate(0, 0), new Coordinate(10, 0));
            LineSegment lineB = new LineSegment(new Coordinate(5, 2), new Coordinate(15, 2));
            Coordinate projectedPoint;
            projectedPoint = GeomtryUtil.getProjectedPoint(lineB.p0, lineA, null);
            assertEquals(5, projectedPoint.x, EXACT_PRECISION);
            assertEquals(0, projectedPoint.y, EXACT_PRECISION);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineB.p1, lineA, null);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p0, lineB, null);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p1, lineB, null);
            assertEquals(10, projectedPoint.x, EXACT_PRECISION);
            assertEquals(2, projectedPoint.y, EXACT_PRECISION);

        }

        /**
         * 垂线投影
         */
        @Test
        public void projectPerpendicularPoint() {
            LineSegment lineA = new LineSegment(new Coordinate(0, 12), new Coordinate(0, 105));
            LineSegment lineB = new LineSegment(new Coordinate(24, 0), new Coordinate(46, 0));
            IntersectionCoordinate inter = new IntersectionCoordinate(0, 0, lineA, lineB);
            Coordinate projectedPoint = GeomtryUtil.getProjectedPoint(lineB.p0, lineA, inter);

            assertEquals(0, projectedPoint.x, EXACT_PRECISION);
            assertEquals(24, projectedPoint.y, EXACT_PRECISION);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineB.p1, lineA, inter);
            assertEquals(0, projectedPoint.x, EXACT_PRECISION);
            assertEquals(46, projectedPoint.y, EXACT_PRECISION);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p0, lineB, inter);
            assertNull(projectedPoint);
            projectedPoint = GeomtryUtil.getProjectedPoint(lineA.p1, lineB, inter);
            assertNull(projectedPoint);
        }


        /**
         * 交叉线投影
         */
        @Test
        public void projectIntersectingPoint() {
            LineSegment edgeA = new LineSegment(new Coordinate(0, 0),
                    new Coordinate(0, 40));
            LineSegment edgeB = new LineSegment(new Coordinate(30, 10), new Coordinate(10,
                    20));
            IntersectionCoordinate intersection = new IntersectionCoordinate(0, 25, edgeA, edgeB);

            Coordinate projectedPoint = GeomtryUtil.getProjectedPoint(edgeB.p0, edgeA,
                    intersection);
            assertNull(projectedPoint);

            projectedPoint = GeomtryUtil.getProjectedPoint(edgeB.p1, edgeA,
                    intersection);
            assertEquals(0, projectedPoint.x, EXACT_PRECISION);
            assertEquals(13.819660112501051, projectedPoint.y, EXACT_PRECISION);

            projectedPoint = GeomtryUtil.getProjectedPoint(edgeA.p0, edgeB,
                    intersection);
            assertEquals(22.360679774997898, projectedPoint.x, EXACT_PRECISION);
            assertEquals(13.819660112501051, projectedPoint.y, EXACT_PRECISION);

            projectedPoint = GeomtryUtil.getProjectedPoint(edgeA.p1, edgeB,
                    intersection);
            assertNull(projectedPoint);
        }


    }


    /**
     * 相交测试 {@link GeomtryUtil getIntersectionCoordinatePoint }
     */
    public static class IntersectionTest {

        /**
         * 不相交
         */
        @Test
        public void notIntersectionEdge() {
            LineSegment l1 = new LineSegment(0, 10, 1, 9);
            LineSegment l2 = new LineSegment(0, 0, 10, 0);
            Coordinate intersectionCoordinatePoint = GeomtryUtil
                    .getIntersectionCoordinatePoint(l1, l2);
            assertEquals(10, intersectionCoordinatePoint.x, EXACT_PRECISION);
            assertEquals(0, intersectionCoordinatePoint.y, EXACT_PRECISION);
        }

        /**
         * 相交
         */
        @Test
        public void intersectionEdge() {
            LineSegment lineA = new LineSegment(5, 1, 10, 0);
            LineSegment lineB = new LineSegment(0, 0, 10, 0);

            Coordinate intersection = GeomtryUtil.getIntersectionCoordinatePoint(lineA, lineB);
            assertEquals(10, intersection.x, EXACT_PRECISION);
            assertEquals(0, intersection.y, EXACT_PRECISION);
        }

        /**
         * 重叠
         */
        @Test
        public void overlayEdge() {
            LineSegment lineA = new LineSegment(0, 1, 10, 1);
            LineSegment lineB = new LineSegment(0, 0, 10, 0);
            Coordinate intersection = GeomtryUtil.getIntersectionCoordinatePoint(lineA, lineB);
            assertNull(intersection);
        }

    }


}
