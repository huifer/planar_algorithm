package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : HeightCal </p>
 * <p>Description : 高度计算工具</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class HeightCal {

    /**
     * 点集
     */
    List<PointInfo> points;
    /***
     * dwh列表
     */
    List<Dwh> dwhs;
    /***
     * 当前点
     */
    PointInfo p;

    public HeightCal(List<PointInfo> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "HeightCal{" +
                "points=" + points +
                ", dwhs=" + dwhs +
                ", p=" + p +
                '}';
    }

    public double getHeight(PointInfo p) {
        this.p = p;
        double height = 0;
        dwhs = new ArrayList<>();
        calcDistance();
        height = calcHeight();
        return height;
    }

    /***
     * P 点内插高程
     * @return
     */
    private double calcHeight() {
        double sumWeight = 0;
        double sumHeight = 0;
        for (int i = 0; i < dwhs.size(); i++) {
            Dwh dwh = dwhs.get(i);
            sumHeight += dwh.getHeight() * dwh.getWeight();
            sumWeight += dwh.getWeight();
        }
        return sumHeight / sumWeight;
    }


    /***
     * 算距离
     * 计算p 到每一个点的距离
     */
    private void calcDistance() {
        double dx = 0;
        double dy = 0;
        double dist = 0;
        Dwh dwh;
        for (int i = 0; i < points.size(); i++) {
            dwh = new Dwh();
            PointInfo tem = points.get(i);

            dx = tem.xx - p.xx;
            dy = tem.yy - p.yy;
            dist = Math.sqrt(dx * dx + dy * dy);
            dwh.setDistance(dist);
            dwh.setWeight(1.0 / dist);
            dwh.setHeight(tem.hh);
//            dwh = new Dwh(dist, 1 / dist, tem.h);
            dwhs.add(dwh);
        }
    }


}
