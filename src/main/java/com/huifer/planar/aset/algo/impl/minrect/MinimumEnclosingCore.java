package com.huifer.planar.aset.algo.impl.minrect;

import com.huifer.planar.aset.algo.MinimumEnclosingInterface;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : MinimumEnclosingCore </p>
 * <p>Description : 最小外接矩形实现</p>
 *
 * @author huifer
 * @date 2019-02-25
 */
public class MinimumEnclosingCore implements MinimumEnclosingInterface {


    /**
     * 最小外接矩形
     *
     * @param polygon 需要计算的面
     * @return 最小外接矩形结果
     */
    @Override
    public Polygon getMinRect(Geometry polygon) {
        GeometryFactory gf = new GeometryFactory();
        Polygon result = MinimumBoundingRectangle.get(polygon, gf);
        return result;
    }

    /**
     * 最大内接圆
     *
     * @param pg 面
     * @return 最大外接圆圆心
     */
    @Override
    public Point getInCirclePoint(Polygon pg) {
        return MaximumInnerCircle.inCirclePoint(pg, 0.0001);
    }

    /**
     * 最大内接圆
     *
     * @param pg 面
     * @return 最大外接圆
     */
    @Override
    public Polygon getInCirclePolygon(Polygon pg) {
        return MaximumInnerCircle.inCirclePolygon(pg, 0.0001);
    }

    /**
     * 最小外接圆
     *
     * @param pg 面
     * @return 最小外接圆
     */
    @Override
    public Polygon getMinimumCircumscribedCircle(Polygon pg) {
        Coordinate[] cs = pg.getCoordinates();
        List<Point> p = new ArrayList<>();
        for (Coordinate c : cs) {
            p.add(
                    new GeometryFactory().createPoint(c)
            );

        }
        MinCircle mec = WelzlAlgo.mec(p);
        return (Polygon) mec.getCenter().buffer(mec.getR());
    }

}
