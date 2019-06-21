package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.KmeanPolygonResult;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : KmeanPolygonSplitInterface </p>
 * <p>Description : kmean 切割面</p>
 *
 * @author huifer
 * @date 2019-01-16
 */
public interface KmeanPolygonSplitInterface {

    /**
     * 切割面
     * @param wkt polygon wkt描述
     * @param setp 随机点数量
     * @param k 切几个面
     * @return {@link com.huifer.planar.aset.entity.KmeanPolygonResult}
     * @throws ParseException 异常
     */
    KmeanPolygonResult splitPolygon(String wkt, int setp, int k) throws ParseException;


    /**
     * shp 切割
     *
     * @param path 文件位置
     * @param splitFiled 切分字段
     * @return 切分结果
     * @throws Exception 异常
     */
        Object splitPolygonWithShp(String path, String splitFiled) throws Exception;

}
