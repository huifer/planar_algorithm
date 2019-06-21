package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.KmeanPolygonSplitInterface;
import com.huifer.planar.aset.algo.VoronoiInterface;
import com.huifer.planar.aset.entity.KmeanPolygonResult;
import com.huifer.planar.aset.entity.Kmeans;
import com.huifer.planar.aset.utils.shptools.ShpUtils;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/**
 * <p>Title : KmeanPolygonSplitCore </p>
 * <p>Description : Kmean split polygon core </p>
 *
 * @author huifer
 * @date 2019-01-16
 */
@Slf4j
public class KmeanPolygonSplitCore implements KmeanPolygonSplitInterface {

    @Override
    public Object splitPolygonWithShp(String path, String splitFiled) throws Exception {
        List<Map<String, Object>> shpData =
                ShpUtils.readShpTools(path, "UTF-8");

        List<Dt> dtList = new ArrayList<>();
        for (int i = 0; i < shpData.size(); i++) {
            Map<String, Object> rowData = shpData.get(i);
            Integer splitSize;

            try {
                splitSize = Integer.valueOf(rowData.get(splitFiled).toString());
            } catch (Exception e) {
                splitSize = 0;
            }
            Object wkt = rowData.get("wkt");


            Dt dt = null;
            if (splitSize <= 1) {
                // 小于等于1 的不用操作直接返回数据
                rowData.remove("wkt");
                dt = new Dt();
                dt.setRowData(rowData);
                dt.setGeometry(new WKTReader().read(wkt.toString()));
                dtList.add(dt);
            } else {
                // 操作



                KmeanPolygonResult kmeanPolygonResult = splitPolygon(wkt.toString(), 100, splitSize);
                List<Geometry> geometryList = kmeanPolygonResult.getGeometryList();

//
                for (int i1 = 0; i1 < geometryList.size(); i1++) {
                    Geometry geometry = geometryList.get(i1);
                    rowData.remove("wkt");
                    dt = new Dt();
                    dt.setRowData(rowData);
                    dt.setGeometry(geometry);
                    dtList.add(dt);

//                    System.out.println(wkt.toString() +"\t" + geometry);
                    log.info("{}\t{}", wkt, geometry);

                }
            }


        }
        //TODO: 输出到文件
//        dtList.forEach(s -> {
//
//            System.out.println(s.getGeometry().toString() +"\t"+ s.getRowData());
//        });

        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class Dt {
        private Map<String, Object> rowData;
        private Geometry geometry;
    }

    private static double random(double max, double min) {
        double d = (Math.random() * (max - min) + min);
        return d;
    }

    @Override
    public KmeanPolygonResult splitPolygon(String wkt, int setp, int k) throws ParseException {
        Operation op = new Operation();
        KmeanPolygonResult result = new KmeanPolygonResult();
//        Polygon polygon = op.createPolygonByWKT(wkt);
        MultiPolygon polygon = op.createMulPolygonByWKT(wkt);
        Coordinate[] coordinates = polygon.getCoordinates();
        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();

        Arrays.stream(coordinates).forEach(
                s -> {
                    xList.add(s.x);
                    yList.add(s.y);
                }
        );
        // xy 最大最小值
        Double xMax = xList.stream().reduce(Double::max).get();
        Double xMin = xList.stream().reduce(Double::min).get();
        Double yMax = yList.stream().reduce(Double::max).get();
        Double yMin = yList.stream().reduce(Double::min).get();

        // 当前点数量
        int pointCount = 0;
        ArrayList<Point> pointArrayList = new ArrayList<>();

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 最大最小值随机
            if (pointCount <= setp) {
                double rx = random(xMax, xMin);
                double ry = random(yMax, yMin);
                Point nowPoint = op.createPointByWkt("POINT(" + rx + " " + ry + ")");
                boolean contains = polygon.contains(nowPoint);
                if (contains) {
                    pointArrayList.add(nowPoint);
                    pointCount++;
                }
            } else {
                break;
            }
        }
        // k-means 数据 构造
        double[][] kmData = new double[pointArrayList.size()][2];
        for (int i = 0; i < pointArrayList.size(); i++) {
            Point point = pointArrayList.get(i);
            double[] oneData = new double[2];
            oneData[0] = point.getX();
            oneData[1] = point.getY();
            kmData[i] = oneData;
        }

        // k-means 结果

        Kmeans kmeans = new Kmeans(kmData, k);

        // 构造泰森多边形
        VoronoiInterface vo = new VoronoiInterfaceImpl();
        List<Geometry> voronoi = vo.voronoi(kmeans.getCentroids());

        List<Geometry> geometryList = new ArrayList<>();
        voronoi.forEach(
                s -> {
                    Geometry intersection = polygon.intersection(s);
                    geometryList.add(intersection);
                }
        );

        result.setPolygon(polygon);
        result.setPointList(pointArrayList);
        result.setAssignments(kmeans.getAssignments());
        result.setCentroids(kmeans.getCentroids());
        result.setXlist(xList);
        result.setYlist(yList);
        result.setVoronoi(voronoi);
        result.setGeometryList(geometryList);
        return result;
    }

}
