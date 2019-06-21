package com.huifer.planar.aset.mappingalgo.coortrans.lib;

/**
 * <p>Title : Position </p>
 * <p>Description : 三维坐标转换</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class Position {

    private Ellipsoid ellipsoid;

    public Position(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }


    /***
     * 大地测量转换为笛卡尔坐标
     * @param lat 纬度
     * @param lon 经度
     * @param height 高度
     * @return 笛卡尔坐标系 xyz
     */
    public double[] geodetic2Cartesian(double lat, double lon, double height) {
        double x, y, z;

        double n = ellipsoid.n(lat);
        double slat = Math.sin(lat);
        double clat = Math.cos(lat);
        x = (n + height) * clat * Math.cos(lon);
        y = (n + height) * clat * Math.sin(lon);
        z = (n * (1 - ellipsoid.eccSq) + height) * slat;
        return new double[]{x, y, z};
    }

    /**
     * 笛卡尔坐标系转换 大地坐标系
     * @param x x
     * @param y y
     * @param z z
     * @return 大地坐标系值
     */
    public double[] cartesian2Geodetic(double x, double y, double z) {
        double lat = 0;
        double lon = 0;
        double height = 0;
        double positionTolerance = 0.0001;

        double p, slat, n, htold, latold;

        p = Math.sqrt(x * x + y * y);
        if (p < positionTolerance) {
            lat = (z > 0 ? 90.0 : -90.0);
            lon = 0;
            height = Math.abs(z - ellipsoid.a * Math.sqrt(1 - ellipsoid.eccSq));
        } else {
            lat = Math.atan2(z, p);
            height = 0;
            for (int i = 0; i < 5; i++) {
                slat = Math.sin(lat);
                n = ellipsoid.n(lat);
                htold = height;
                height = p / Math.cos(lat) - n;
                latold = lat;
                lat = Math.atan2(z + n * ellipsoid.eccSq * slat, p);
                if (Math.abs(lat - latold) < 1.0e-9
                        && Math.abs(height - htold) < 1.0e-9 * ellipsoid.a) {
                    break;
                }
                lon = Math.atan2(y, x);
                {
                    if (lon < 0.0) {
                        lon += 2 * Math.PI;
                    }
                }
            }
        }
        return new double[]{lat, lon, height};
    }


}
