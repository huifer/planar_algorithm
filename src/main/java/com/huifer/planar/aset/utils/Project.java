package com.huifer.planar.aset.utils;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.geom.util.AffineTransformationBuilder;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * <p>Title : Project </p>
 * <p>Description : 投影</p>
 *
 * @author huifer
 * @date 2019-02-27
 */
public class Project {

    /**
     * 仿射转换
     * @param lon  经度
     * @param lat 纬度
     */
    public static void affine(double lon, double lat) {
        Coordinate st1 = new Coordinate();
        Coordinate st2 = new Coordinate();
        Coordinate st3 = new Coordinate();

        Coordinate end1 = new Coordinate();
        Coordinate end2 = new Coordinate();
        Coordinate end3 = new Coordinate();
        AffineTransformationBuilder affineTransformationBuilder = new AffineTransformationBuilder(
                st1, st2, st3, end1, end2, end3);
        AffineTransformation afm = affineTransformationBuilder.getTransformation();

        Point sourcePoint = new GeometryFactory().createPoint(new Coordinate(lat, lon));
        sourcePoint.apply(afm);

    }

    /***
     * 投影转换
     * @param lon 经度
     * @param lat 纬度
     * @param epsgSource EPSG:xxx 输入坐标系
     * @param epsgTarget EPSG:xxx 输出坐标系
     * @return
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public static Point projectTransform(double lon, double lat,
            String epsgSource, String epsgTarget) throws FactoryException,
            MismatchedDimensionException, TransformException {

        Point sourcePoint = new GeometryFactory().createPoint(new Coordinate(lat, lon));

        CoordinateReferenceSystem crsSource = CRS.decode(epsgSource);
        CoordinateReferenceSystem crsTarget = CRS.decode(epsgTarget);
        MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
        Point pointTarget = (Point) JTS.transform(sourcePoint, transform);

        return pointTarget;
    }
}
