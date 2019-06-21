package com.huifer.planar.aset.utils.geo;

import java.awt.Color;

/**
 * <p>Title : ColorUtil </p>
 * <p>Description : 颜色转换器</p>
 *
 * @author huifer
 * @date 2019-01-18
 */
public class ColorUtil {

    /**
     * color 转换 16进制颜色字符串
     *
     * @param color 颜色
     * @return 16进制颜色字符串
     */
    public static String colorToHexColor(Color color) {
        String r, b, g;
        StringBuilder hexColor = new StringBuilder();
        r = Integer.toHexString(color.getRed());
        g = Integer.toHexString(color.getGreen());
        b = Integer.toHexString(color.getBlue());
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        hexColor.append("0x");
        hexColor.append(r);
        hexColor.append(g);
        hexColor.append(b);
        return hexColor.toString();
    }

    /**
     * 16进制颜色字符串 转换 color
     *
     * @param hexColor 16进制颜色字符串
     * @return 颜色
     */
    public static Color hexColorToColor(String hexColor) {
        if (hexColor.contains("0x") || hexColor.contains("0X")) {
            hexColor = hexColor.substring(2);
        } else if (hexColor.contains("#")) {
            hexColor = hexColor.substring(1);
        }
        Color color = new Color(Integer.parseInt(hexColor, 16));
        return color;
    }


}
