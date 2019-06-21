package com.huifer.planar.aset.entity;

import static java.lang.Double.max;
import static java.lang.Double.min;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 描述： 点
 *
 * @author huifer
 */
@Data
@ToString
@NoArgsConstructor
public class MyPoint {

    private double x;

    private double y;

    private int code;


    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public MyPoint(MyPoint point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /**
     * 交叉判断
     *
     * @param a 线段1 的起点
     * @param b 线段1 的终点
     * @param c 线段2 的起点
     * @param d 线段2 的终点
     */
    public static MyPoint intersection(MyPoint a, MyPoint b, MyPoint c, MyPoint d) {
        double a1 = b.y - a.y;
        double b1 = a.x - b.x;
        double c1 = a1 * (a.x) + b1 * (a.y);

        double a2 = d.y - c.y;
        double b2 = c.x - d.x;
        double c2 = a2 * (c.x) + b2 * (c.y);

        double determinant = a1 * b2 - a2 * b1;

        if (determinant == 0) {
            return new MyPoint(Double.MAX_VALUE, Double.MAX_VALUE);
        } else {
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            return new MyPoint(x, y);
        }
    }

    /**
     * 三点是否共线
     */
    public static boolean onSegment(MyPoint p, MyPoint q, MyPoint r) {
        return q.x <= max(p.x, r.x) && q.x >= min(p.x, r.x) &&
                q.y <= max(p.y, r.y) && q.y >= min(p.y, r.y);

    }

    /**
     * 三个点的方向
     *
     * @return 0 共线 1 顺时针 2 逆时针
     */
    public static int orientation(MyPoint p, MyPoint q, MyPoint r) {
        double val = (q.y - p.y) * (r.x - q.x) -
                (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return 0;
        }

        return (val > 0) ? 1 : 2;
    }

    public MyPoint rotatePoint(MyPoint basePoint, double a) {
        a = Math.PI * a / 180;
        double x = (this.x - basePoint.x) * Math.cos(a) - (this.y - basePoint.y) * Math.sin(a)
                + basePoint.x;

        double y = (this.x - basePoint.x) * Math.sin(a) + (this.y - basePoint.y) * Math.cos(a)
                + basePoint.y;
        return new MyPoint(x, y);

    }

    /**
     * 两点间距离
     */
    public double distance(MyPoint p) {
        double px = p.getX() - this.getX();
        double py = p.getY() - this.getY();
        return Math.sqrt(px * px + py * py);
    }


}
