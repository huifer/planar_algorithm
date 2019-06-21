package com.huifer.planar.aset.algo.impl.minrect;


import lombok.Data;
import lombok.ToString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * <p>Title : MinCircle </p>
 * <p>Description : 最小外接圆</p>
 *
 * @author huifer
 * @date 2019-03-13
 */
@Data
@ToString
public class MinCircle {

    /**
     * 用来和0最比较的值
     */
    static final double EPS = 1e-5;
    /**
     * 中心点
     */
    private Point center;
    /**
     * 半径
     */
    private double r;
    /**
     * 半径的平方
     */
    private double rSquare;


    /**
     * 构造器
     *
     * @param centerX 中心点x
     * @param centerY 中心点y
     * @param r 半径
     */
    public MinCircle(double centerX, double centerY, double r) {
        this(new GeometryFactory().createPoint(new Coordinate(centerX, centerY)), r);

    }

    /**
     * 构造器
     *
     * @param center 中兴点
     * @param r 半径
     */
    public MinCircle(Point center, double r) {
        this.center = center;
        this.r = r;
        rSquare = r * r;
    }

    /**
     * 构造器
     *
     * @param p 点
     */
    public MinCircle(Point p) {
        this(p, 0);
    }

    /**
     * 已知2点求圆
     *
     * @param p0 点0
     * @param p1 点1
     */
    public MinCircle(Point p0, Point p1) {
        center = new GeometryFactory().createPoint(
                new Coordinate((p0.getX() + p1.getX()) * 0.5,
                        (p0.getY() + p1.getY()) * 0.5));

        r = p0.distance(p1) * 0.5;
        rSquare = r * r;
    }

    /***
     * 三点求一个圆
     * @param p0 点0
     * @param p1 点1
     * @param p2 点2
     */
    public MinCircle(Point p0, Point p1, Point p2) {

        double x0 = p0.getX();
        double y0 = p0.getY();
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double d = 2 * (x0 * (y1 - y2) + x1 * (y2 - y0) + x2 * (y0 - y1));
        double centerX = ((y1 - y2) * (x0 * x0 + (y0 - y1) * (y0 - y2)) + (x1 * x1) * (y2 - y0)
                + (x2 * x2) * (y0 - y1)) / d;
        double centerY =
                ((x0 * x0) * (x2 - x1) + x0 * ((x1 * x1) - (x2 * x2) + (y1 * y1) - (y2 * y2))
                        - (x1 * x1) * x2 + x1
                        * ((x2 * x2) - (y0 * y0) + (y2 * y2)) + x2 * ((y0 * y0) - (y1 * y1)))
                        / d;
        center = new GeometryFactory().createPoint(new Coordinate(centerX, centerY));
        r = center.distance(p0);
        rSquare = r * r;
    }

    /**
     * 点是不是被包含
     *
     * @param x x坐标
     * @param y y坐标
     * @return yes / no
     */
    public boolean contains(double x, double y) {
        return (Math.pow(x - center.getX(), 2)
                + Math.pow(y - center.getY(), 2) -
                rSquare < EPS);
    }


    /**
     * 距离的平方公式
     *
     * @param c 点1
     * @param pt 点2
     * @return 距离的平方
     */
    private double distanceSq(Point c, Point pt) {
        double px = pt.getX() - c.getX();
        double py = pt.getY() - c.getY();
        return (px * px + py * py);
    }

    /**
     * 点是否被包含
     *
     * @param point 点
     * @return yes / no
     */
    public boolean contains(Point point) {
        return (distanceSq(center, point) - rSquare < EPS);
    }

    /**
     * 点是否被包含
     * @param point
     * @param er
     * @return
     */
    public boolean contains(Point point, double er) {
        return (center.distance(point) - (r + er) < EPS);
    }





}
