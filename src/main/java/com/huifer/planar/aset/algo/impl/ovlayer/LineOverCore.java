package com.huifer.planar.aset.algo.impl.ovlayer;

import com.huifer.planar.aset.algo.GraphInterface;
import com.huifer.planar.aset.algo.LineOverInterface;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.OvlayerEnum;
import com.huifer.planar.aset.entity.SortPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : LineOverCore </p>
 * <p>Description : LineOverInterface 实现</p>
 *
 * @author huifer
 * @date 2018/12/17
 */
@Slf4j
public class LineOverCore implements LineOverInterface {

    @Override
    public MyPoint getProjectivePoint(MyLine line, MyPoint out) {
        MyPoint res = new MyPoint();
        if (line.getSlope() == 0) {
            res.setX(out.getX());
            res.setY(line.getPoint1().getY());
            return res;
        } else {
            res.setX(((line.getSlope() * line.getPoint1().getX() + out.getX() / line.getSlope() + out.getY()
                    - line.getPoint1().getY()) / (1 / line.getSlope()
                    + line.getSlope())));
            res.setY((-1 / line.getSlope() * (res.getX() - out.getX()) + out.getY()));
            return res;
        }
    }

    @Override
    public MyPoint intersectPoint(MyLine l1, MyLine l2) {
        MyPoint intersection = MyPoint
                .intersection(l1.getPoint1(), l1.getPoint2(), l2.getPoint1(), l2.getPoint2());
        if (intersection.getX() == Double.MAX_VALUE &&
                intersection.getY() == Double.MAX_VALUE) {
            return null;
        } else {
            return intersection;
        }
    }

    @Override
    public final Map<String, List<SortPoint>> simpleLineSortPoint(MyLine line,
            ArrayList<MyPoint> points) {

        GraphInterface g = new GraphCore();
        ArrayList<SortPoint> sortPoints = new ArrayList<>();
        points.forEach(
                s -> {
                    OvlayerEnum ovlayerEnum = g.rightHandRule(s, line);
                    if (ovlayerEnum.equals(OvlayerEnum.ON_LINE) || ovlayerEnum
                            .equals(OvlayerEnum.ON_THE_EXTENSION_LINE)) {
                        throw new RuntimeException("输入点在必须在输入直线两侧 异常点为: " + s);
                    }
                    SortPoint sp = new SortPoint(s.getX(), s.getY());
                    sp.setLeftOrRight(ovlayerEnum);
                    sp.setProjectivePoint(line.getProjectivePoint(s));
                    sp.setDistanceWithP1(line.getPoint1().distance(s));
                    sortPoints.add(sp);
                }
        );

        List<SortPoint> rightPoints = sortPoints.stream()
                .filter(s -> s.getLeftOrRight() == OvlayerEnum.ON_THE_RIGHT_OF_THE_LINE)
                .sorted((s1, s2) -> Double.compare(s1.getDistanceWithP1(), s2.getDistanceWithP1()))
                .collect(
                        Collectors.toList());

        List<SortPoint> leftPoints = sortPoints.stream()
                .filter(s -> s.getLeftOrRight() == OvlayerEnum.ON_THE_LEFT_OF_THE_LINE)
                .sorted((s1, s2) -> Double.compare(s1.getDistanceWithP1(), s2.getDistanceWithP1()))
                .collect(
                        Collectors.toList());

        for (int i = 0; i < rightPoints.size(); i++) {
            SortPoint sortPoint = rightPoints.get(i);
            int code = 2 * i + 1;
            sortPoint.setLeftOrRightCode(code);
        }
        for (int i = 0; i < leftPoints.size(); i++) {
            SortPoint sortPoint = leftPoints.get(i);
            int code = 2 * i;
            sortPoint.setLeftOrRightCode(code);
        }

        Map<String, List<SortPoint>> result = new HashMap<>(2);
        result.put("right", rightPoints);
        result.put("left", leftPoints);
        return result;
    }


}
