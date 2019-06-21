package com.huifer.planar.aset.utils.kriging;

/**
 * <p>Title : Kpoint </p>
 * <p>Description : xyz</p>
 *
 * @author huifer
 * @date 2019-04-19
 */
public class Kpoint {


    private double x;
    private double y;
    private double z;

    public Kpoint() {
    }

    public Kpoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Kpoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
