package com.huifer.planar.aset.algo.impl.minrect;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : Rotation </p>
 * <p>Description : 旋转</p>
 *
 * @author huifer
 * @date 2019-02-22
 */
public class Rotation {


    /**
     * 旋转
     *
     * @param geomCollection 旋转集合
     * @param center 旋转中心
     * @param angel 旋转角度
     * @param gf 构造器
     * @return 旋转结果
     */
    public static GeometryCollection get(GeometryCollection geomCollection, Coordinate center,
            double angel, GeometryFactory gf) {
        Geometry[] gs = new Geometry[geomCollection.getNumGeometries()];
        for (int i = 0; i < geomCollection.getNumGeometries(); i++) {
            gs[i] = get(geomCollection.getGeometryN(i), center, angel, gf);
        }
        return gf.createGeometryCollection(gs);
    }

    /**
     * 旋转
     *
     * @param geom geometry
     * @param center 旋转中心
     * @param angle 旋转角度
     * @param gf 构造器
     * @return 旋转结果
     */
    public static Geometry get(Geometry geom, Coordinate center, double angle, GeometryFactory gf) {
        if (geom instanceof Point) {
            return get((Point) geom, center, angle, gf);
        } else if (geom instanceof Polygon) {
            return get((Polygon) geom, center, angle, gf);
        } else if (geom instanceof LineString) {
            return get((LineString) geom, center, angle, gf);
        } else if (geom instanceof LinearRing) {
            return get((LinearRing) geom, center, angle, gf);
        }
        return null;
    }

    /**
     * x旋转
     *
     * @param geom 旋转面
     * @param center 旋转中心
     * @param angle 旋转角度
     * @param gf 构造器
     * @return 旋转结果
     */
    public static Polygon get(Polygon geom, Coordinate center, double angle, GeometryFactory gf) {
        LinearRing linearRing = get((LinearRing) geom.getExteriorRing(), center, angle, gf);
        LinearRing[] linearRings = new LinearRing[geom.getNumInteriorRing()];
        for (int j = 0; j < geom.getNumInteriorRing(); j++) {
            linearRings[j] = get((LinearRing) geom.getInteriorRingN(j), center, angle, gf);
        }
        return gf.createPolygon(linearRing, linearRings);
    }

    /**
     * 旋转
     *
     * @param linearRing 旋转环
     * @param center 旋转中心
     * @param angle 旋转角度
     * @param gf 构造器
     * @return 旋转后结果
     */
    public static LinearRing get(LinearRing linearRing, Coordinate center, double angle,
            GeometryFactory gf) {
        return gf.createLinearRing(get(linearRing.getCoordinates(), center, angle));
    }


    /**
     * 旋转
     *
     * @param line 旋转线
     * @param center 旋转中心
     * @param angle 旋转角度
     * @param gf 构造器
     * @return 旋转后结果
     */
    public static LineString get(LineString line, Coordinate center, double angle,
            GeometryFactory gf) {
        return gf.createLineString(get(line.getCoordinates(), center, angle));
    }


    /**
     * 旋转
     *
     * @param point 点
     * @param center 旋转中心
     * @param angle 角度
     * @param gf 构造器
     * @return 旋转后结果
     */
    public static Point get(Point point, Coordinate center, double angle, GeometryFactory gf) {
        return gf.createPoint(get(point.getCoordinate(), center, angle));
    }


    /**
     * 旋转
     *
     * @param coord 旋转坐标
     * @param center 旋转中心
     * @param angle 角度
     * @return 旋转后结果
     */
    public static Coordinate[] get(Coordinate[] coord, Coordinate center, double angle) {
        Coordinate[] newCoord = new Coordinate[coord.length];
        double cos = Math.cos(angle), sin = Math.sin(angle);
        double xc = center.x, yc = center.y;
        Coordinate ci;
        double x, y;
        for (int i = 0; i < coord.length; i++) {
            ci = coord[i];
            x = ci.x;
            y = ci.y;
            newCoord[i] = new Coordinate(xc + cos * (x - xc) - sin * (y - yc),
                    yc + sin * (x - xc) + cos * (y - yc));
        }
        return newCoord;
    }


    /**
     * 旋转点
     *
     * @param point 被旋转的点
     * @param center 旋转中心
     * @param angle 角度
     * @return 旋转后坐标
     */
    public static Coordinate get(Coordinate point, Coordinate center, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = point.x;
        double y = point.y;
        double centerX = center.x;
        double centerY = center.y;
        return new Coordinate(centerX + cos * (x - centerX) - sin * (y - centerY),
                centerY + sin * (x - centerX) + cos * (y - centerY));
    }

}
