package com.huifer.planar.aset.mappingalgo.trajectorycompression;

/**
 * <p>Title : Point </p>
 * <p>Description : point</p>
 *
 * @author huifer
 * @date 2018/10/19
 */
public class Point {

    private double id;
    private double x;
    private double y;

    @Override
    public String toString() {
        return "Point{" +
                "id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void setId(double id) {
        this.id = id;
    }

    public Point(Double id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double getId() {
        return id;
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
}
