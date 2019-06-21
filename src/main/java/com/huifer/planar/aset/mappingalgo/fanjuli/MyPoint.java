package com.huifer.planar.aset.mappingalgo.fanjuli;

/**
 * <p>Title : MyPoint </p>
 * <p>Description : 测站坐标点</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class MyPoint {

    private String id;
    private double x;
    private double y;
    private double z;
    private double dist;

    public void setDist(double dist) {
        this.dist = dist;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getDist() {
        return dist;
    }

    public MyPoint(String id, double x, double y ,double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "MyPoint{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", dist=" + dist +
                '}';
    }

    public MyPoint() {
        x = y = z = dist = 0;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
