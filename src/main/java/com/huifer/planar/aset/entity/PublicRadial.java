package com.huifer.planar.aset.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

/**
 * <p>Title : PublicRadial </p>
 * <p>Description : 射线</p>
 *
 * @author huifer
 * @date 2019-03-11
 */
@Data
@NoArgsConstructor
@ToString
public class PublicRadial {
    /**
     * 射线起点
     */
    private Point startPoint;
    /**
     * 射线终点
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



    public PublicRadial(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        double AA = endPoint.getY() - startPoint.getY();
        double BB = startPoint.getX() - endPoint.getX();
        double CC = endPoint.getX() * startPoint.getY() - startPoint.getX() * endPoint.getY();
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
