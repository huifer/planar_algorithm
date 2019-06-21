package com.huifer.planar.aset.utils.shptools.polygontools.geometry;

import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import com.huifer.planar.aset.utils.shptools.polygontools.entity.LatLngEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;


/**
 * 凸包算法,对于给定的2维空间的点集合，计算包含这些点的最小凸多边形。 返回该凸多边形的顶点集合
 *
 * @author huifer
 */
public class ConvexHull {


    /**
     * 从顶点p1开始，判断连续的三点p1p2p3组成的三角形是否为逆时针，若是则将三角型p1p2p3添加到三角型集合中，在顶点序列链表中去掉顶点p2，
     * 然后判断连续的三点p1p3p4是否构成逆时针三角形；若p1p2p3组成三角形为顺时针，则从p2点开始重复步骤1） 继续处理顶点序列，相邻三点组成逆时针三角形，则从顶点序列中去掉中间点，并将该三角形添加到三角形集合中。
     * 注：判断三角形ABC是否为逆时针，只需判定向量AC角度是否大于向量AB 或根据三角形面积是否为正判断。三角形面积为正，三角形为逆时针，否则为顺时针。
     */

    public static void main(String[] args) throws ParseException {

        Operation op = new Operation();

        Polygon polygonByWKT = op.createPolygonByWKT(
                "POLYGON ((85.87263679504395 54.5977840423584, 98.27873420715332 55.37316703796387, 97.07258415222168 47.53320503234863, 100.26026344299316 47.27474403381348, 107.66945838928223 41.50246238708496, 90.15027046203613 38.4332332611084, 85.87263679504395 54.5977840423584))");
        Coordinate[] coordinates = polygonByWKT.getCoordinates();

//        Arrays.stream(coordinates).forEach(
//                s -> {
//                    System.out.println(s.x + " - " + s.y);
//                }
//        );

//        System.out.println();
    }


    /***
     * 列表对象去重
     * @param pts 点集
     * @return 去重结果
     */
    public ArrayList<LatLngEntity> removeduplicate(List<LatLngEntity> pts) {

        HashSet<LatLngEntity> set = new HashSet<LatLngEntity>();
        for (int i = 0; i < pts.size(); i++) {
            set.add(pts.get(i));
        }
        ArrayList<LatLngEntity> result = new ArrayList<LatLngEntity>();
        result.addAll(set);

        return result;
    }


    /**
     * 列表对象排序
     * @param al 点集
     * @return 排序后结果
     */
    public ArrayList<LatLngEntity> sort(ArrayList<LatLngEntity> al) {
        ComparatorLatLng comparator = new ComparatorLatLng();
        Collections.sort(al, comparator);
        return al;
    }

    /**
     * 计算二维点的凸包，输出逆时针方向凸包顶点列表，
     *
     * @param pts 二维顶点列表
     * @return 逆时针方向凸包顶点列表
     */
    public ArrayList<LatLngEntity> getConvexHull(List<LatLngEntity> pts) {
        ArrayList<LatLngEntity> points;
        ArrayList<LatLngEntity> lower;
        ArrayList<LatLngEntity> upper;
        ArrayList<LatLngEntity> output;

        points = sort(removeduplicate(pts));

        lower = new ArrayList<LatLngEntity>();
        for (int i = 0; i < points.size(); i++) {

            while (lower.size() >= 2 && cross(
                    new double[]{lower.get(lower.size() - 2).getLat(),
                            lower.get(lower.size() - 2).getLng()},
                    new double[]{lower.get(lower.size() - 1).getLat(),
                            lower.get(lower.size() - 1).getLng()},
                    new double[]{points.get(i).getLat(), points.get(i).getLng()}) <= 0) {
                lower.remove(lower.size() - 1);
            }

            lower.add(points.get(i));
        }

        upper = new ArrayList<LatLngEntity>();
        for (int i = points.size() - 1; i >= 0; i--) {

            while (upper.size() >= 2 && cross(
                    new double[]{upper.get(upper.size() - 2).getLat(),
                            upper.get(upper.size() - 2).getLng()},
                    new double[]{upper.get(upper.size() - 1).getLat(),
                            upper.get(upper.size() - 1).getLng()},
                    new double[]{points.get(i).getLat(), points.get(i).getLng()}) <= 0) {
                upper.remove(upper.size() - 1);
            }

            upper.add(points.get(i));
        }

        output = new ArrayList<LatLngEntity>();
        for (int i = 0; i < lower.size() - 1; i++) {
            output.add(lower.get(i));
        }
        for (int i = 0; i < upper.size() - 1; i++) {
            output.add(upper.get(i));
        }

        return output;

    }

    /**
     * 计算两个向量夹角OAB是顺时针还是逆时针
     *
     * @return 返回值为正，逆时针 返回值为负，顺时针 返回值为0， 共线
     */
    public double cross(double[] o, double[] a, double[] b) {
        return (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0]);
    }

}
