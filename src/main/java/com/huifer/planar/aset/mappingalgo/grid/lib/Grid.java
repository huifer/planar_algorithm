package com.huifer.planar.aset.mappingalgo.grid.lib;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Title : Grid </p>
 * <p>Description : Grid</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class Grid {

    /**
     * 矩阵网格
     */
    public GridCell[][] gridCells;


    /**
     * 间隔
     */
    public double interval;

    @Override
    public String toString() {
        return "Grid{" +
                "gridCells=" + Arrays.toString(gridCells) +
                ", interval=" + interval +
                ", width=" + width +
                ", height=" + height +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", minX=" + minX +
                ", minY=" + minY +
                ", convex=" + convex +
                '}';
    }

    /**
     * 宽高
     */
    public int width, height;

    /**
     * 四至
     */
    private double maxX, maxY, minX, minY;

    /**
     * 凸包
     */
    private Convex convex;

    public Grid(Convex convex, double interval) {
        this.convex = convex;
        this.interval = interval;
        // 初始化网格
        initGrid();
        // 初始化单元格
        initCell();
        // 凸包内网格
        calcInSide();
    }

    private void calcInSide() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                PointInfo p = new PointInfo();
                p.x = gridCells[row][col].downLeft.x + interval / 2;
                p.y = gridCells[row][col].downLeft.y + interval / 2;
                // 内部判断方法 单点射线
                boolean side = inSideWithRay(convex.vertexs, p);

//                System.out.println(row + " , " + col + " : " + side);
                gridCells[row][col].isInSide = side;
            }
        }
    }

    /**
     * 射线法判断是否在内部
     *
     * @return boolean
     */
    private boolean inSideWithRay(List<PointInfo> vertexs, PointInfo p) {
        // 单边相交数量
        int cross = 0;
        int count = vertexs.size();
        for (int i = 0; i < count; i++) {
            PointInfo p1 = vertexs.get(i);
            PointInfo p2 = vertexs.get((i + 1) % count);
            if (p1.y == p2.y) {
                // 平行
                continue;
            }
            if (p.y < Math.min(p1.y, p2.y)) {
                // 交点在 p1p2延长线上
                continue;
            }
            if (p.y >= Math.max(p1.y, p2.y)) {
                // 交点在 p1p2延长线上
                continue;
            }
            // 交点计算
            double x =  (p.y - p1.y) *  (p2.x - p1.x) /  (p2.y - p1.y) + p1.x;
            if (x > p.x) {
                cross++;
            }
        }
        return (cross % 2 == 1);
    }

    /**
     * 初始化单元格
     */
    private void initCell() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                GridCell cell = new GridCell();
                PointInfo upLeft = new PointInfo();
                PointInfo upRight = new PointInfo();
                PointInfo downLeft = new PointInfo();
                PointInfo downRight = new PointInfo();

                downLeft.name = "DownLeft";
                downLeft.x = minX + col * interval;
                downLeft.y = minY + row * interval;

                downRight.name = "DownRight";
                downRight.x = downLeft.x + interval;
                downRight.y = downLeft.y;

                upLeft.name = "UpLeft";
                upLeft.x = downLeft.x;
                upLeft.y = downLeft.y + interval;

                upRight.name = "UpRight";
                upRight.x = downLeft.x + interval;
                upRight.y = downLeft.y + interval;

                cell.sideLength = interval;
                cell.upLeft = upLeft;
                cell.upRight = upRight;
                cell.downLeft = downLeft;
                cell.downRight = downRight;
                gridCells[row][col] = cell;
            }
        }
    }

    /**
     * 初始化网格
     */
    private void initGrid() {
        maxX = minX = convex.vertexs.get(0).x;
        maxY = minY = convex.vertexs.get(0).y;

        for (int i = 1; i < convex.vertexs.size(); i++) {
            PointInfo p = convex.vertexs.get(i);
            if (p.x > maxX) {
                maxX = p.x;
            } else if (p.x < minX) {
                minX = p.x;
            }

            if (p.y > maxY) {
                maxY = p.y;
            } else if (p.y < minY) {
                minY = p.y;
            }

        }

        width = (int) Math.ceil((maxX - minX) / interval);
        height = (int) Math.ceil((maxY - minY) / interval);
        gridCells = new GridCell[height][width];
    }


}
