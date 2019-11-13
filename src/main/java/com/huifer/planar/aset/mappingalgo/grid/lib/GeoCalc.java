package com.huifer.planar.aset.mappingalgo.grid.lib;

/**
 * <p>Title : GeoCalc </p>
 * <p>Description : 坐标计算</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class GeoCalc {

    /**
     * 叉积
     */
    public static double crossMul(Vector v1, Vector v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    /***
     * 点乘
     * @param v1
     * @param v2
     * @return
     */
    public static double dotMul(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    /**
     * 取模
     *
     * @param vector
     * @return
     */
    public static double model(Vector vector) {
        return Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
    }

    /**
     * 计算体积
     *
     * @param grid       网格
     * @param baseHeight 基准高度
     * @return 体积
     */
    public static double calcVolumn(Grid grid, double baseHeight) {
        double voumnSum = 0;
        for (int row = 0; row < grid.height; row++) {
            for (int col = 0; col < grid.width; col++) {
                GridCell cell = grid.gridCells[row][col];
                if (cell.isInSide) {
//                    double vol = (cell.Height - baseheight) * cell.SideLength * cell.SideLength;
                    double vol = (cell.height - baseHeight) * cell.sideLength * cell.sideLength;
                    voumnSum += vol;
                }
            }

        }

        return voumnSum;
    }

}
