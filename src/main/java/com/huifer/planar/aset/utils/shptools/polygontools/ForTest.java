package com.huifer.planar.aset.utils.shptools.polygontools;


import com.huifer.planar.aset.utils.shptools.polygontools.entity.LatLngEntity;
import com.huifer.planar.aset.utils.shptools.polygontools.geometry.ConvexHull;
import com.huifer.planar.aset.utils.shptools.polygontools.geometry.Polygon;
import java.util.ArrayList;


/**
 * ForTest 测试类
 * @author huifer
 */
public class ForTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConvexHull convexHull = new ConvexHull();

        ArrayList<LatLngEntity> list = new ArrayList<LatLngEntity>();
        list.add(new LatLngEntity(0.0, 1.0));
        list.add(new LatLngEntity(0.0, 0.0));
        list.add(new LatLngEntity(1.0, 1.0));
        list.add(new LatLngEntity(2.0, 0.0));
        list.add(new LatLngEntity(1.0, 2.0));
        list.add(new LatLngEntity(1.0, 2.0));
        try {
            //凸包多边形
            ArrayList<LatLngEntity> convexHullPts = convexHull.getConvexHull(list);
//            for (LatLngEntity geo : convexHullPts) {
//                System.out.println(geo.getLng() + " , " + geo.getLat());
//            }

            Polygon poly = new Polygon(convexHullPts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
