package com.huifer.planar.aset.mappingalgo.coortrans.lib;

/**
 * <p>Title : PointInfo </p>
 * <p>Description : 点信息</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class PointInfo {

    public String name;
    /**
     * 大地坐标系b
     */
    public double b;
    /**
     * 大地坐标系l
     */
    public double l;
    /**
     * 大地坐标系h
     */
    public double h;
    /**
     * 空间直角坐标系 x
     */
    public double x;
    /**
     * 空间直角坐标系 y
     */
    public double y;
    /**
     * 空间直角坐标系 z
     */
    public double z;
    /**
     * 高斯平面直角坐标系 x
     */
    public double gsx;
    /**
     * 高斯平面直角坐标系 y
     */
    public double gsy;


    @Override
    public String toString() {
        return "PointInfo{" +
                "name='" + name + '\'' +
                ", b=" + b +
                ", l=" + l +
                ", h=" + h +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", gsx=" + gsx +
                ", gsy=" + gsy +
                '}';
    }
}
