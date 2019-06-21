package com.huifer.planar.aset.mappingalgo.grid.lib;

import java.util.List;

/**
 * <p>Title : HeightCalc </p>
 * <p>Description : 高度计算</p>
 *
 * @author huifer
 * @date 2018/11/15
 */
public class HeightCalc {

    private Grid grid;
    private List<PointInfo> points;
    private double sortRange;
    private double sortRadius;

    public HeightCalc(Grid grid, List<PointInfo> points, double sortRange) {
        this.points = points;
        this.grid = grid;
        this.sortRange = sortRange;
    }

    public void runCalc(double baseHeight) {
        sortRadius = sortRange * (grid.width + grid.height) * grid.interval / 2;
        for (int row = 0; row < grid.height; row++) {

            for (int col = 0; col < grid.width; col++) {

                GridCell cell = grid.gridCells[row][col];
                if (cell.isInSide) {
                    double h1 = cell.upLeft.h = calcHeight(cell.upLeft);
                    double h2 = cell.upRight.h = calcHeight(cell.upRight);
                    double h3 = cell.downLeft.h = calcHeight(cell.downLeft);
                    double h4 = cell.downRight.h = calcHeight(cell.downRight);
                    cell.height = (h1 + h2 + h3 + h4) / 4;
                } else {
                    cell.upLeft.h = baseHeight;
                    cell.upRight.h = baseHeight;
                    cell.downLeft.h = baseHeight;
                    cell.downRight.h = baseHeight;
                }
            }
        }
    }

    private double calcHeight(PointInfo pointInfo) {
        Vector vector;
        vector = new Vector();
        double weightSum = 0;
        double heightSum = 0;
        for (int i = 0; i < points.size(); i++) {
            PointInfo tem = points.get(i);
            vector.x = tem.x - pointInfo.x;
            vector.y = tem.y - pointInfo.y;
            double dist = GeoCalc.model(vector);
            if (dist <= sortRadius) {
                double weight = 1.0 / dist;
                weightSum += weight;
                double height = tem.h * weight;
                heightSum += height;
            }
        }
        return heightSum / weightSum;

    }

    @Override
    public String toString() {
        return "HeightCalc{" +
                "grid=" + grid +
                ", points=" + points +
                ", sortRange=" + sortRange +
                ", sortRadius=" + sortRadius +
                '}';
    }
}
