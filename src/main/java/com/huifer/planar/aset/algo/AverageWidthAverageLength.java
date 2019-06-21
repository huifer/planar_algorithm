package com.huifer.planar.aset.algo;

/**
 * <p>Title : AverageWidthAverageLength </p>
 * <p>Description : 多边形(非回型)平均宽度 平均长度</p>
 *
 * @author huifer
 * @date 2019-01-14
 */
public interface AverageWidthAverageLength {

    /**
     * 平均宽度
     * @param count 分成多少分
     * @param wkt polygon wkt 描述
     * @return 平均宽度
     */
    double calcAverageWidth(int count, String wkt);


    /**
     * 平均长度
     * @param count 分成多少分
     * @param wkt polygon wkt 描述
     * @return 平均长度
     */
    double calcAverageLength(int count, String wkt);

}
