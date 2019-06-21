package com.huifer.planar.aset.utils.shptools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;


/**
 * 描述: shp读取
 *
 * @author huifer
 * @date 2019-02-09
 */
public class ShpUtils {

    private static String THE_GEOM = "the_geom";
    /***
     *  读取SHP文件
     * @param shpPath 数据
     * @param encode 编码格式
     * @throws IOException
     */
    public static List<Map<String, Object>> readShpTools(String shpPath, String encode)
            throws IOException {
        SimpleFeatureIteratorMethod simpleFeatureIteratorMethod = new SimpleFeatureIteratorMethod(
                shpPath, encode).invoke();
        List<Map<String, Object>> list = simpleFeatureIteratorMethod.getList();
        SimpleFeatureIterator itertor = simpleFeatureIteratorMethod.getItertor();
        while (itertor.hasNext()) {
            Map<String, Object> data = new HashMap<String, Object>();
            SimpleFeature feature = itertor.next();
            Collection<Property> p = feature.getProperties();
            Iterator<Property> it = p.iterator();
            while (it.hasNext()) {
                Property pro = it.next();
                String field = pro.getName().toString().toLowerCase();
                String value = pro.getValue().toString();
                field = field.equals(THE_GEOM) ? "wkt" : field;
                data.put(field, value);
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 获取shp文件类型
     */
    public static void shpFileType(String shpPath, String encode) throws IOException {
        SimpleFeatureIteratorMethod simpleFeatureIteratorMethod = new SimpleFeatureIteratorMethod(
                shpPath, encode).invoke();
        SimpleFeatureIterator itertor = simpleFeatureIteratorMethod.getItertor();
        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            Collection<Property> p = feature.getProperties();
            Iterator<Property> it = p.iterator();
            while (it.hasNext()) {
                Property property = it.next();
                if (property.getValue() instanceof Point) {
                    System.out.println("=========point=========");
                    return;
                } else if (property.getValue() instanceof Polygon) {
                    System.out.println("=========Polygon=========");
                    return;
                } else if (property.getValue() instanceof LineString) {
                    System.out.println("=========LineString=========");
                    return;
                } else {
                    System.out.println("其他类型");
                    return;
                }

            }
        }

    }

    /**
     * SimpleFeatureIteratorMethod shp SimpleFeatureIterator geotools返回
     */
    private static class SimpleFeatureIteratorMethod {

        private String shpPath;
        private String encode;
        private List<Map<String, Object>> list;
        private SimpleFeatureIterator itertor;

        public SimpleFeatureIteratorMethod(String shpPath, String encode) {
            this.shpPath = shpPath;
            this.encode = encode;
        }

        public List<Map<String, Object>> getList() {
            return list;
        }

        public SimpleFeatureIterator getItertor() {
            return itertor;
        }

        public SimpleFeatureIteratorMethod invoke() throws IOException {
            ShapefileDataStore shpDataStore = null;
            Calendar startTime = Calendar.getInstance();
            list = new ArrayList<Map<String, Object>>();
            File file = new File(shpPath);
            shpDataStore = new ShapefileDataStore(file.toURI().toURL());
            //设置字符编码
            Charset charset = Charset.forName(encode);
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            itertor = result.features();
            return this;
        }
    }


}
