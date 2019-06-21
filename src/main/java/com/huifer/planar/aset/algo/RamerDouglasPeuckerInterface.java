package com.huifer.planar.aset.algo;

import java.util.List;

/**
 * <p>Title : RamerDouglasPeuckerInterfaceCore </p>
 * <p>Description : 拉默-道格拉斯-普克算法 rdp</p>
 *
 * @author huifer
 * @date 2019-01-24
 */
public interface RamerDouglasPeuckerInterface {

    /**
     * dp算法
     * @param list 点集合
     * @param threshold 容差
     * @return 压缩后结果
     */
    List<Double[]> douglasPeucke(List<Double[]> list, double threshold);
}
