package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.TroughLineWithSort;
import java.util.List;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : CuttingConcavePolygon </p>
 * <p>Description : 凹多边形的切割</p>
 *
 * @author huifer
 * @date 2019-03-01
 */
public interface CuttingConcavePolygon {

    /**
     * 凹多边形的凸多边形拆分
     * @param polygon 凹多边形
     * @return {@link TroughLineWithSort}
     * @throws Exception
     */
    List<TroughLineWithSort> concaveSplit(Polygon polygon) throws Exception;

}
