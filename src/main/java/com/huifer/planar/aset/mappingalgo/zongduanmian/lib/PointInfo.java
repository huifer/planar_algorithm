package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

import java.util.Objects;

/**
 * <p>Title : PointInfo </p>
 * <p>Description : 离散点</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class PointInfo {

    public String name;
    public double xx;

    public double yy;
    public double hh;
    public double k;
    public String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PointInfo pointInfo = (PointInfo) o;
        return Double.compare(pointInfo.xx, xx) == 0 &&
                Double.compare(pointInfo.yy, yy) == 0 &&
                Double.compare(pointInfo.hh, hh) == 0 &&
                Double.compare(pointInfo.k, k) == 0 &&
                Objects.equals(name, pointInfo.name) &&
                Objects.equals(tag, pointInfo.tag);
    }

    @Override
    public String toString() {
        return "PointInfo{" +
                "name='" + name + '\'' +
                ", x=" + xx +
                ", y=" + yy +
                ", h=" + hh +
                ", k=" + k +
                ", tag='" + tag + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, xx, yy, hh, k, tag);
    }
}
