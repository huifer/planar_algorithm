package com.huifer.planar.aset.mappingalgo.fanjuli;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : InverseDistanceCore </p>
 * <p>Description : 运行工具</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
@Slf4j
public class InverseDistanceCore {

    public static void main(String[] args) {
        ReadHelper readHelper = new ReadHelper(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\fanjuli\\测站坐标.txt");
        List<MyPoint> points = readHelper.getPoints();

        List<MyPoint> paramList = new ArrayList<>();
        MyPoint p = new MyPoint("Q1", 4310, 3600, 0);
        HashMap<String, Object> idw = idw(points, p, 5);
//        System.out.println(idw);
        log.info("idw = {}", idw);
    }

    /**
     * idw 主要流程
     * @param points
     * @param point
     */
    public static HashMap<String, Object> idw(List<MyPoint> points,MyPoint point,int n) {

        HashMap<String, Object> res = new HashMap<>();

        for (int i = 0; i < points.size(); i++) {
            double d = distance(points.get(i), point);
            points.get(i).setDist(d);
        }
        // 排序
        points.sort(
                Comparator.comparingDouble(MyPoint::getDist)
        );
        double h = getH(points,n);
        point.setZ(h);
        res.put("参数点", point);
        res.put("参与插值", points.subList(0, n));
        return res;
    }

    /**
     * 计算 距离
     * @param p1 第一个点
     * @param p2 第二个点
     * @return double 距离
     */
    private static double distance(MyPoint p1, MyPoint p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        double ds = Math.sqrt(dx * dx + dy * dy);
        return ds;
    }

    /***
     * 计算高
     * @param points 点列表
     * @param n 参与数量
     * @return double h
     */
    private static double getH(List<MyPoint> points,int n) {
        double over = 0;
        double under = 0;
        for (int i = 0; i < n; i++) {
            over += points.get(i).getZ() / points.get(i).getDist();
            under += 1 / points.get(i).getDist();
        }
        return over / under;
    }
}
