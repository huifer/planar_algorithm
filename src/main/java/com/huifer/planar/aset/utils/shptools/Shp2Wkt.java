package com.huifer.planar.aset.utils.shptools;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * shp 2 wkt
 *
 * @author huifer
 * @date 2019年1月17日
 */
public class Shp2Wkt {

    public static void main(String[] args) {
        String path1 = "D:\\testdata\\2019年1月16日\\11111.shp";
        SimpleFeatureCollection colls1 = readShp(path1);
        SimpleFeatureIterator iters = colls1.features();
        while (iters.hasNext()) {
            SimpleFeature sf = iters.next();
            System.out.println("wkt: " + sf.getAttributes());
        }
    }

    public static SimpleFeatureCollection readShp(String path) {
        return readShp(path, null);

    }

    public static SimpleFeatureCollection readShp(String path, Filter filter) {
        SimpleFeatureSource featureSource = readStoreByShp(path);
        if (featureSource == null) {
            return null;
        }
        try {
            return filter != null ? featureSource.getFeatures(filter) : featureSource.getFeatures();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimpleFeatureSource readStoreByShp(String path) {
        File file = new File(path);
        FileDataStore store;
        SimpleFeatureSource featureSource = null;
        try {
            store = FileDataStoreFinder.getDataStore(file);
            ((ShapefileDataStore) store).setCharset(Charset.forName("UTF-8"));
            featureSource = store.getFeatureSource();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return featureSource;
    }
}

