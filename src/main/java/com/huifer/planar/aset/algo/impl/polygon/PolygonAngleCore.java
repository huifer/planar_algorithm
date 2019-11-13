package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.PolygonAngleInterface;
import com.huifer.planar.aset.entity.PolygonAngleResult;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title : PolygonAngleCore </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-01-28
 */
public class PolygonAngleCore implements PolygonAngleInterface {

    /**
     * 计算夹角
     *
     * @param o 基准点 两条直线共有点
     * @param s 起点
     * @param e 终点
     * @return 角度
     */
    public static double angle(PointF o, PointF s,
                               PointF e) {
        double cosfi = 0, fi = 0, norm = 0;
        double dsx = s.x - o.x;
        double dsy = s.y - o.y;
        double dex = e.x - o.x;
        double dey = e.y - o.y;

        cosfi = dsx * dex + dsy * dey;
        norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);
        cosfi /= Math.sqrt(norm);

        if (cosfi >= 1.0) {
            return 0;
        }
        if (cosfi <= -1.0) {
            return Math.PI;
        }
        fi = Math.acos(cosfi);
        if (180 * fi / Math.PI < 180) {
            return 180 * fi / Math.PI;
        } else {
            return 360 - 180 * fi / Math.PI;
        }
    }

    /**
     * 节点 1，2 | 2，3  节点 2，3 | 3，4  节点 4，1 | 1,2 计算角度
     *
     * @param coordinates 节点坐标值
     * @return 角度列表
     */
    private static HashMap<PointF, Double> calcAngle(Coordinate[] coordinates) {
        List<Double> angles = new ArrayList<>();
        HashMap<PointF, Double> rs = new HashMap<>();
        for (int i = 0; i < coordinates.length; i++) {
            if (i + 2 < coordinates.length) {
                // 基准点
                PointF o = new PointF(coordinates[i + 1].x, coordinates[i + 1].y);
                // 起点
                PointF s = new PointF(coordinates[i].x, coordinates[i].y);
                // 终点
                PointF e = new PointF(coordinates[i + 2].x, coordinates[i + 2].y);
                double angle = angle(o, s, e);
                angles.add(angle);
                rs.put(o, angle);
            }
            if (i == coordinates.length - 1) {
                PointF o = new PointF(coordinates[0].x, coordinates[0].y);
                PointF s = new PointF(coordinates[coordinates.length - 2].x,
                        coordinates[coordinates.length - 2].y);
                PointF e = new PointF(coordinates[1].x, coordinates[1].y);
                double angle = angle(o, s, e);
                angles.add(angle);
                rs.put(o, angle);
            }
        }
        return rs;
    }

    /**
     * polygon计算角度核心
     *
     * @param wkt polygonWKT描述
     * @return {@link PolygonAngleResult}
     * @throws ParseException wkt格式化异常
     */
    private static PolygonAngleResult polygonWktAngleCore(String wkt)
            throws ParseException {
        Operation op = new Operation();
        GeometryFactory gFactory = new GeometryFactory();
        Polygon polygon = op.createPolygonByWKT(wkt);
        List<HashMap<PointF, Double>> polygonAngles = new ArrayList<>();
        List<LineString> ls = new ArrayList<>();
        PolygonAngleResult polygonAngleResult = new PolygonAngleResult();
        Geometry geometry = null;
        // 是否有环形
        if (polygon.getNumInteriorRing() == 0) {
            LineString exteriorRing = polygon.getExteriorRing();
            geometry = gFactory.createMultiLineString(new LineString[]{exteriorRing});
            Coordinate[] coordinates = geometry.getCoordinates();
            HashMap<PointF, Double> doubles = calcAngle(coordinates);
            polygonAngles.add(doubles);
            polygonAngleResult.setPolygon(polygon);
            polygonAngleResult.setPolygonAngles(polygonAngles);

        } else {
            geometry = polygon.getBoundary();
            // geometry 数量
            int dimension = geometry.getDimension();

            for (int i = 0; i <= dimension; i++) {
                Geometry line = geometry.getGeometryN(i);
                ls.add((LineString) line);
                Coordinate[] coordinates = line.getCoordinates();
                HashMap<PointF, Double> doubles = calcAngle(coordinates);
                polygonAngles.add(doubles);
            }

            polygonAngleResult.setPolygon(polygon);
            polygonAngleResult.setPolygonAngles(polygonAngles);
        }

        return polygonAngleResult;
    }

    @Override
    public PolygonAngleResult polygonWktAngle(String wkt) throws ParseException {
        PolygonAngleResult polygonAngleResult = polygonWktAngleCore(wkt);
        return polygonAngleResult;
    }

    @Override
    public PolygonAngleResult polygonWktAngleMerge(String wkt, Double angle) throws ParseException {

        PolygonAngleResult polygonAngleResult = polygonWktAngle(wkt);
        polygonAngleResult.setRemoveAngle(angle);

        Polygon oldPolygon = polygonAngleResult.getPolygon();
        Coordinate[] coordinates = oldPolygon.getCoordinates();
        List<HashMap<PointF, Double>> polygonAngles = polygonAngleResult.getPolygonAngles();

        List<Coordinate> collect = Arrays.stream(coordinates).collect(Collectors.toList());

        for (int i = 0; i < polygonAngles.size(); i++) {
            polygonAngles.get(i).forEach(
                    (k, v) -> {
                        for (int j = 0; j < collect.size(); j++) {
                            Coordinate coordinate = new Coordinate(k.x, k.y);
                            if (collect.get(j).equals(coordinate) && (v < 30 || 180 - v < 30)) {

                                collect.remove(coordinate);
                            }
                        }
                    }
            );
        }


        Polygon polygon = new GeometryFactory().createPolygon(createCoordinate(collect));
        polygonAngleResult.setNewPolygon(polygon);


        return polygonAngleResult;
    }

    /**
     * 根据 List<Coordinate> collect创建 collect []
     *
     * @param collect List<Coordinate> collect
     * @return Coordinate
     */
    private Coordinate[] createCoordinate(List<Coordinate> collect) {
        Coordinate[] coordinates = new Coordinate[collect.size()];

        for (int i = 0; i < collect.size(); i++) {
            coordinates[i] = collect.get(i);
        }
        return coordinates;
    }

    /**
     * 计算两条直线之间夹角的点类
     */
    @EqualsAndHashCode
    @Data
    public static class PointF {

        double x;
        double y;

        public PointF() {
        }

        public PointF(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"x\":")
                    .append(x);
            sb.append(",\"y\":")
                    .append(y);
            sb.append('}');
            return sb.toString();
        }

    }


}
