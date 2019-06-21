package com.huifer.planar.aset.mappingalgo.grid.lib;

/**
 * <p>Title : PointInfo </p>
 * <p>Description : 单点信息</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class PointInfo {

    /**
     * 点名
     */
    public String name;
    /**
     * x
     */
    public double x;
    /**
     * y
     */
    public double y;
    /**
     * z
     */
    public double h;
    /**
     * 角度
     */
    public double angle;


    @Override
    public String toString() {
        return "PointInfo{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", h=" + h +
                ", angle=" + angle +
                '}';
    }



}
