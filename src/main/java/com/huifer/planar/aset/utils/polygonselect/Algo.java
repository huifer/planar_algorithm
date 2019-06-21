package com.huifer.planar.aset.utils.polygonselect;


import static com.huifer.planar.aset.utils.polygonselect.ReadShpHelper.gc;
import static com.huifer.planar.aset.utils.polygonselect.ReadShpHelper.polygon2Line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

/**
 * <p>Title : Algo </p>
 * <p>Description : 主要算法实现</p>
 *
 * @author huifer
 * @date 2019-04-26
 */
public class Algo {

    public static final String SHP_PATH = "E:\\mck\\point-verify\\src\\main\\resources\\shp\\afafaf.shp";
    public static final String ENCODE = "utf-8";
    public static final double BUFFER_SIZE = 1.2;

    public static void main(String[] args) {

        List<TocePolygon> shpPolygonList = gc(SHP_PATH, ENCODE);
        List<ToceLineString> toceLineStringList = polygon2Line(shpPolygonList);

        //////////////////////////////输出所有线段+buffer后的图形///////////////////////////////////

//        toceLineStringList.forEach(
//                s -> {
//                    s.getLineStringList().forEach(
//                            ss -> {
//                                Geometry buffer = ss.buffer(BUFFER_SIZE);
//                                System.out.println(buffer);
//                            }
//                    );
//                }
//        );

        /////////////////////////////////////////////////////////////////
        Set<String> s = new HashSet<>();
        for (int i = 0; i < toceLineStringList.size(); i++) {
            ToceLineString tLine = toceLineStringList.get(i);
            List<LineString> lineStringList = tLine.getLineStringList();
            List<LineString> allLineCon = allLineCon(toceLineStringList);

            allLineCon.removeAll(lineStringList);

            List<Integer> counts = new ArrayList<>();
            for (int j = 0; j < lineStringList.size(); j++) {
                LineString lineString = lineStringList.get(j);
                int i1 = lineStringBufferNumb(lineString, allLineCon);
                counts.add(i1);
            }
            System.out.println(tLine.getPolygonID() + counts);

            boolean b = nextEques(counts);
            if (b) {
                s.add(tLine.getPolygonID());
            }

        }

        System.out.println("选择内容id:" + s);
        System.out.println("总共选择：" + s.size());

    }

    private static int lineStringBufferNumb(LineString lineString, List<LineString> allLineCon) {
        int count = 0;
        for (int i = 0; i < allLineCon.size(); i++) {
            boolean contains = lineString.buffer(BUFFER_SIZE).intersects(allLineCon.get(i));
            if (contains) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取所有线段
     *
     * @param toceLineStringList ToceLineString集合
     * @return 线段
     */
    private static List<LineString> allLineCon(List<ToceLineString> toceLineStringList) {
        List<LineString> allLines = new ArrayList<>();
        toceLineStringList.forEach(
                s -> {
                    List<LineString> lineStrings = s.getLineStringList();
                    allLines.addAll(lineStrings);
                }
        );
        return allLines;
    }

    /**
     * 算法1： * 根据面的每一个结点做一个buffer ， * 连续两个结点的buffer压盖点数量等于0 那么这个面被选择
     */
    public static void algoOne() {
        List<TocePolygon> shpPolygonList = gc(SHP_PATH, ENCODE);
        List<TocePoint> polygon2pointList = polygon2point(shpPolygonList);
        //////////////////////////////输出所有结点+buffer后的图形///////////////////////////////////

//        for (int i = 0; i < polygon2pointList.size(); i++) {
//            TocePoint tocePoint = polygon2pointList.get(i);
//            List<Point> pointList = tocePoint.getPointList();
//            pointList.forEach(sss -> {
//                System.out.println(sss);
//                System.out.println(sss.buffer(BUFFER_SIZE));
//
//            });
//        }

        /////////////////////////////////////////////////////////////////

        // 循环每一个面的结点
        // 每一个结点buffer 长度10
        Set<String> s = new HashSet<>();

        for (int i = 0; i < polygon2pointList.size(); i++) {
            // 其中一个面的结点
            TocePoint tocePoint = polygon2pointList.get(i);
            List<Point> pointList = tocePoint.getPointList();
            // 去掉最后一个点 避免计算问题
            pointList.remove(pointList.size() - 1);
            List<Point> allPointList1 = allPointCon(polygon2pointList);
            allPointList1.removeAll(pointList);

            List<Integer> counts = new ArrayList<>();
            for (int j = 0; j < pointList.size(); j++) {
                Point point = pointList.get(j);
                // point 和所有的结点过滤
                int i1 = pointBufferNum(point, allPointList1);
                counts.add(i1);
            }
            System.out.println(tocePoint.getPolygonID() + counts);
            boolean b = nextEques(counts);
            if (b) {
                s.add(tocePoint.getPolygonID());
            }
        }
        System.out.println("选择内容id:" + s);
        System.out.println("总共选择：" + s.size());
    }


    /**
     * 是否是连续两个0
     *
     * @param counts 结点经过buffer覆盖的点数量
     * @return yes / no
     */
    public static boolean nextEques(List<Integer> counts) {

        if (counts.get(0).equals(0) && counts.get(counts.size() - 1).equals(0)) {

            return true;
        }

        int maxPassTimes = getMaxPassTimes(counts);

        return maxPassTimes > 1;
    }


    /**
     * 连续出现0的次数
     *
     * @param list 结点经过buffer覆盖的点数量
     * @return 连续出现0的次数
     */
    private static int getMaxPassTimes(List<Integer> list) {
        int count_0 = 0;
        int count_1 = 0;
        int max_0 = 0;
        int max_1 = 0;
        for (Integer s : list) {
            if (s == 0) {
                // 连续出现0的次数
                count_0++;
                count_1 = 0;
            } else if (s == 1) {
                count_1++;
                count_0 = 0;
            }

            if (count_0 > max_0) {
                max_0 = count_0;
            }
            if (count_1 > max_1) {
                max_1 = count_1;
            }
        }
        return max_0;
    }

    /**
     * 计算结点buffer后，压盖了多少点
     *
     * @param p 结点
     * @param pointList 点集合
     * @return 覆盖点数量总和
     */
    public static int pointBufferNum(Point p, List<Point> pointList) {
        int count = 0;
        for (int i = 0; i < pointList.size(); i++) {
            boolean contains = p.buffer(BUFFER_SIZE).contains(pointList.get(i));
            if (contains) {
                count++;
            }
        }
        return count;
    }


    /**
     * 获取所有结点
     *
     * @param polygon2pointList TocePoint点集合
     * @return 所有节点
     */
    public static List<Point> allPointCon(List<TocePoint> polygon2pointList) {
        List<Point> allPointList = new ArrayList<>();
        polygon2pointList.forEach(
                s -> {
                    List<Point> pointList = s.getPointList();
                    allPointList.addAll(pointList);
                }
        );
        return allPointList;
    }

}
