package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.MyRectangle;

/**
 * <p>Title : MyRectangleOver </p>
 * <p>Description : 矩阵拓扑</p>
 *
 * @author huifer
 * @date 2018/12/20
 */
public interface MyRectangleOver {

    /**
     * 计算两个矩形相交面积
     *
     * @param r1 矩形1
     * @param r2 矩形2
     * @return 相交面积
     */
    double calcAreaIntersectionRectangles(MyRectangle r1, MyRectangle r2);


}
