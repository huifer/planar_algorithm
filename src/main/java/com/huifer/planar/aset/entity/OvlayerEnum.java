package com.huifer.planar.aset.entity;

import lombok.ToString;

/**
 * 点线关系判断
 *
 * @author huifer
 */
@ToString
public enum OvlayerEnum {
    /**
     * 在线上
     */
    ON_LINE("在线上", 1),
    /**
     * 在线段延长线上
     */
    ON_THE_EXTENSION_LINE("在线段延长线上", 2),
    /**
     * 在线段右侧
     */
    ON_THE_RIGHT_OF_THE_LINE("在线段右侧", 3),
    /**
     * 在线段左侧
     */
    ON_THE_LEFT_OF_THE_LINE("在线段左侧", 4),

    /**
     * 在多边形线上
     */
    ON_THE_POLYGON_LINE("在多边形线上", 1),
    /**
     * 在多边形内
     */
    INSIDE_THE_POLYGON("在多边形内", 2),
    /**
     * 在多边形外
     */
    OUTSIDE_THE_POLYGON("在多边形外", 0),
    ;

    /**
     * 情况
     */
    private String name;
    /**
     * 编号
     */
    private int code;

    OvlayerEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
