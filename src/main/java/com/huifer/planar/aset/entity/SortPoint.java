package com.huifer.planar.aset.entity;

import lombok.Data;
import lombok.ToString;

/**
 * <p>Title : SortPoint </p>
 * <p>Description : 排序点</p>
 *
 * @author huifer
 * @date 2018/12/17
 */
@ToString
@Data
public class SortPoint extends MyPoint {

    private OvlayerEnum leftOrRight;
    /**
     * 点编号
     */
    private int leftOrRightCode;
    /**
     * 投影点
     */
    private MyPoint projectivePoint;
    /**
     * 到第一个点的距离
     */
    private double distanceWithP1;

    public SortPoint() {
    }

    public SortPoint(double x, double y) {
        super(x, y);
    }

}
