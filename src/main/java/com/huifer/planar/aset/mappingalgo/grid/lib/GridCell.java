package com.huifer.planar.aset.mappingalgo.grid.lib;

/**
 * <p>Title : GridCell </p>
 * <p>Description : 单元网格 矩阵</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class GridCell {

    /**
     * 左上角
     */
    public PointInfo upLeft;
    /**
     * 右上角
     */
    public PointInfo upRight;
    /**
     * 左下角
     */
    public PointInfo downLeft;
    /**
     * 右下角
     */
    public PointInfo downRight;

    /**
     * 边长
     */
    public double sideLength;

    /**
     * 高
     */
    public double height;

    /**
     * 是否在内部
     */
    public boolean isInSide;

    @Override
    public String toString() {
        return "GridCell{" +
                "upLeft=" + upLeft +
                ", upRight=" + upRight +
                ", downLeft=" + downLeft +
                ", downRight=" + downRight +
                ", sideLength=" + sideLength +
                ", height=" + height +
                ", isInSide=" + isInSide +
                '}';
    }
}
