package com.huifer.planar.aset.algo.impl.polygon;

import com.huifer.planar.aset.algo.LineOverInterface;
import com.huifer.planar.aset.algo.MyRectangleOver;
import com.huifer.planar.aset.algo.impl.ovlayer.LineOverCore;
import com.huifer.planar.aset.entity.MyRectangle;

/**
 * <p>Title : MyRecatngleOverCore </p>
 * <p>Description : 矩形相交</p>
 *
 * @author huifer
 * @date 2018/12/20
 */
public class MyRecatngleOverCore implements MyRectangleOver {

    LineOverInterface over = new LineOverCore();


    /**
     * 计算两个矩形相交面积
     */
    @Override
    public double calcAreaIntersectionRectangles(MyRectangle r1, MyRectangle r2) {
        if (r1 == null || r2 == null) {
            return -1;
        }
        double p1x = r1.getLowLeft().getX();
        double p1y = r1.getLowLeft().getY();

        double p2x = p1x + r1.getLength();
        double p2y = p1y + r1.getWidth();

        double p3x = r2.getLowLeft().getX();
        double p3y = r2.getLowLeft().getY();

        double p4x = p3x + r2.getLength();
        double p4y = p3y + r2.getWidth();
        if (p1x > p4x || p2x < p3x || p1y > p4y || p2y < p3y) {
            return 0;
        }
        double len = Math.min(p2x, p4x) - Math.max(p1x, p3x);
        double wid = Math.min(p2y, p4y) - Math.max(p1y, p3y);
        return len * wid;

    }
}
