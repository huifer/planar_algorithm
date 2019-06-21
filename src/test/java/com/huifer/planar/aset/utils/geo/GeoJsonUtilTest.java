package com.huifer.planar.aset.utils.geo;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

@Slf4j
public class GeoJsonUtilTest {


    @Test
    public void jsonTgeojson() throws IOException {
        String geoJsonPath = "F:\\desktop\\sheng\\sf.geojson";
        SimpleFeatureCollection collection = GeoJsonUtil.readGeoJsonByFeatureJSON(geoJsonPath);
        SimpleFeatureIterator features = collection.features();
        while (features.hasNext()) {
            SimpleFeature next = features.next();
            SimpleFeatureType featureType = next.getFeatureType();
//            System.out.println(next.getAttributes());
            log.info("geojson data = {}", next.getAttributes());
        }
//        System.out.println(collection);

        //language=JSON
        String json = "[\n"
                + "  {\n"
                + "    \"id\": 1,\n"
                + "    \"x\": 1,\n"
                + "    \"y\": 1\n"
                + "  },  {\n"
                + "  \"id\": 2,\n"
                + "  \"x\": 2,\n"
                + "  \"y\": 2\n"
                + "}\n"
                + "]";
        String s = GeoJsonUtil.jsonToGeoJson(json, "x", "y", 1);
//        System.out.println(s);

    }
}
