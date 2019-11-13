package com.huifer.planar.aset.algo.impl.minrect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import static java.lang.Double.max;
import static java.lang.Double.min;

/**
 * <p>Title : MinCircleToE </p>
 * <p>Description : 最小外接圆</p>
 *
 * @author huifer
 * @date 2019-03-13
 */
@Deprecated
public class MinCircleToE {


    /**
     * 已知三点求圆
     *
     * @param point1 点1
     * @param point2 点2
     * @param point3 点3
     * @return 圆
     */
    public static Polygon threePointCircle(Coordinate point1, Coordinate point2,
                                           Coordinate point3) {
        Circle circle = threePointCircle(
                new P(point1.getX(), point1.getY()),
                new P(point2.getX(), point2.getY()),
                new P(point3.getX(), point3.getY())
        );
        if (circle == null) {
            return null;
        } else {

            return (Polygon) new GeometryFactory()
                    .createPoint(new Coordinate(circle.getX(), circle.getY()))
                    .buffer(circle.getR());
        }
    }

    /**
     * 已知三点求圆
     *
     * @param point1 点1
     * @param point2 点2
     * @param point3 点3
     * @return 圆
     */
    public static Polygon threePointCircle(Point point1, Point point2, Point point3) {
        Circle circle = threePointCircle(
                new P(point1.getX(), point1.getY()),
                new P(point2.getX(), point2.getY()),
                new P(point3.getX(), point3.getY())
        );

        if (circle == null) {
            return null;
        } else {

            return (Polygon) point1.getFactory()
                    .createPoint(new Coordinate(circle.getX(), circle.getY()))
                    .buffer(circle.getR());
        }
    }

    /**
     * 已知三点求圆
     *
     * @param point1 点1
     * @param point2 点2
     * @param point3 点3
     * @return 圆
     */
    private static Circle threePointCircle(P point1, P point2, P point3) {

        // 是否共线
        if (point2.x <= max(point1.x, point3.x) && point2.x >= min(point1.x, point3.x) &&
                point2.y <= max(point1.y, point3.y) && point2.y >= min(point1.y,
                point3.y)) {
            return null;
        } else {

            double mat1 =
                    ((point2.x * point2.x + point2.y * point2.y) - (point1.x * point1.x
                            + point1.y * point1.y)) * (2 * (point3.y - point1.y)) -
                            ((point3.x * point3.x + point3.y * point3.y) - (
                                    point1.x * point1.x
                                            + point1.y * point1.y)) * (2 * (point2.y
                                    - point1.y));
            double mat2 =
                    (2 * (point2.x - point1.x)) * (
                            (point3.x * point3.x + point3.y * point3.y) - (
                                    point1.x * point1.x + point1.y * point1.y)) -
                            (2 * (point3.x - point1.x)) * (
                                    (point2.x * point2.x + point2.y * point2.y) - (
                                            point1.x * point1.x + point1.y * point1.y));
            double mat3 =
                    4 * ((point2.x - point1.x) * (point3.y - point1.y)
                            - (point3.x - point1.x) * (
                            point2.y - point1.y));

            double x = mat1 / mat3;
            double y = mat2 / mat3;
            double r = Math
                    .sqrt((point1.x - x) * (point1.x - x) + (point1.y - y) * (point1.y
                            - y));

            System.out.println("三点求一个元");
            System.out.println("圆心" + x + "\t," + y);
            System.out.println("半径" + "\t" + r);
            return new Circle(x, y, r);
        }

    }


    private static P calcCircleCenterPoint(P a, P b, P c) {

        double a1 = b.x - a.x, b1 = b.y - a.y, c1 = (a1 * a1 + b1 * b1) / 2;
        double a2 = c.x - a.x, b2 = c.y - a.y, c2 = (a2 * a2 + b2 * b2) / 2;
        double d = a1 * b2 - a2 * b1;
        return new P(a.x + (c1 * b2 - c2 * b1) / d, a.y + (a1 * c2 - a2 * c1) / d);
    }


    private static double dist(P p1, P p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }


    @Data
    @NoArgsConstructor
    @ToString
    public static class Circle {

        /**
         * 圆心坐标x
         */
        double x;
        /**
         * 圆心坐标y
         */
        double y;
        /**
         * 圆的半径
         */
        double r;

        double rSquare;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.rSquare = r * r;
        }


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class P {

        /**
         * 点坐标x
         */
        double x;
        /**
         * 点坐标y
         */
        double y;
    }


}
