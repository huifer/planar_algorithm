package com.huifer.planar.aset.view;

import com.huifer.planar.aset.algo.PolygonAngleInterface;
import com.huifer.planar.aset.algo.impl.polygon.PolygonAngleCore;
import com.huifer.planar.aset.entity.PolygonAngleResult;
import com.huifer.planar.aset.view.base.BaseFrame;
import com.huifer.planar.aset.view.base.FrameContext;
import com.huifer.planar.aset.view.base.ViewHelper;
import org.locationtech.jts.awt.ShapeWriter;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 *
 * @author huifer
 * @date 2019-01-28
 */
public class PolygonAngleMergeView extends FrameContext {

    public static void main(String[] args) {
        PolygonAngleMergeView polygonAngleMerge = new PolygonAngleMergeView();
        BaseFrame frame = new BaseFrame(polygonAngleMerge);
        frame.run();
    }


    @Override
    public void drawing(Graphics g) throws ParseException {

        ShapeWriter sw = new ShapeWriter();
        Graphics2D g2d = (Graphics2D) g;
        String wkt = "POLYGON ((290.890041493776 392.188796680498,279.707468879668 191.919087136929,307.155601659751 327.126556016598,363.06846473029 170.570539419087,548.089211618257 237.665975103734,605.01867219917 407.4377593361,493.192946058091 301.711618257261,472.860995850622 232.582987551867,479.977178423236 311.877593360996,290.890041493776 392.188796680498))";  PolygonAngleInterface polygonAngleInterface = new PolygonAngleCore();
        PolygonAngleResult polygonAngleResult = polygonAngleInterface.polygonWktAngleMerge(wkt, 30.0);
        Polygon polygon = polygonAngleResult.getPolygon();
        Polygon newPolygon = polygonAngleResult.getNewPolygon();

        List<HashMap<PolygonAngleCore.PointF, Double>> polygonAngles = polygonAngleResult.getPolygonAngles();



        // 绘图流程
        ViewHelper.setStrokeWidth(g2d, 2);
        ViewHelper.setColor(g2d, ViewHelper.BLUE);
        Shape polyShape = sw.toShape(polygon);
        g2d.draw(polyShape);

        ViewHelper.setColor(g2d, ViewHelper.GREEN);
        for (HashMap<PolygonAngleCore.PointF, Double> angle : polygonAngles) {
            angle.forEach(
                    (k, v) -> {
                        ViewHelper.drawText(g2d, v.toString(), (int) k.getX(), (int) k.getY());
                    }
            );
        }

        ViewHelper.setColor(g2d, ViewHelper.YELLOW);

        polyShape = sw.toShape(newPolygon);
        g2d.draw(polyShape);

    }

    @Override
    public void paintComponent(Graphics g) {
        try {
            drawing(g);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
