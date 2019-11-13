package com.huifer.planar.aset.view.base;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * <p>Title : ViewHelper </p>
 * <p>Description : ViewHelper</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public class ViewHelper {

    public static final Color RED = new Color(0xf44336);
    public static final Color PINK = new Color(0xE91E63);
    public static final Color PURPLE = new Color(0x9C27B0);
    public static final Color DEEPPURPLE = new Color(0x673AB7);
    public static final Color INDIGO = new Color(0x3F51B5);
    public static final Color BLUE = new Color(0x2196F3);
    public static final Color LIGHTBLUE = new Color(0x03A9F4);
    public static final Color CYAN = new Color(0x00BCD4);
    public static final Color TEAL = new Color(0x009688);
    public static final Color GREEN = new Color(0x4CAF50);
    public static final Color LIGHTGREEN = new Color(0x8BC34A);
    public static final Color LIME = new Color(0xCDDC39);
    public static final Color YELLOW = new Color(0xFFEB3B);
    public static final Color AMBER = new Color(0xFFC107);
    public static final Color ORANGE = new Color(0xFF9800);
    public static final Color DEEPORANGE = new Color(0xFF5722);
    public static final Color BROWN = new Color(0x795548);
    public static final Color GREY = new Color(0x9E9E9E);
    public static final Color BLUEGREY = new Color(0x607D8B);
    public static final Color BLACK = new Color(0x000000);
    public static final Color WHITE = new Color(0xFFFFFF);


    private ViewHelper() {
    }

    /***
     * 画面
     * @param g
     * @param x
     * @param y
     */
    public static void drawPolygon(Graphics2D g, double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(" x坐标组和y坐标组长度不相等");
        }
        Path2D path = new Path2D.Double();

        path.moveTo(x[0], y[0]);
        for (int i = 1; i < x.length; i++) {
            path.lineTo(x[i], y[i]);
        }
        path.closePath();
        g.draw(path);
    }

    public static void drawPolygon(Graphics2D g, ArrayList<Double> xList, ArrayList<Double> yList) {
        if (xList.size() != yList.size()) {
            throw new IllegalArgumentException(" x坐标组和y坐标组长度不相等");
        }
        Path2D path = new Path2D.Double();

        path.moveTo(xList.get(0), yList.get(0));

        for (int i = 1; i < xList.size(); i++) {
            path.lineTo(xList.get(i), yList.get(i));
        }
        path.closePath();
        g.draw(path);
    }

    /**
     * 画点
     *
     * @param g Graphics2D
     * @param x x
     * @param y y
     */
    public static void drawPoint(Graphics2D g, double x, double y) {
        Line2D lin = new Line2D.Double(x, y, x, y);
        g.draw(lin);
    }

    /**
     * 画线
     *
     * @param g  Graphics2D
     * @param x1 x1
     * @param y1 y1
     * @param x2 x2
     * @param y2 y2
     */
    public static void drawLine(Graphics2D g, double x1, double y1, double x2, double y2) {
        Line2D lin = new Line2D.Double(x1, y1, x2, y2);
        g.draw(lin);
    }

    /**
     * 绘空心制圆
     *
     * @param g Graphics2D
     * @param x x
     * @param y y
     * @param r 半径
     */
    public static void strokeCircle(Graphics2D g, double x, double y, double r) {

        Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g.draw(circle);
    }

    /**
     * 绘制填充圆
     *
     * @param g Graphics2D
     * @param x x
     * @param y y
     * @param r 半径
     */
    public static void fillCircle(Graphics2D g, double x, double y, double r) {

        Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g.fill(circle);
    }

    /**
     * 绘制空心矩形
     *
     * @param g Graphics2D
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     */
    public static void strokeRectangle(Graphics2D g, double x, double y, double w, double h) {

        Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
        g.draw(rectangle);
    }

    /**
     * 绘制实心矩形
     *
     * @param g Graphics2D
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     */
    public static void fillRectangle(Graphics2D g, double x, double y, double w, double h) {
        Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
        g.fill(rectangle);
    }

    /**
     * 设置颜色
     *
     * @param g     Graphics2D
     * @param color 颜色
     */
    public static void setColor(Graphics2D g, Color color) {
        g.setColor(color);
    }

    /**
     * 设置笔触
     *
     * @param g Graphics2D
     * @param w w
     */
    public static void setStrokeWidth(Graphics2D g, int w) {
        int strokeWidth = w;
        g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /**
     * 显示图
     *
     * @param g        Graphics2D
     * @param x        x
     * @param y        y
     * @param imageURL 图片地址
     */
    public static void putImage(Graphics2D g, int x, int y, String imageURL) {

        ImageIcon icon = new ImageIcon(imageURL);
        Image image = icon.getImage();

        g.drawImage(image, x, y, null);
    }

    /**
     * 显示文字
     *
     * @param g       Graphics2D
     * @param text    文字
     * @param centerx centerx
     * @param centery centery
     */
    public static void drawText(Graphics2D g, String text, int centerx, int centery) {

        if (text == null) {
            throw new IllegalArgumentException("Text is null in drawText function!");
        }
        g.drawString(text, centerx, centery);
    }

    /***
     * 抗锯齿
     * @param g Graphics2D
     * @param i boolean
     */
    public static void isRenderingHints(Graphics2D g, boolean i) {
        if (i) {
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.addRenderingHints(hints);
        }
    }

    public static ArrayList<Color> getColorList() {
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(RED);
        colors.add(PINK);
        colors.add(BROWN);
        colors.add(DEEPPURPLE);
        colors.add(TEAL);
        colors.add(INDIGO);
        colors.add(BLUE);
        colors.add(LIGHTBLUE);
        colors.add(CYAN);
        colors.add(GREEN);
        colors.add(PURPLE);
        colors.add(LIGHTGREEN);
        colors.add(LIME);
        colors.add(YELLOW);
        colors.add(AMBER);
        colors.add(ORANGE);
        colors.add(DEEPORANGE);
        colors.add(GREY);
        colors.add(BLUEGREY);
        colors.add(BLACK);
        colors.add(WHITE);
        return colors;
    }
}
