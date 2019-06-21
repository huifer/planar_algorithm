package com.huifer.planar.aset.utils.geo;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.Style;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * <p>Title : GeoMapContentUtil </p>
 * <p>Description : 地图框</p>
 *
 * @author huifer
 * @date 2019-01-23
 */
public class GeoMapContentUtil {


    /**
     * 追加layers
     *
     * @param content mapContent
     * @param features 要素
     * @param style 样式
     */
    public static void addLayer(MapContent content, SimpleFeatureCollection features, Style style) {
        Layer layer = new FeatureLayer(features, style);
        content.addLayer(layer);
    }

    /**
     * 获取地图实例
     */
    public static MapContent getMapContent(String title) {

        MapContent mapContent = new MapContent();
        mapContent.setTitle(title);
        return mapContent;
    }

    public static Layer getFlickrLayer() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("MyFeatureType");
        // set crs
        builder.setCRS(null);
        // add geometry
        builder.add("location", Point.class);

        // build the type
        SimpleFeatureType type = builder.buildFeatureType();

        // create features using the type defined
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        org.locationtech.jts.geom.Point point = geometryFactory.createPoint(new Coordinate(5,5));
        featureBuilder.add(point);
        SimpleFeature feature = featureBuilder.buildFeature("FeaturePoint");
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection("internal", type);
        featureCollection.add(feature); // Add feature 1, 2, 3, etc

        Style style = GeoStyleUtil.pointStyle(null, "0xffffff", "30000", "0xffffff", "1", 5000);
        Layer layer = new FeatureLayer(featureCollection, style);
//        layer.setTitle("NewPointLayer");
        return layer;
    }

}
