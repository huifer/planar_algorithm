package com.huifer.planar.aset.utils.shptools.polygontools.geometry;


import com.huifer.planar.aset.utils.shptools.polygontools.entity.LatLngEntity;

/**
 * Bounds 构建由最大和最小经纬度组成的矩形区域
 *
 * @author huifer
 */
public class Bounds {

    private LatLngEntity minlatlngEntity;
    private LatLngEntity maxlatlngEntity;

    public Bounds(LatLngEntity minlatlng, LatLngEntity maxlatlng) {
        minlatlngEntity = minlatlng;
        maxlatlngEntity = maxlatlng;
    }

    /**
     * 判断经纬度是否在该矩形区域
     */
    public boolean contains(LatLngEntity latlng) {
        boolean status = true;
        if (latlng.getLat() > maxlatlngEntity.getLat() ||
                latlng.getLng() > maxlatlngEntity.getLng() ||
                latlng.getLat() < minlatlngEntity.getLat() ||
                latlng.getLng() < minlatlngEntity.getLng()) {
            status = false;
        }
        return status;
    }

    @Override
    public String toString() {
        return "最小经纬度 : " + minlatlngEntity.getLat() + " , " + minlatlngEntity.getLng() +
                "最大经纬度 : " + maxlatlngEntity.getLat() + " , " + maxlatlngEntity.getLng()
                ;
    }
}
