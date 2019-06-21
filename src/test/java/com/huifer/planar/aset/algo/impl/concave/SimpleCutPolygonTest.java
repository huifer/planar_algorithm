package com.huifer.planar.aset.algo.impl.concave;

import static com.huifer.planar.aset.algo.impl.concave.SimpleCutPolygon.simpleCutPolygonWithLine;

import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.TroughLineWithSort;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

public class SimpleCutPolygonTest {

    @Test
    public void simpleCutPolygonWithLineTest() throws Exception {

        Polygon pg = (Polygon) new WKTReader()
                .read("POLYGON ((88064.0209026337 73719.926618576,88065.280916214 73720.9421520233,88066.9358577728 73719.719751358,88066.9358577728 73720.719751358,88068.1582584381 73719.6257190704,88068.1958713531 73722.1269397736,88067.0298862457 73721.2430477142,88066.0895786285 73721.8824596405,88065.0895786285 73721.8824596405,88065.0895786285 73720.8824596405,88063.8328418732 73721.7884273529,88064.0209026337 73719.926618576))");
        List<TroughLineWithSort> splitLine = ConcaveSplit.split(pg);
        List<Polygon> polygons = simpleCutPolygonWithLine(pg, splitLine);
        polygons.forEach(
                s -> System.out.println(s)
        );
    }
}
