package com.huifer.planar.aset.utils.shptools.center;

import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * <p>Title : ThiessenPolygon </p>
 * <p>Description : 泰森多边形</p>
 *
 * @author huifer
 * @date 2018/10/11
 */
public class ThiessenPolygon {
    static ThiessenPolygon tsdbx = new ThiessenPolygon();
    Operation op = new Operation();

    public void voronoiTest()throws Exception {
        VoronoiDiagramBuilder voronoiDiagramBuilder = new VoronoiDiagramBuilder();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        Envelope clipEnvelpoe = new Envelope();

        List<Geometry> geomsPoints = new ArrayList<Geometry>();


        String pointpath = "D:\\testdata\\2019年1月14日\\1.shp";
        ShapefileDataStore shpDataStore = null;
        File file = new File(pointpath);
        shpDataStore = new ShapefileDataStore(file.toURI().toURL());
        Charset charset = Charset.forName("UTF-8");
        shpDataStore.setCharset(charset);
        String typeName = shpDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = null;
        featureSource = shpDataStore.getFeatureSource(typeName);
        SimpleFeatureCollection result = featureSource.getFeatures();
        SimpleFeatureIterator itertor = result.features();

        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            Collection<Property> p = feature.getProperties();
            Iterator<Property> it = p.iterator();
            while (it.hasNext()) {
                Property pro = it.next();
                String field = pro.getName().toString();
                String value = pro.getValue().toString();
                if ("the_geom".equals(field)) {
                    Point pointByWkt = op.createPointByWkt(value);
                    Coordinate coordinate = pointByWkt.getCoordinate();
                    coords.add(coordinate);
                    clipEnvelpoe.expandToInclude(coordinate);
                    geomsPoints.add(new GeometryFactory().createPoint(coordinate));
                }
            }
        }

        // 泰森多边形
        voronoiDiagramBuilder.setSites(coords);
        voronoiDiagramBuilder.setClipEnvelope(clipEnvelpoe);
        Geometry geom = voronoiDiagramBuilder.getDiagram(JTSFactoryFinder.getGeometryFactory());
        List<Geometry> geoms = new ArrayList<Geometry>();
        for (int i = 0; i < geom.getNumGeometries(); i++) {
            geoms.add(geom.getGeometryN(i));
        }

        String polygonpath = "D:\\testdata\\2019年1月14日\\point___ttt.shp";
        tsdbx.writeShape(polygonpath, "Polygon", geoms);
    }

    /**
     * @param filepath
     * @param geoType
     * @param geoms
     */
    public void writeShape(String filepath, String geoType, List<Geometry> geoms) {
        try {
            //创建shape文件对象
            File file = new File(filepath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
//            tb.setCRS(DefaultGeographicCRS.WGS84);
            tb.setName("shapefile");
            if (geoType == "Point") {
                tb.add("the_geom", Point.class);
            } else {
                tb.add("the_geom", Polygon.class);
            }

            ds.createSchema(tb.buildFeatureType());
            //设置编码
            Charset charset = Charset.forName("UTF-8");
            ds.setCharset(charset);
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            for (int i = 0, len = geoms.size(); i < len; i++) {
                //写下一条
                SimpleFeature feature = writer.next();
                Geometry geom = geoms.get(i);
                feature.setAttribute("the_geom", geom);
            }
            writer.write();
            writer.close();
            ds.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)throws Exception {
        long start = System.currentTimeMillis();
        tsdbx.voronoiTest();
//        System.out.println("共耗时" + (System.currentTimeMillis() - start) + "ms");
    }
}
