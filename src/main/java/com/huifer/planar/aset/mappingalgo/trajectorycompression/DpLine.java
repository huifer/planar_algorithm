package com.huifer.planar.aset.mappingalgo.trajectorycompression;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : DpLine </p>
 * <p>Description : 轨迹数据压缩算法</p>
 *
 * @author huifer
 * @date 2018/10/19
 */
@Slf4j
public class DpLine {

    List<Point> pointIndexsToKeep = new ArrayList<>();

    public static void main(String[] args) {
        ReadText readTxt = new ReadText();
        List<Point> pointList = readTxt.readTxtFile(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\trajectorycompression\\read.txt");
        DpLine dPline = new DpLine();
        List<Point> dp = dPline.dp(pointList, 0.001);
//        System.out.println(dp);
        log.info("dp = {}",dp);
    }


    /**
     * dp压缩路径算法
     *
     * @param pointList 点集
     * @param tolerance 误差值
     * @return 压缩后值
     */
    public List<Point> dp(List<Point> pointList, double tolerance) {

        if (pointList == null || pointList.size() < 3) {

        }
        int firstPoint = 0;
        int lastPoitn = pointList.size() - 1;

        pointIndexsToKeep.add(pointList.get(firstPoint));
        pointIndexsToKeep.add(pointList.get(lastPoitn));

        while (pointList.get(firstPoint).equals(pointList.get(lastPoitn))) {
            lastPoitn--;
        }
        douglasPeuckerReduction(pointList, firstPoint, lastPoitn, tolerance);

        List<Point> aaa = pointIndexsToKeep;
        return aaa;
    }


    /**
     * dp压缩路径主算法
     *
     * @param points 点集合
     * @param firstPoint 第一个点
     * @param lastPoint 最后一个点
     * @param tolerance 误差值
     */
    private void douglasPeuckerReduction(List<Point> points, int firstPoint, int lastPoint,
            double tolerance) {
        double maxDistance = 0.0;
        int indexFarthest = 0;

        for (int index = firstPoint; index < lastPoint; index++) {
            Double distance = height(points.get(firstPoint), points.get(lastPoint),
                    points.get(index));

            if (distance > maxDistance) {
                maxDistance = distance;
                indexFarthest = index;
            }
        }

        if (maxDistance > tolerance && indexFarthest != 0) {
            pointIndexsToKeep.add(points.get(indexFarthest));

            douglasPeuckerReduction(points, firstPoint, indexFarthest, tolerance);
            douglasPeuckerReduction(points, indexFarthest, lastPoint, tolerance);
        }
    }


    /**
     * 点到直线的距离
     *
     * @param point1 Point
     * @param point2 Point
     * @param point Point
     * @return length
     */
    private Double height(Point point1, Point point2, Point point) {
        Double area = Math.abs(.5 * (point1.getX() * point2.getY() + point2.getX() *
                point.getY() + point.getX() * point1.getY() - point2.getX() * point1.getY()
                - point.getX() *
                point2.getY() - point1.getX() * point.getY()));
        Double bottom = Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) +
                Math.pow(point1.getY() - point2.getY(), 2));
        Double height = area / bottom * 2;

        return height;
    }

}
