package com.huifer.planar.aset.entity;

import com.huifer.planar.aset.algo.GraphInterface;
import com.huifer.planar.aset.algo.LineOverInterface;
import com.huifer.planar.aset.algo.impl.ovlayer.GraphCore;
import com.huifer.planar.aset.algo.impl.ovlayer.LineOverCore;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.huifer.planar.aset.entity.MyPoint.onSegment;

/**
 * 描述:
 * 线段
 *
 * @author huifer
 */
@Data
@NoArgsConstructor
public class MyLine {


    private MyPoint point1;
    private MyPoint point2;

    private double distance;
    /**
     * 斜率
     */
    private double slope;


    public MyLine(MyPoint point1, MyPoint point2) {
        this.point1 = point1;
        this.point2 = point2;
        double xLen = Math.abs(point1.getX() - point2.getX());
        double yLen = Math.abs(point1.getY() - point2.getY());
        this.distance = Math.pow((xLen * xLen + yLen * yLen), 0.5);
        this.slope = calcSlope(point1, point2);
    }


    /**
     * 求直线外一点到直线上的投影点
     *
     * @param pLine    线上一点
     * @param k        斜率
     * @param pOut     线外一点
     * @param pProject 投影点
     */
    public static void getProjectivePoint(MyPoint pLine, double k, MyPoint pOut, MyPoint pProject) {

        if (k == 0) {
            pProject.setX(pOut.getX());
            pProject.setY(pLine.getY());
        } else {
            pProject.setX(
                    ((k * pLine.getX() + pOut.getX() / k + pOut.getY() - pLine.getY()) / (1 / k
                            + k)));
            pProject.setY((-1 / k * (pProject.getX() - pOut.getX()) + pOut.getY()));
        }

    }

    /**
     * 直线旋转a 度
     */
    public static MyLine roateLine(MyLine line, MyPoint basePoint, double a) {
        MyLine l = new MyLine();
        MyPoint myPoint1 = line.getPoint1().rotatePoint(basePoint, 30);
        MyPoint myPoint2 = line.getPoint2().rotatePoint(basePoint, 30);
        l.setPoint1(myPoint1);
        l.setPoint2(myPoint2);
        return l;
    }


    /**
     * 求直线外一点到直线上的投影点
     */
    public static MyPoint getProjectivePoint(MyLine line, MyPoint out) {
        LineOverInterface lineOver = new LineOverCore();
        MyPoint projectivePoint = lineOver.getProjectivePoint(line, out);
        return projectivePoint;
    }

    /**
     * 斜率计算
     */
    private static double calcSlope(MyPoint p1, MyPoint p2) {
        double slope;
        if (p1.getX() == p2.getX()) {
//            throw new RuntimeException("不存在斜率 平行 y 轴");
            slope = 0;
        }
        slope = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        return slope;
    }

    /**
     * 直线交点
     */
    public static MyPoint intersect(MyLine l1, MyLine l2) {

        LineOverInterface over = new LineOverCore();
        MyPoint tersect = over.intersectPoint(l1, l2);
        return tersect;
    }

    /**
     * 是否相交
     */
    public static boolean isIntresect(MyLine l1, MyLine l2) {
        MyPoint p1 = l1.point1;
        MyPoint q1 = l1.point2;
        MyPoint p2 = l2.point1;
        MyPoint q2 = l2.point2;
        int o1 = MyPoint.orientation(p1, q1, p2);
        int o2 = MyPoint.orientation(p1, q1, q2);
        int o3 = MyPoint.orientation(p2, q2, p1);
        int o4 = MyPoint.orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }
        return o4 == 0 && onSegment(p2, q1, q2);
    }


    /**
     * 直线绕点旋转 a 度
     *
     * @param basePoint 旋转的基点
     * @param a         度数
     */
    public void roateLine(MyPoint basePoint, double a) {
        MyPoint myPoint1 = this.getPoint1().rotatePoint(basePoint, 30);
        MyPoint myPoint2 = this.getPoint2().rotatePoint(basePoint, 30);
        setPoint1(myPoint1);
        setPoint2(myPoint2);
    }

    /**
     * 求直线外一点到直线上的投影点
     */
    public MyPoint getProjectivePoint(MyPoint out) {
        MyPoint res = new MyPoint();
        if (this.slope == 0) {
            res.setX(out.getX());
            res.setY(this.getPoint1().getY());
            return res;
        } else {
            res.setX(((this.slope * this.getPoint1().getX() + out.getX() / this.slope + out.getY()
                    - this.getPoint1().getY()) / (1 / this.slope
                    + this.slope)));
            res.setY((-1 / this.slope * (res.getX() - out.getX()) + out.getY()));
            return res;
        }
    }

    /**
     * 判断点在线的哪里
     *
     * @param point 判断点
     * @return @see ovlayerEnum
     */
    public OvlayerEnum pointWhereIsLine(MyPoint point) {
        GraphInterface graphInterface = new GraphCore();
        OvlayerEnum ovlayerEnum = graphInterface.rightHandRule(point, this);
        return ovlayerEnum;
    }


}
