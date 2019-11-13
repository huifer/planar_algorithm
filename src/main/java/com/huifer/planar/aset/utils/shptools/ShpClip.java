package com.huifer.planar.aset.utils.shptools;


import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述： 两个面相切
 * Date: 2018/10/2
 *
 * @author huifer
 */
public class ShpClip {

    public static void main(String[] args) {
        double start = System.currentTimeMillis();

        String inputPath2 = "/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/切割层.shp";
        String inputPath1 = "/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/整面.shp";
        String outputPath = "/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/province_test.shp";

        try {
            File inputFile1 = new File(inputPath1);
            File inputFile2 = new File(inputPath2);

            ShapefileDataStore shpDataStore1 = new ShapefileDataStore(inputFile1.toURI().toURL());
            ShapefileDataStore shpDataStore2 = new ShapefileDataStore(inputFile2.toURI().toURL());

            //属性编码
            Charset charset = Charset.forName("UTF8");

            shpDataStore1.setCharset(charset);
            shpDataStore2.setCharset(charset);

            String typeName1 = shpDataStore1.getTypeNames()[0];
            String typeName2 = shpDataStore2.getTypeNames()[0];

            SimpleFeatureSource featureSource1 = shpDataStore1.getFeatureSource(typeName1);
            SimpleFeatureSource featureSource2 = shpDataStore2.getFeatureSource(typeName2);

            SimpleFeatureCollection featureCollection1 = featureSource1.getFeatures();
            SimpleFeatureCollection featureCollection2 = featureSource2.getFeatures();

            /**
             * mapFields记录的是两个图层的属性名称，
             *          在处理第二个图层的时候，如果已经有了这个名称，
             *          会在字段后面加‘_1’予以区分
             * fields1为图层1的字段
             * fields2为图层2的字段
             */
            Map<String, Class> mapFields = new HashMap();
            List<Map> fields1 = new ArrayList(),
                    fields2 = new ArrayList();

            SimpleFeatureType featureType1 = featureCollection1.getSchema();
            List<AttributeDescriptor> attrList1 = featureType1.getAttributeDescriptors();
            for (int i = 0; i < attrList1.size(); i++) {
                AttributeDescriptor attr = attrList1.get(i);
                String name = attr.getName().toString();
                Class type = attr.getType().getBinding();
                if (name != "the_geom") {
                    mapFields.put(name, type);
                    Map map = new HashMap();
                    map.put("fieldShp", name);
                    map.put("fieldNew", name);
                    fields1.add(map);
                }
            }
            SimpleFeatureType featureType2 = featureCollection2.getSchema();
            List<AttributeDescriptor> attrList2 = featureType2.getAttributeDescriptors();
            for (int j = 0; j < attrList2.size(); j++) {
                AttributeDescriptor attr = attrList1.get(j);
                String name = attr.getName().toString();
                Class type = attr.getType().getBinding();
                if (name != "the_geom") {
                    String name1 = name;
                    if (mapFields.containsKey(name)) {
                        name1 = name1 + "_1";
                    }
                    mapFields.put(name1, type);
                    Map map = new HashMap();
                    map.put("fieldShp", name);
                    map.put("fieldNew", name1);
                    fields2.add(map);
                }
            }

            SimpleFeatureIterator itertor1 = featureCollection1.features();

            //创建输出文件
            File outputFile = new File(outputPath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, outputFile.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
                    .createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            tb.add("the_geom", MultiPolygon.class);
            for (String key : mapFields.keySet()) {
                tb.add(key, mapFields.get(key));
            }
            ds.createSchema(tb.buildFeatureType());
            //设置编码
            ds.setCharset(charset);
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
                    .getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //记录已经参与过计算的数据
            Map hasDone = new HashMap();
            //开始计算
            while (itertor1.hasNext()) {
                SimpleFeature feature1 = itertor1.next();
                Geometry geom1 = (Geometry) feature1.getDefaultGeometry();
                String id1 = feature1.getID();

                SimpleFeatureIterator itertor2 = featureCollection2.features();
                while (itertor2.hasNext()) {
                    SimpleFeature feature2 = itertor2.next();
                    Geometry geom2 = (Geometry) feature2.getDefaultGeometry();
                    String id2 = feature2.getID();
                    //判断是否已经参与了计算，需要考虑1∩2和2∩1两种情况
                    boolean isDone1 = hasDone.containsKey(id1 + "-" + id2),
                            isDone2 = hasDone.containsKey(id2 + "-" + id1),
                            isIntersect = geom1.intersects(geom2);
                    if (!isDone1 && !isDone2 && isIntersect) {
                        Geometry geomOut = geom1.intersection(geom2);
                        SimpleFeature featureOut = writer.next();
                        featureOut.setAttribute("the_geom", geomOut);
                        for (int i = 0; i < fields1.size(); i++) {
                            Map map = fields1.get(i);
                            String fieldShp = map.get("fieldShp").toString(),
                                    fieldNew = map.get("fieldNew").toString();
                            featureOut.setAttribute(fieldNew, feature1.getAttribute(fieldShp));
                        }
                        for (int i = 0; i < fields2.size(); i++) {
                            Map map = fields2.get(i);
                            String fieldShp = map.get("fieldShp").toString(),
                                    fieldNew = map.get("fieldNew").toString();
                            featureOut.setAttribute(fieldNew, feature2.getAttribute(fieldShp));
                        }
                        writer.write();
                    }
                    hasDone.put(id1 + "-" + id2, true);
                    hasDone.put(id2 + "-" + id1, true);
                }
                itertor2.close();
            }

            writer.close();
            ds.dispose();

            itertor1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double end = System.currentTimeMillis();
        System.out.println("共耗时" + (end - start) + "MS");
    }

}
