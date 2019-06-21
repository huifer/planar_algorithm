package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

/**
 * <p>Title : Dwh </p>
 * <p>Description : 距离\ 宽度\ 高度</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class Dwh {

    /***
     * 距离
     */
    private double distance;
    /**
     * 宽度
     */
    private double weight;
    /***
     * 高度
     */
    private double height;

    public Dwh(double distance, double weight, double height) {
        this.distance = distance;
        this.weight = weight;
        this.height = height;
    }

    public Dwh() {
    }

    @Override
    public String toString() {
        return "Dwh{" +
                "distance=" + distance +
                ", weight=" + weight +
                ", height=" + height +
                '}';
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
