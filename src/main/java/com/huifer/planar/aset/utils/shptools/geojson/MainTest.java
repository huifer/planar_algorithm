package com.huifer.planar.aset.utils.shptools.geojson;

import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONObject;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：geojson 测试
 * <p>
 * Date: 2018/10/3
 *
 * @author huifer
 */
public class MainTest {

    public static void main(String[] args) throws IOException {

//        toShp(new File(
//                "/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/geojson-test.json"));
//        shape2Geojson("/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/geo-test.shp",
//                "/Users/wangtao/IdeaProjects/tance/src/main/resources/shp/geo.json");

        Map map = shape2Geojson(
                "E:\\mck\\planar_algorithm\\src\\main\\resources\\data\\shp\\省范围.shp");

//        writeInFileByfb("E:\\mck\\planar_algorithm\\src\\main\\resources\\data\\shp\\省范围.geo.json",
//                map.delete("message").toString());
        toShp(new File("E:\\mck\\planar_algorithm\\src\\main\\resources\\data\\shp\\省范围.geo.json"));

    }

    public static void writeInFileByfb(String filePath, String content) {
        File f = new File(filePath);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            fw = new FileWriter(f.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Map shape2Geojson(String shpPath) {
        Map map = new HashMap();
        FeatureJSON fjson = new FeatureJSON();

        try {
            StringBuffer sb = new StringBuffer();
            sb.append("{\"type\": \"FeatureCollection\",\"features\": ");

            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;

            shpDataStore = new ShapefileDataStore(file.toURI().toURL());
            //设置编码
            Charset charset = Charset.forName("UTF-8");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();
            SimpleFeatureIterator itertor = result.features();
            JSONArray array = new JSONArray();
            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                StringWriter writer = new StringWriter();
                fjson.writeFeature(feature, writer);
                JSONObject json = new JSONObject(writer.toString());
                array.put(json);
            }
            itertor.close();
            sb.append(array.toString());
            sb.append("}");

            map.put("status", "success");
            map.put("message", sb.toString());
        } catch (Exception e) {
            map.put("status", "failure");
            map.put("message", e.getMessage());
            e.printStackTrace();

        }
        return map;
    }

    public static void toShp(File geojson) throws IOException {
        File shpFile = new File(
                "E:\\mck\\planar_algorithm\\src\\main\\resources\\data\\shp\\geo-test.shp");
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shpFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore shpDataStore = (ShapefileDataStore) dataStoreFactory
                .createNewDataStore(params);

        InputStream in = new FileInputStream(geojson);
        int decimals = 15;
        GeometryJSON gjson = new GeometryJSON(decimals);
        FeatureJSON fjson = new FeatureJSON(gjson);

        FeatureCollection fc = fjson.readFeatureCollection(in);

        SimpleFeatureType type = (SimpleFeatureType) fc.getSchema();
        shpDataStore.createSchema(type);

        Transaction transaction = new DefaultTransaction("create");

        String typeName = shpDataStore.getTypeNames()[0];

        SimpleFeatureSource featureSource = shpDataStore.getFeatureSource(typeName);

        if (featureSource instanceof FeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            featureStore.setTransaction(transaction);
            try {

                featureStore.addFeatures(fc);

                transaction.commit();

            } catch (Exception ex) {
                ex.printStackTrace();
                transaction.rollback();

            } finally {
                transaction.close();
            }
        } else {
//            System.out.println(typeName + " does not support read/write access");
        }


    }
}
