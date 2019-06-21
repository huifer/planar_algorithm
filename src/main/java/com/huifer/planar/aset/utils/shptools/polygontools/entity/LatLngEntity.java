package com.huifer.planar.aset.utils.shptools.polygontools.entity;


/**
 * 经纬度
 * @author huifer
 */
public class LatLngEntity {

	private double lat;
	private double lng;

	public LatLngEntity (double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public LatLngEntity() {
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public boolean equals(Object obj) {
		if(this==obj){
            return true ;
        }
        if(!(obj instanceof LatLngEntity)){
            return false ;
        }
        LatLngEntity latlngInfo = (LatLngEntity)obj ;
        return this.lat == latlngInfo.lat &&
        		this.lng == latlngInfo.lng;
	}

	 @Override
	public int hashCode() {
		return (int) (this.lat*this.lng);
	}

}
