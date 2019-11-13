package com.huifer.planar.aset.utils.shptools.polygontools.geometry;


import com.huifer.planar.aset.utils.shptools.polygontools.entity.LatLngEntity;

import java.util.Comparator;


/**
 * LatLngEntity对象比较类，实现Comparator接口
 *
 * @author huifer
 */

public class ComparatorLatLng implements Comparator<Object> {

    @Override
    public int compare(Object arg0, Object arg1) {

        LatLngEntity latlng0 = (LatLngEntity) arg0;
        LatLngEntity latlng1 = (LatLngEntity) arg1;

//		首先比较lat，如果lat相同，再比较lng
        int flag = Double.toString(latlng0.getLat()).compareTo(Double.toString(latlng1.getLat()));

        if (flag == 0) {
            return Double.toString(latlng0.getLng()).compareTo(Double.toString(latlng1.getLng()));
        } else {
            return flag;
        }

    }

}

