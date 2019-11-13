package com.huifer.planar.aset.utils;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommonUtilsTest {


    @Test
    public void pointOnLineTest() throws Exception {
        LineString l1 = (LineString) new WKTReader()
                .read("LINESTRING (3128.2956 1454.2733 , 5193.036 1454.2733 )");
        Point p1 = (Point) new WKTReader().read("POINT( 3551.1528 1454.3)");
        boolean b = CommonUtils.pointOnLine(l1, p1);
        System.out.println(b);
    }


    @Test
    public void lineSplitTest() throws Exception {
        LineString l1 = (LineString) new WKTReader()
                .read("LINESTRING (3128.2956 1454.2733 , 5193.036 1454.2733 )");
        Point p1 = (Point) new WKTReader().read("POINT( 3551.1528 1454.2733)");
        Point p2 = (Point) new WKTReader().read("POINT( 4543.4202 1454.2733)");
//        List<LineString> lineStrings = CommonUtils.lineSplitPoint(l1, p1);

        List<Point> ps = new ArrayList<>();
        ps.add(p1);
        ps.add(p2);
        List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);

    }

    @Test
    public void lineIntersectionLineTest() throws Exception {

        LineString l1 = (LineString) new WKTReader()
                .read("LINESTRING (16.4150 6.3828, 20.7857 14.1406 )");
        LineString l2 = (LineString) new WKTReader()
                .read("LINESTRING (31.7650 7.8855, 21.3452 9.4230 )");
        Point point = CommonUtils.lineIntersectionLine(l1, l2);
//        18.3748 , 9.8613
        System.out.println(point);
    }

    @Test
    public void positiveDirectionTest() {
        LineString hengZhong = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87690.3662, 73733.4319),
                        new Coordinate(87736.3536, 73733.4319)
                }
        );
        LineString hengZuo = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87663.8101, 73733.4319),
                        new Coordinate(87690.3662, 73733.4319)
                }
        );

        LineString hengYou = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87736.3536, 73733.4319),
                        new Coordinate(87759.3473, 73733.4319)
                }
        );

        assertTrue(CommonUtils.positiveDirection(hengZhong, hengZuo));
        assertTrue(CommonUtils.positiveDirection(hengZhong, hengYou));

        LineString hengWaiNi = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87830.6448, 73733.4319),
                        new Coordinate(87771.8651, 73733.4319)
                }
        );
        assertFalse(CommonUtils.positiveDirection(hengZhong, hengWaiNi));

        LineString shuZhong = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87956.4789, 73864.2352),
                        new Coordinate(87956.4789, 73728.4010)
                }
        );
        LineString shuShang = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87956.4789, 73977.6721),
                        new Coordinate(87956.4789, 73864.2352)
                }
        );
        LineString shuXia = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87830.6448, 73733.4319),
                        new Coordinate(87771.8651, 73733.4319)
                }
        );
        LineString shuWaiNi = new GeometryFactory().createLineString(
                new Coordinate[]{
                        new Coordinate(87956.4789, 73493.6503),
                        new Coordinate(87956.4789, 73596.4867)
                }
        );
        assertTrue(CommonUtils.positiveDirection(shuZhong, shuShang));
        assertTrue(CommonUtils.positiveDirection(shuZhong, shuXia));
        assertFalse(CommonUtils.positiveDirection(shuZhong, shuWaiNi));

        assertTrue(CommonUtils.positiveDirection(shuZhong, hengZhong));

    }

    public static class LineSplitTest {


        @Test
        public void testTop2Down() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (3266.4499 1478.0327, 3266.4499 -329.2992)");
            Point p1 = (Point) new WKTReader().read("POINT (3266.4499 1044.2693) ");
            Point p2 = (Point) new WKTReader().read("POINT (3266.4499 680.3762)");
            Point p3 = (Point) new WKTReader().read("POINT (3266.4499 377.132)");
            Point p4 = (Point) new WKTReader().read("POINT (3266.4499 -54.1487)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从上到下=============================");

            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);

        }


        @Test
        public void testDown2Top() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (7084.5367 1031.0655, 7084.5367 2896.0781)");
            Point p1 = (Point) new WKTReader().read("POINT (7084.5367 1325.4429) ");
            Point p2 = (Point) new WKTReader().read("POINT (7084.5367 1756.7236)");
            Point p3 = (Point) new WKTReader().read("POINT (7084.5367 2059.9678)");
            Point p4 = (Point) new WKTReader().read("POINT (7084.5367 2423.8609)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从下到上=============================");
            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);

        }


        @Test
        public void testLeft2Right() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (9109.4277 313.3471, 12703.4883 313.3471)");
            Point p1 = (Point) new WKTReader().read("POINT (9621.2629 313.3471) ");
            Point p2 = (Point) new WKTReader().read("POINT (10267.8199 313.3471)");
            Point p3 = (Point) new WKTReader().read("POINT (10895.3606 313.3471)");
            Point p4 = (Point) new WKTReader().read("POINT (11732.0814 313.3471)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从左到右=============================");
            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);

        }


        @Test
        public void testRight2Left() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (12356.7713 -2071.7418, 9365.7994 -2071.7418)");
            Point p1 = (Point) new WKTReader().read("POINT (11650.0956 -2071.7418) ");
            Point p2 = (Point) new WKTReader().read("POINT (11083.317 -2071.7418)");
            Point p3 = (Point) new WKTReader().read("POINT (10528.1053 -2071.7418)");
            Point p4 = (Point) new WKTReader().read("POINT (9729.9885 -2071.7418)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从右到左=============================");
            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);

        }


        @Test
        public void testRightTop2LeftDown() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (24203.9738 3623.7518, 21088.2016 -267.1532)");
            Point p1 = (Point) new WKTReader().read("POINT (23244.5037 2425.5875) ");
            Point p2 = (Point) new WKTReader().read("POINT (22599.0449 1619.5534)");
            Point p3 = (Point) new WKTReader().read("POINT (21931.7061 786.196)");
            Point p4 = (Point) new WKTReader().read("POINT (21242.4874 -74.4846)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从右上到左下=============================");
            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);
        }


        @Test
        public void testLeftDown2RightTop() throws Exception {
            LineString l1 = (LineString) new WKTReader()
                    .read("LINESTRING (22545.188 -6399.2471, 26738.6243 -254.8907)");
            Point p1 = (Point) new WKTReader().read("POINT (22576.2504 -6353.7334) ");
            Point p2 = (Point) new WKTReader().read("POINT (23870.3164 -4457.6268)");
            Point p3 = (Point) new WKTReader().read("POINT (24794.7983 -3103.0464)");
            Point p4 = (Point) new WKTReader().read("POINT (25908.334 -1471.4586)");
            List<Point> ps = new ArrayList<>();
            ps.add(p1);
            ps.add(p2);
            ps.add(p3);
            ps.add(p4);
            List<LineString> lineStrings = CommonUtils.lineSplit(l1, ps);
            System.out.println("=============================从左下到右上=============================");
            lineStrings.forEach(s -> System.out.println(s));
            boolean exact = l1.getEndPoint().equalsExact(lineStrings.get(0).getStartPoint());
            assertTrue(exact);
        }


    }

}
