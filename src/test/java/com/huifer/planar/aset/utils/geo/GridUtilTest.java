package com.huifer.planar.aset.utils.geo;

import com.huifer.planar.aset.entity.FourBox;
import com.huifer.planar.aset.utils.TmeanLength;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;

@RunWith(Enclosed.class)
@Slf4j
public class GridUtilTest {

    public static class TestCreateGrids {

        @Test
        public void createGrid() throws Exception {
            MapContent map = new MapContent();
            map.setTitle("Quickstart");

            Polygon polygon = (Polygon) new WKTReader()
                    .read("POLYGON ((0 0, 10 0, 10 10, 0 10,  0 0))");
            // 单个面的四至
            FourBox fourBox = TmeanLength.calcSpaceBetween(
                    Arrays.stream(polygon.getCoordinates()).collect(Collectors.toList()));
            // 输出边框范围
            double outLeftX = fourBox.getLeftX() - 1;
            double outLeftY = fourBox.getLeftX() - 1;
            double outRightX = fourBox.getLeftX() + 1;
            double outRightY = fourBox.getLeftX() + 1;

            ReferencedEnvelope mapArea = new ReferencedEnvelope(outLeftX, outRightX,
                    outLeftY, outRightY, null);
            ReferencedEnvelope gridArea = new ReferencedEnvelope(outLeftX - 0.001,
                    outRightX - 0.001,
                    outLeftY + 0.001, outRightY + 0.001, null);

            @SuppressWarnings("rawtypes")
            SimpleFeatureSource simpleFeatureByCreateGrids = GridUtil
                    .getSimpleFeatureByCreateGrids(2, 4, mapArea);

            SimpleFeatureCollection features = simpleFeatureByCreateGrids.getFeatures();
            SimpleFeatureIterator iterator = features.features();

            // 输出每个grid坐标
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
//                System.out.println(  feature.getDefaultGeometry());
                log.info("{}", feature.getDefaultGeometry());
            }

            Style style = GeoStyleUtil.getPolygonStyle(null, "0xff0000", "4",
                    "0xffffff", "0");

            Layer flickrLayer = GeoMapContentUtil.getFlickrLayer();
            Layer layer = new FeatureLayer(features, style);

            map.addLayer(layer);
            map.addLayer(flickrLayer);
            JMapFrame.showMap(map);
        }

    }

}
