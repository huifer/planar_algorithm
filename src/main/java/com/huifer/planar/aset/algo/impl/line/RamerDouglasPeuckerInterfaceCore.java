package com.huifer.planar.aset.algo.impl.line;

import com.huifer.planar.aset.algo.RamerDouglasPeuckerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : RamerDouglasPeuckerInterfaceCore </p>
 * <p>Description : 拉默-道格拉斯-普克算法 rdp</p>
 *
 * @author huifer
 * @date 2019-01-24
 */
public class RamerDouglasPeuckerInterfaceCore implements RamerDouglasPeuckerInterface {

    /**
     * 平方
     *
     * @param x 值
     * @return 值的平方
     */
    private static double sqr(double x) {
        return Math.pow(x, 2);
    }

    /**
     * 计算点之间的距离
     *
     * @param startX 起点x
     * @param startY 起点y
     * @param endX   终点x
     * @param endY   终点y
     * @return 距离
     */
    private static double calcPointDist(double startX, double startY, double endX,
                                        double endY) {
        return sqr(startX - endX) + sqr(startY - endY);
    }

    /**
     * 距离的平方
     *
     * @param pointX 待测点x
     * @param pointY 待测点y
     * @param startX 起点x
     * @param startY 起点y
     * @param endX   终点x
     * @param endY   终点y
     * @return 距离平方
     */
    private static double distanceToSquared(double pointX, double pointY, double startX,
                                            double startY, double endX, double endY) {
        double l2 = calcPointDist(startX, startY, endX, endY);
        if (l2 == 0) {
            return calcPointDist(pointX, pointY, startX, startY);
        }
        double t =
                ((pointX - startX) * (endX - startX) + (pointY - startY) * (endY - startY)) / l2;
        if (t < 0) {
            return calcPointDist(pointX, pointY, startX, startY);
        }
        if (t > 1) {
            return calcPointDist(pointX, pointY, endX, endY);
        }
        return calcPointDist(pointX, pointY, (startX + t * (endX - startX)),
                (startY + t * (endY - startY)));
    }


    /**
     * 求解垂直距离
     *
     * @param pointX 待测点x
     * @param pointY 待测点y
     * @param startX 起点x
     * @param startY 起点y
     * @param endX   终点x
     * @param endY   终点y
     * @return 点到直线的垂直距离
     */
    private static double calcVerticalDist(double pointX, double pointY, double startX,
                                           double startY,
                                           double endX, double endY) {
        return Math.sqrt(distanceToSquared(pointX, pointY, startX, startY, endX, endY));
    }

    /**
     * douglasPeucker 实现
     *
     * @param list       原始数据 [  [x1,y1],[x2,y2],[x3,y3]  ]
     * @param startIndex 起点索引
     * @param endIndex   终点索引
     * @param threshold  阈值
     * @param resultList 数据结果
     */
    private static void douglasPeucker(List<Double[]> list, int startIndex, int endIndex,
                                       double threshold, List<Double[]> resultList) {
        // 找出最大距离点
        double dmax = 0;
        int index = 0;

        int start = startIndex;
        int end = endIndex - 1;
        for (int i = start + 1; i < end; i++) {
            double aPointX = list.get(i)[0];
            double aPointY = list.get(i)[1];
            double startX = list.get(start)[0];
            double startY = list.get(start)[1];
            double endX = list.get(end)[0];
            double endY = list.get(end)[1];
            double d = calcVerticalDist(aPointX, aPointY, startX, startY, endX, endY);
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }
        // 如果最大值超过预设值简化
        if (dmax > threshold) {
            douglasPeucker(list, startIndex, index, threshold, resultList);
            douglasPeucker(list, index, endIndex, threshold, resultList);
        } else {
            if ((end - start) > 0) {
                resultList.add(list.get(start));
                resultList.add(list.get(end));
            } else {
                resultList.add(list.get(start));
            }
        }
    }


    /**
     * douglasPeucker
     *
     * @param list      [  [x1,y1],[x2,y2],[x3,y3]  ]
     * @param threshold 预设阈值
     * @return 简化后的曲线坐标值
     */
    @Override
    public List<Double[]> douglasPeucke(List<Double[]> list, double threshold) {
        final List<Double[]> resultList = new ArrayList<Double[]>();
        douglasPeucker(list, 0, list.size(), threshold, resultList);
        return resultList;
    }
}
