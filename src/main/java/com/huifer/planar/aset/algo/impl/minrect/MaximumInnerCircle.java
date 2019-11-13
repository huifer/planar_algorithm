package com.huifer.planar.aset.algo.impl.minrect;


import lombok.ToString;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * <p>Title : 最大内接圆</p>
 *
 * @author huifer
 * @date 2019-03-11
 */
public class MaximumInnerCircle {

    public static void main(String[] args) throws Exception {
        Geometry read = new WKTReader()
                .read("POLYGON ((35 10, 45 45, 15 40, 10 20, 35 10))");
        Point polylabel = MaximumInnerCircle.inCirclePoint((Polygon) read, 0.000001);
        Polygon polygon = MaximumInnerCircle.inCirclePolygon((Polygon) read, 0.000001);
        System.out.println(polylabel);
        System.out.println(polygon);
    }


    /**
     * 最大内接圆的图形
     *
     * @param polygon   需要求最大内接圆的面
     * @param precision 预设容差，0.0001 在接口中已经写死
     * @return 圆
     */
    public static Polygon inCirclePolygon(Polygon polygon, double precision) {

        // 四至
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (int i = 0; i < polygon.getCoordinates().length; i++) {
            double lon = polygon.getCoordinates()[i].x;
            double lat = polygon.getCoordinates()[i].y;

            minX = StrictMath.min(minX, lon);
            minY = StrictMath.min(minY, lat);
            maxX = StrictMath.max(maxX, lon);
            maxY = StrictMath.max(maxY, lat);

        }

        double width = maxX - minX;
        double height = maxY - minY;
        double cellSize = Math.min(width, height);
        double h = cellSize / 2;

        if (cellSize == 0) {
            return null;
        }

        // 根据点到多边形的最大距离排序
        PriorityQueue<Cell> cellSort = new PriorityQueue<>(new CellComparator());

        // 创建单元格
        for (double x = minX; x < maxX; x += cellSize) {
            for (double y = minY; y < maxY; y += cellSize) {
                cellSort.add(new Cell(x + h, y + h, h, polygon));
            }
        }

        //求重心
        Cell bestCell = getCentroidCell(polygon);
        Cell bboxCell = new Cell(minX + width / 2, minY + height / 2, 0, polygon);
        if (bboxCell.d > bestCell.d) {
            bestCell = bboxCell;
        }

        while (!cellSort.isEmpty()) {
            // 选择一个单元格进行解析
            Cell cell = cellSort.poll();
            //1.这个单元格比上一个单元格好那么用这个单元格
            if (cell.d > bestCell.d) {
                bestCell = cell;
            }

            // 如果这个单元格的值小于了预设值结束
            if (cell.max - bestCell.d <= precision) {
                continue;
            }

            // 通过就继续拆分
            h = cell.h / 2;
            cellSort.add(new Cell(cell.x - h, cell.y - h, h, polygon));
            cellSort.add(new Cell(cell.x + h, cell.y - h, h, polygon));
            cellSort.add(new Cell(cell.x - h, cell.y + h, h, polygon));
            cellSort.add(new Cell(cell.x + h, cell.y + h, h, polygon));
        }

        Polygon buffer = (Polygon) new GeometryFactory()
                .createPoint(new Coordinate(bestCell.x, bestCell.y))
                .buffer(bestCell.d);
        return buffer;
    }


    public static Point inCirclePoint(Polygon polygon, double precision) {

        // 四至
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;
        for (int i = 0; i < polygon.getCoordinates().length; i++) {
            double lon = polygon.getCoordinates()[i].x;
            double lat = polygon.getCoordinates()[i].y;

            minX = StrictMath.min(minX, lon);
            minY = StrictMath.min(minY, lat);
            maxX = StrictMath.max(maxX, lon);
            maxY = StrictMath.max(maxY, lat);

        }

        double width = maxX - minX;
        double height = maxY - minY;
        double cellSize = Math.min(width, height);
        double h = cellSize / 2;

        if (cellSize == 0) {
            return new GeometryFactory().createPoint(new Coordinate(minX, minY));
        }

        // 根据点到多边形的最大距离排序
        PriorityQueue<Cell> cellSort = new PriorityQueue<>(new CellComparator());

        // 创建单元格
        for (double x = minX; x < maxX; x += cellSize) {
            for (double y = minY; y < maxY; y += cellSize) {
                cellSort.add(new Cell(x + h, y + h, h, polygon));
            }
        }

        //求重心
        Cell bestCell = getCentroidCell(polygon);
        Cell bboxCell = new Cell(minX + width / 2, minY + height / 2, 0, polygon);
        if (bboxCell.d > bestCell.d) {
            bestCell = bboxCell;
        }

        while (!cellSort.isEmpty()) {
            // 选择一个单元格进行解析
            Cell cell = cellSort.poll();
            //1.这个单元格比上一个单元格好那么用这个单元格
            if (cell.d > bestCell.d) {
                bestCell = cell;
            }

            // 如果这个单元格的值小于了预设值结束
            if (cell.max - bestCell.d <= precision) {
                continue;
            }

            // 通过就继续拆分
            h = cell.h / 2;
            cellSort.add(new Cell(cell.x - h, cell.y - h, h, polygon));
            cellSort.add(new Cell(cell.x + h, cell.y - h, h, polygon));
            cellSort.add(new Cell(cell.x - h, cell.y + h, h, polygon));
            cellSort.add(new Cell(cell.x + h, cell.y + h, h, polygon));
        }

        Geometry buffer = new GeometryFactory().createPoint(new Coordinate(bestCell.x, bestCell.y))
                .buffer(bestCell.d);
        return new GeometryFactory().createPoint(new Coordinate(bestCell.x, bestCell.y));
    }

    /**
     * 多边形质心
     *
     * @param polygon 多边形
     * @return 质心
     */
    private static Cell getCentroidCell(Polygon polygon) {
        double area = 0;
        double x = 0;
        double y = 0;

        for (int i = 0, len = polygon.getCoordinates().length, j = len - 1; i < len; j = i++) {
            Coordinate a = polygon.getCoordinates()[i];
            Coordinate b = polygon.getCoordinates()[j];
            double ax = a.x;
            double ay = a.y;
            double bx = b.x;
            double by = b.y;

            double f = ax * by - bx * ay;
            x += (ax + bx) * f;
            y += (ay + by) * f;
            area += f * 3;
        }

        if (area == 0) {
            Coordinate c = polygon.getCoordinates()[0];
            return new Cell(c.x, c.y, 0, polygon);
        }

        return new Cell(x / area, y / area, 0, polygon);
    }

    /**
     * 定义一个排序规则: 点到多边形的最大距离
     */
    private static class CellComparator implements Comparator<Cell> {

        @Override
        public int compare(Cell o1, Cell o2) {
            return Double.compare(o2.max, o1.max);
        }
    }

    /**
     * 矩形网格
     */
    @ToString
    private static class Cell {

        /**
         * 矩形的重心x坐标
         */
        private double x;

        /**
         * 矩形的重心y坐标
         */
        private double y;

        /**
         * 这个矩形的一半
         */
        private double h;

        /**
         * 这个矩形到多边形的距离，半径
         */
        private double d;

        /**
         * 这个矩形到多边形的距离(最大值)
         */
        private double max;

        private Cell(double x, double y, double h, Polygon polygon) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.d = pointToPolygonDist(x, y, polygon);
            this.max = this.d + this.h * Math.sqrt(2);

            double ax = x - h / 2;
            double ay = y - h / 2;
            double bx = x - h / 2;
            double by = y + h / 2;
            double cx = x + h / 2;
            double cy = y + h / 2;
            double dx = x + h / 2;
            double dy = y - h / 2;

//            String o =
//                    "POLYGON ((" + ax + " " + ay + " ,  " + bx + " " + by + " ,  " + cx + " " + cy
//                            + " ,  " + dx + " " + dy + " ,  " + ax + " " + ay + "))";
//            try {
//                Geometry read = new WKTReader().read(o);
//                Point p = new GeometryFactory().createPoint(new Coordinate(x, y));
//                Geometry buffer = p.buffer(d);
////                System.out.println(buffer);
//
//                System.out.println(read);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


        }

        /***
         * 点到多边形的距离，点在外面负数
         * @param x x坐标
         * @param y y坐标
         * @param polygon 多边形
         * @return 距离
         */
        private double pointToPolygonDist(double x, double y, Polygon polygon) {
            boolean inside = false;
            double minDistSq = Double.MAX_VALUE;

            for (int i = 0, len = polygon.getCoordinates().length, j = len - 1; i < len; j = i++) {
                Coordinate a = polygon.getCoordinates()[i];
                Coordinate b = polygon.getCoordinates()[j];
                double ax = a.x;
                double ay = a.y;
                double bx = b.x;
                double by = b.y;

                if ((ay > y != by > y) &&
                        (x < (bx - ax) * (y - ay) / (by - ay) + ax)) {
                    inside = !inside;
                }

                minDistSq = Math.min(minDistSq, getSegDistSq(x, y, a, b));
            }

            return (inside ? 1 : -1) * Math.sqrt(minDistSq);
        }


        /**
         * 点到多边形的距离<sup>2</sup>，点在外面负数
         *
         * @return 点到多边形的距离平方
         */
        private double getSegDistSq(double px, double py, Coordinate a, Coordinate b) {

            double x = a.x;
            double y = a.y;
            double dx = b.x - x;
            double dy = b.y - y;

            if (dx != 0 || dy != 0) {
                double t = ((px - x) * dx + (py - y) * dy) / (dx * dx + dy * dy);

                if (t > 1) {
                    x = b.x;
                    y = b.y;
                } else if (t > 0) {
                    x += dx * t;
                    y += dy * t;
                }
            }

            dx = px - x;
            dy = py - y;

            return dx * dx + dy * dy;
        }

    }
}
