package com.huifer.planar.aset.algo.impl.minrect;

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Point;

/**
 * <p>Title : WelzlAlgo </p>
 * <p>Description :  最小外接圆实现算法</p>
 * <p>https://en.wikipedia.org/wiki/Smallest-circle_problem</p>
 *
 * @author huifer
 * @date 2019-03-13
 */
public class WelzlAlgo {


    /**
     * mec算法
     *
     * @param pointSet 点集合
     * @param maxId 最大ID
     * @return {@link MinCircle} 最小外接圆
     */
    public static MinCircle mec(List<Point> pointSet, int maxId) {
        MinCircle retCircle = null;
        if (maxId < 0) {
            retCircle = new MinCircle(0, 0, 0);
        } else {
            Point point = pointSet.get(maxId);
            retCircle = mec(pointSet, maxId - 1);
            if (!retCircle.contains(point)) {
                List<Point> boundarySet = new ArrayList<>();
                boundarySet.add(point);
                retCircle = bMec(pointSet, maxId - 1, boundarySet);
            }
        }
        return retCircle;
    }

    /**
     * bMec 算法流程
     *
     * @param pointSet 点集合
     * @param maxId 最大id
     * @param boundarySet 边界点
     * @return 圆
     */
    private static MinCircle bMec(List<Point> pointSet, int maxId, List<Point> boundarySet) {
        MinCircle resultMinCircle = new MinCircle(0, 0, 0);
        if (maxId < 0 || boundarySet.size() == 3) {
            if (boundarySet.size() == 1) {
                resultMinCircle = new MinCircle(boundarySet.get(0));
            } else {
                if (boundarySet.size() == 2) {
                    resultMinCircle = new MinCircle(boundarySet.get(0),
                            boundarySet.get(1));
                } else {
                    if (boundarySet.size() == 3) {
                        resultMinCircle = new MinCircle(boundarySet.get(0),
                                boundarySet.get(1), boundarySet.get(2));
                    }
                }
            }
        } else {
            Point point = pointSet.get(maxId);
            resultMinCircle = bMec(pointSet, maxId - 1, boundarySet);
            if (!resultMinCircle.contains(point)) {
                boundarySet.add(point);
                resultMinCircle = bMec(pointSet, maxId - 1, boundarySet);
                boundarySet.remove(point);
            }
        }
        return resultMinCircle;
    }

    public static MinCircle mec(List<Point> p) {
        MinCircle circle = mec(p, p.size() - 1);
        return circle;
    }
}
