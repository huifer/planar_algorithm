package com.huifer.planar.aset.algo.impl.concave;

import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.TroughLineWithSort;
import com.huifer.planar.aset.entity.PublicRadial;
import com.huifer.planar.aset.utils.AdjacentMatrixUtil;
import com.huifer.planar.aset.utils.CommonUtils;
import com.huifer.planar.aset.utils.simplecycles.SearchCycles;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Title : SimpleCutPolygon </p>
 * <p>Description : 简单切割面</p>
 *
 * @author huifer
 * @date 2019-03-11
 */
public class SimpleCutPolygon {


    /**
     * 简单切割面
     * <P>保留了所有的凸包</P>
     *
     * @param pg        被切割的面
     * @param splitLine 切割线段
     * @return 切割后的面
     */
    public static List<Polygon> simpleCutPolygonWithLine(Polygon pg,
                                                         List<TroughLineWithSort> splitLine) throws Exception {
        // 需要的参数
        // 面的线段
        List<LineString> pgLine = CommonUtils.polygonLineString(pg);
        // 内部线段
        List<LineString> inLine = new ArrayList<>();
        splitLine.forEach(s -> {
            inLine.add(s.getAoLineResult());
        });
        List<LineString> allLine = calcLineCutLine(pgLine, inLine);

//        //com.huifer.planar.aset.utils.simplecycles 做闭环搜索
        List<Point> paramPoint = CommonUtils.lines2point(allLine);
        List<List<Boolean>> bs = AdjacentMatrixUtil
                .createAdjacentMatrixBoolean(paramPoint, allLine);
        SearchCycles searchCycles = new SearchCycles(bs, paramPoint);
        List nodeCycles = searchCycles.getNodeCycles();
        ArrayList<Polygon> re = new ArrayList<>();
        for (int i = 0; i < nodeCycles.size(); i++) {
            List cyc = (List) nodeCycles.get(i);
            if (cyc.size() >= 3) {
                cyc.add(cyc.get(0));
                Polygon polygonByPointList = CommonUtils.createPolygonByPointList(cyc);
                Geometry geometry = polygonByPointList.convexHull();
                if (Math.abs(polygonByPointList.getArea() - geometry.getArea()) <= 0.0002) {
                    re.add(polygonByPointList);
                }
            }
        }
        return re;
    }

    /**
     * 线段和线段切割
     *
     * @param pgLine 面的构造线段
     * @param inLine 内部线
     * @return 线段和线段切割后的线段集合
     */
    private static List<LineString> calcLineCutLine(List<LineString> pgLine,
                                                    List<LineString> inLine) throws Exception {
        Set<LineString> result = new HashSet<>();
        List<LineString> allLineString = new ArrayList<>();
        allLineString.addAll(pgLine);
        allLineString.addAll(inLine);

        double lineMaxX = CommonUtils.lineMaxX(allLineString);
        double lineMaxY = CommonUtils.lineMaxY(allLineString);
        double lineMinX = CommonUtils.lineMinX(allLineString);
        double lineMinY = CommonUtils.lineMinY(allLineString);

        List<Point> allPoint = CommonUtils.lines2point(allLineString);
        // 取得的线段有容差不确定具体数值 用数学方法求解
        for (int i = 0; i < allLineString.size(); i++) {
            List<Point> inP = new ArrayList<>();
            LineString iLine = allLineString.get(i);

            for (int j = 0; j < allLineString.size(); j++) {
                LineString jLine = allLineString.get(j);

                if (!iLine.equals(jLine)) {

                    if (iLine.intersects(jLine)) {
                        Geometry intersection = iLine.intersection(jLine);
                        if (intersection instanceof Point) {
                            inP.add((Point) intersection);
                        }

                    } else {

                        PublicRadial plj = new PublicRadial(jLine.getStartPoint(),
                                jLine.getEndPoint());
                        double calcXXMAX = plj.calcXX(lineMaxY);
                        double calcXXMin = plj.calcXX(lineMinY);
                        LineString lineString = jLine.getFactory()
                                .createLineString(new Coordinate[]{
                                        new Coordinate(calcXXMAX, lineMaxY),
                                        new Coordinate(calcXXMin, lineMinY)
                                });
                        Geometry intersection = lineString.intersection(iLine);

                        if (intersection instanceof Point) {
                            // 过滤点距离太大的
                            Point p1 = (Point) intersection;
                            double d1 = p1.distance(iLine.getStartPoint());
                            double d2 = p1.distance(iLine.getEndPoint());

                            double d3 = p1.distance(jLine.getStartPoint());
                            double d4 = p1.distance(jLine.getEndPoint());

//                            if (iLine.intersects(p1)) {
//                                inP.add(p1);
//                                continue;
//                            }

                            if (d1 < 0.00002 || d2 < 0.00002 || d3 < 0.00002 || d4 < 0.00002) {
                                inP.add(p1);
                            }
                        }
                    }

                }
            }

            List<LineString> lineStrings = CommonUtils.lineSplit(iLine, inP);
            result.addAll(lineStrings);
        }

        List<LineString> oc = new ArrayList<>();
        for (LineString s : result) {
            Coordinate[] cs = s.getCoordinates();
            for (int i = 0; i < cs.length; i++) {
                Coordinate c = cs[i];
                cs[i] = new Coordinate(CommonUtils.reserveDecimal(c.x, 3),
                        CommonUtils.reserveDecimal(c.y, 3));
            }
            oc.add(new GeometryFactory().createLineString(cs));
        }

        return oc;
    }


}
