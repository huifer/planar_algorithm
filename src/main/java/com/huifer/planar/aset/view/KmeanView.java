package com.huifer.planar.aset.view;

import com.huifer.planar.aset.algo.KmeanPolygonSplitInterface;
import com.huifer.planar.aset.algo.impl.polygon.KmeanPolygonSplitCore;
import com.huifer.planar.aset.entity.KmeanPolygonResult;
import com.huifer.planar.aset.utils.geo.GeomtryUtil;
import com.huifer.planar.aset.view.base.BaseFrame;
import com.huifer.planar.aset.view.base.FrameContext;
import com.huifer.planar.aset.view.base.ViewHelper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : KmeanView </p>
 * <p>Description : KmeanView</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public class KmeanView extends FrameContext {

    public static void main(String[] args) {
        KmeanView myContext = new KmeanView();
        BaseFrame frame = new BaseFrame(myContext);
        frame.run();
    }

    @Override
    public void drawing(Graphics g) throws ParseException {
        Graphics2D g2d = (Graphics2D) g;
        // 获取数据
        int setp = 3000;
        int k = 10;
        String polygonWkt = "POLYGON((411 80, 569 125, 675 238, 795 321, 917 416, 866 597, 600 699, 443 614, 500 483, 399 338, 411 80))";
        KmeanPolygonSplitInterface km = new KmeanPolygonSplitCore();
        KmeanPolygonResult result = km.splitPolygon(polygonWkt, setp, k);

        ArrayList<Double> xlist = result.getXlist();
        ArrayList<Double> ylist = result.getYlist();

        ArrayList<Point> pointList = result.getPointList();
        int[] assignments = result.getAssignments();
        double[][] centroids = result.getCentroids();
        List<Geometry> voronoi = result.getVoronoi();

//        System.out.println(result.getPolygon());
        List<Geometry> geometryList = result.getGeometryList();
        // 绘图流程
        ViewHelper.setStrokeWidth(g2d, 8);
        ViewHelper.setColor(g2d, ViewHelper.BLUE);
        ViewHelper.drawPolygon(g2d, xlist, ylist);

        geometryList.forEach(s -> System.out.println(s));

        drawKmean(g2d, pointList, assignments, centroids, voronoi, geometryList);

    }

    private void drawKmean(Graphics2D g2d, ArrayList<Point> points, int[] assignments,
            double[][] centroids, List<Geometry> voronoi, List<Geometry> geometryList) {
        // 绘制所有点
        ViewHelper.setStrokeWidth(g2d, 1);
        ArrayList<Color> colorList = ViewHelper.getColorList();
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            int assignment = assignments[i];
            int colorIndex = assignment % colorList.size();
            ViewHelper.setColor(g2d, colorList.get(colorIndex));
            ViewHelper.drawPoint(g2d, point.getX(), point.getY());
        }
        // 绘制 簇质心
        ViewHelper.setStrokeWidth(g2d, 6);
        ViewHelper.setColor(g2d, ViewHelper.BLACK);
        for (int i = 0; i < centroids.length; i++) {
            ViewHelper.drawPoint(g2d, centroids[i][0], centroids[i][1]);
        }
        // 绘制泰森多边形
        ViewHelper.setStrokeWidth(g2d, 1);
        ViewHelper.setColor(g2d, ViewHelper.BLACK);

        for (int i = 0; i < voronoi.size(); i++) {
            Geometry geometry = voronoi.get(i);
            Coordinate[] coordinates = geometry.getCoordinates();
            ArrayList<Double> geometryX = GeomtryUtil.getGeometryX(coordinates);
            ArrayList<Double> geometryY = GeomtryUtil.getGeometryY(coordinates);
            ViewHelper.drawPolygon(g2d, geometryX, geometryY);
        }

        ViewHelper.setStrokeWidth(g2d, 3);
        ViewHelper.setColor(g2d, ViewHelper.RED);



        for (int i = 0; i < geometryList.size(); i++) {
            Geometry geometry = geometryList.get(i);
            Coordinate[] coordinates = geometry.getCoordinates();
            ArrayList<Double> geometryX = GeomtryUtil.getGeometryX(coordinates);
            ArrayList<Double> geometryY = GeomtryUtil.getGeometryY(coordinates);
            ViewHelper.drawPolygon(g2d, geometryX, geometryY);
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
