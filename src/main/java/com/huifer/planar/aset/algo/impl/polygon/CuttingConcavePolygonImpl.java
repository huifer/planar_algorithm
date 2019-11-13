package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.CuttingConcavePolygon;
import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit;
import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.TroughLineWithSort;
import org.locationtech.jts.geom.Polygon;

import java.util.List;

/**
 * <p>Title : CuttingConcavePolygonImpl </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-03-01
 */
public class CuttingConcavePolygonImpl implements CuttingConcavePolygon {

    @Override
    public List<TroughLineWithSort> concaveSplit(Polygon polygon) throws Exception {
        return ConcaveSplit.split(polygon);
    }
}
