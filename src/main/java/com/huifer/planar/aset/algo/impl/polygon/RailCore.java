package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.RailInterface;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.MyPolygon;
import java.util.ArrayList;

/**
 * <p>Title : RailCore </p>
 * <p>Description : 点面交集</p>
 *
 * @author huifer
 * @date 2018/12/14
 */
public class RailCore implements RailInterface {

    @Override
    public boolean isInPolygon(MyPoint point, MyPolygon polygon) {
        boolean result = false;
        ArrayList<MyLine> polylines = polygon.getPolylines();
        for (MyLine edge : polylines) {

            if ((edge.getPoint1().getX() < point.getX() && edge.getPoint2().getX() >= point.getX())
                    ||
                    (edge.getPoint2().getX() < point.getX() && edge.getPoint1().getX() >= point
                            .getX())
                            && (edge.getPoint1().getY() <= point.getY()
                            || edge.getPoint2().getY() <= point
                            .getY())
            ) {
                result ^= (edge.getPoint2().getY() +
                        (point.getX() - edge.getPoint2().getX()) / (edge.getPoint1().getX() - edge
                                .getPoint2()
                                .getX()) * (edge.getPoint1().getY() - edge.getPoint2().getY())
                        < point
                        .getY());
            }
        }

        return false;
    }
}
