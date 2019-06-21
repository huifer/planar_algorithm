package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.CuttingConcavePolygon;
import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit;
import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.TroughLineWithSort;
import java.util.List;
import org.locationtech.jts.geom.Polygon;

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
