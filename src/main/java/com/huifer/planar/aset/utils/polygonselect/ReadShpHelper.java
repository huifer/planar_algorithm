package com.huifer.planar.aset.utils.polygonselect;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * <p>Title : ReadShpHelper </p>
 * <p>Description : 读取Shp工具</p>
 *
 * @author huifer
 * @date 2019-04-26
 */
public class ReadShpHelper {

    public static void main(String[] args) throws Exception {
        List<TocePolygon> gc = gc("E:\\mck\\point-verify\\src\\main\\resources\\shp\\afafaf.shp",
                "utf-8");

        List<TocePoint> tocePointList1 = polygon2point(gc);

        System.out.println();
    }


    /**
     * 这个面就是简单面没有空洞
     */
    private static List<LineString> polygonLineString(MultiPolygon pg) {

        List<LineString> result = new ArrayList<>();
        for (int i = 0; i < pg.getCoordinates().length - 1; i++) {
            result.add(
                    new GeometryFactory().createLineString(
                            new Coordinate[]{
                                    new Coordinate(pg.getCoordinates()[i].x,
                                            pg.getCoordinates()[i].y),
                                    new Coordinate(pg.getCoordinates()[i + 1].x,
                                            pg.getCoordinates()[i + 1].y)
                            }
                    )
            );
        }
        return result;
    }

    public static List<ToceLineString> polygon2Line(List<TocePolygon> gc) {
        List<ToceLineString> rs = new ArrayList<>();

        gc.forEach(
                s -> {
                    Geometry geom = s.getGeom();
                    String polygonID = s.getId();

                    ToceLineString tp = new ToceLineString(polygonID);
                    List<LineString> lineStrings = polygonLineString((MultiPolygon) geom);
                    tp.setLineStringList(lineStrings);
                    rs.add(tp);
                }
        );
        return rs;
    }

    public static List<TocePoint> polygon2point(List<TocePolygon> gc) {
        List<TocePoint> tocePointList = new ArrayList<>();

        gc.forEach(
                s -> {
                    Geometry geom = s.getGeom();
                    String polygonID = s.getId();
                    Coordinate[] coords = geom.getCoordinates();

                    TocePoint tp = new TocePoint(polygonID);
                    List<Point> plist = new ArrayList<>();
                    for (Coordinate coord : coords) {
                        Point point = new GeometryFactory().createPoint(coord);
                        plist.add(point);
                    }
                    tp.setPointList(plist);
                    tocePointList.add(tp);
                }
        );
        return tocePointList;
    }

    public static List<TocePolygon> gc(String shpPath, String encode) {
        try {

            List<Map<String, Object>> maps = null;
            maps = readShpTools(shpPath, encode);
            List<TocePolygon> rs = new ArrayList<>();
            maps.forEach(s -> {
                Object id = s.get("id");
                Object wkt = s.get("wkt");
                Geometry read = null;
                try {
                    read = new WKTReader().read(String.valueOf(wkt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TocePolygon t = new TocePolygon(String.valueOf(id), read);
                rs.add(t);
            });
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
                field = field.equals("the_geom") ? "wkt" : field;
                data.put(field, value);
            }
            list.add(data);
        }
        return list;
    }


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
