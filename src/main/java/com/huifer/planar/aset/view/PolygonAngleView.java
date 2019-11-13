package com.huifer.planar.aset.view;

import com.huifer.planar.aset.algo.PolygonAngleInterface;
import com.huifer.planar.aset.algo.impl.polygon.PolygonAngleCore;
import com.huifer.planar.aset.algo.impl.polygon.PolygonAngleCore.PointF;
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
 * <p>Title : PolygonAngleView </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-01-28
 */
public class PolygonAngleView extends FrameContext {

    public static void main(String[] args) {
        PolygonAngleView polygonAngleView = new PolygonAngleView();
        BaseFrame frame = new BaseFrame(polygonAngleView);
        frame.run();
    }

    @Override
    public void drawing(Graphics g) throws ParseException {
//        String wkt = "POLYGON ((350 100, 450 450, 150 400, 100 200, 350 100), (200 300, 350 350, 300 200, 200 300))";
        String wkt = "POLYGON ((350 100, 450 450, 150 400, 100 200, 350 100))";

        ShapeWriter sw = new ShapeWriter();
        PolygonAngleInterface polygonAngleInterface = new PolygonAngleCore();
        PolygonAngleResult polygonAngleResult = polygonAngleInterface.polygonWktAngle(wkt);
        Graphics2D g2d = (Graphics2D) g;
        Polygon polygon = polygonAngleResult.getPolygon();
        List<HashMap<PointF, Double>> polygonAngles = polygonAngleResult.getPolygonAngles();

        // 绘图流程
        ViewHelper.setStrokeWidth(g2d, 2);
        ViewHelper.setColor(g2d, ViewHelper.BLUE);
        Shape polyShape = sw.toShape(polygon);
        g2d.draw(polyShape);

        ViewHelper.setColor(g2d, ViewHelper.GREEN);
        for (HashMap<PointF, Double> angle : polygonAngles) {
            angle.forEach(
                    (k, v) -> {
                        ViewHelper.drawText(g2d, v.toString(), (int) k.getX(), (int) k.getY());
                    }
            );
        }
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
