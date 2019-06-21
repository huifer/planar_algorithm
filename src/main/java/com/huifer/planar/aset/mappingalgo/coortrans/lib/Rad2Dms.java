package com.huifer.planar.aset.mappingalgo.coortrans.lib;

/**
 * <p>Title : Rad2Dms </p>
 * <p>Description : rad dms 互相转换</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
public class Rad2Dms {

    /***
     * rad 转 dms
     * @param rad
     * @return double
     */
    public static double rad2dms(double rad) {
        double dms = 0;
        double sec = 0;
        int deg = 0;
        int minu = 0;
        int sign = 1;
        if (rad < 0) {
            sign = -1;
            rad = -rad;
        }
        dms = rad / Math.PI * 180;
        deg = (int) Math.floor(dms);
        minu = (int) Math.floor((dms - deg) * 60.0);

        sec = (dms - deg - minu / 60.0) * 3600.0;
        dms = deg + minu / 100.0 + sec / 10000.0;
        dms = sign * dms;

        return dms;
    }


    /***
     * dms 转 rad
     * @param dms
     * @return double
     */
    public static double dms2rad(double dms) {
        int sign = 1;
        double rad = 0;
        double sec = 0;
        int deg = 0;
        int minu = 0;
        if (dms < 0) {
            sign = -1;
            dms = -dms;
        }
        deg = (int) Math.floor(dms);
        minu = (int) Math.floor((dms - deg) * 100.0);

        sec = (dms - deg - minu / 100.0) * 10000;
        rad = deg + minu / 60.0 + sec / 3600.0;
        rad = rad / 180.0 * Math.PI;
        rad = rad * sign;
        return rad;
    }

    /**
     * rad 转 dms 字符串
     *
     * @return str
     */
    public static String rad2Str(double rad) {
        String res = "";
        double d = rad / Math.PI * 180;
        String sign = "";
        if (d < 0) {
            sign = "-";
        }
        d = Math.abs(d);
        double dd, mm, ss;
        dd = Math.floor(d);
        mm = Math.floor((d - dd) * 60.0);
        ss = (d - dd - mm / 60.0) * 3600.0;
        res = sign + dd + "°" + mm + "′" + ss + "″";
        return res;
    }


}
