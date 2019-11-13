package com.huifer.planar.aset.view.base;

import org.locationtech.jts.io.ParseException;

import java.awt.*;

/**
 * <p>Title : Aframe </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public interface Aframe {

    /**
     * 绘图方法
     *
     * @param g Graphics
     * @throws ParseException 异常
     */
    void drawing(Graphics g) throws ParseException;
}
