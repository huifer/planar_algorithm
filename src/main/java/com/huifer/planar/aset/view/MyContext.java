package com.huifer.planar.aset.view;

import com.huifer.planar.aset.view.base.BaseFrame;
import com.huifer.planar.aset.view.base.FrameContext;
import com.huifer.planar.aset.view.base.ViewHelper;

import java.awt.*;
import java.util.Random;

/**
 * <p>Title : MyContext </p>
 * <p>Description : 测试显示</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
public class MyContext extends FrameContext {

    public static void main(String[] args) {
        MyContext myContext = new MyContext();
        BaseFrame frame = new BaseFrame(myContext);
        frame.run();
    }


    @Override
    public void drawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        ViewHelper.isRenderingHints(g2d, true);
        ViewHelper.setColor(g2d, ViewHelper.BLUE);
        ViewHelper.drawText(g2d, "aaaaaaaaaaaaaaa", 0, 50);
        ViewHelper.setColor(g2d, ViewHelper.TEAL);
        ViewHelper.strokeCircle(g2d, 10, 10, 10);

        ViewHelper.setColor(g2d, ViewHelper.RED);
        for (int i = 0; i < 2000; i++) {
            Random rand = new Random();
            double rx = rand.nextInt(1024) + 0;
            double ry = rand.nextInt(768) + 0;
            ViewHelper.drawPoint(g2d, rx, ry);
        }

        ViewHelper.setColor(g2d, ViewHelper.AMBER);
        ViewHelper.drawLine(g2d, 110.2, 200, 300.3, 400.3);
        ViewHelper.setColor(g2d, ViewHelper.DEEPPURPLE);
        ViewHelper.fillCircle(g2d, 20.3, 55.5, 30);
        ViewHelper.setColor(g2d, ViewHelper.ORANGE);
        ViewHelper.strokeRectangle(g2d, 30, 20, 500, 600);
        ViewHelper.setColor(g2d, ViewHelper.GREEN);
        ViewHelper.fillRectangle(g2d, 1024 - 30, 20, 500, 600);

        ViewHelper.putImage(g2d, 30, 20, "C:\\Users\\wangtao\\Pictures\\go128.png");
        ViewHelper.setColor(g2d, ViewHelper.LIGHTBLUE);
        double[] x = new double[]{100, 30, 50, 1020.8, 19.6, 555.0};
        double[] y = new double[]{90.3, 55.9, 50, 55.6, 19.6, 555.0};
        ViewHelper.drawPolygon(g2d, x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        drawing(g);
    }
}
