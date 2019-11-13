package com.huifer.planar.aset.algo.impl.minrect;

import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : MinimumBoundingRectangle </p>
 * <p>Description : 最小外包矩形</p>
 *
 * @author huifer
 * @date 2019-02-22
 */
public class MinimumBoundingRectangle {

    /**
     * 最小外接矩形的获取
     *
     * @param geom 需要计算的面
     * @param gf   构造器
     * @return 最小外接矩形结果
     */
    public static Polygon get(Geometry geom, GeometryFactory gf) {
        Geometry hull = (new ConvexHull(geom)).getConvexHull();
        if (!(hull instanceof Polygon)) {
            return null;
        }
        Polygon convexHull = (Polygon) hull;
//        System.out.println(convexHull);

        // 直接使用中心值
        Coordinate c = geom.getCentroid().getCoordinate();
//        System.out.println("==============旋转基点==============");
//        System.out.println(new GeometryFactory().createPoint(c));
//        System.out.println("==============旋转基点==============");
        Coordinate[] coords = convexHull.getExteriorRing().getCoordinates();

        double minArea = Double.MAX_VALUE;
        double minAngle = 0;
        Polygon ssr = null;
        Coordinate ci = coords[0];
        Coordinate cii;
        for (int i = 0; i < coords.length - 1; i++) {
            cii = coords[i + 1];
            double angle = Math.atan2(cii.y - ci.y, cii.x - ci.x);
            Polygon rect = (Polygon) Rotation.get(convexHull, c, -1 * angle, gf).getEnvelope();
            double area = rect.getArea();
//            此处可以将 rotationPolygon 放到list中求最小值
//            Polygon rotationPolygon = Rotation.delete(rect, c, angle, gf);
//            System.out.println(rotationPolygon);
            if (area < minArea) {
                minArea = area;
                ssr = rect;
                minAngle = angle;
            }
            ci = cii;
        }

        return Rotation.get(ssr, c, minAngle, gf);
    }


}
