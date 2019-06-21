package com.huifer.planar.aset.utils.shptools.triangulation;


/***
 * @author huifer
 */
public enum Triangle2DEnum {
    /**
     * 一条虚拟边
     */
    T("ConvexTest"),
    /**
     * 2条虚拟边
     */
    M("m"),
    /**
     * 3条虚拟边
     */
    E("E"),
    ;
    private String type;

    Triangle2DEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Triangle2DEnum{" +
                "type='" + type + '\'' +
                '}';
    }

}
