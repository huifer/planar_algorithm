package com.huifer.planar.aset.entity;

import com.huifer.planar.aset.algo.MyRectangleOver;
import com.huifer.planar.aset.algo.impl.polygon.MyRecatngleOverCore;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>Title : MyRectangle </p>
 * <p>Description : 矩形</p>
 *
 * @author huifer
 * @date 2018/12/20
 */
@Data
@ToString
@NoArgsConstructor
public class MyRectangle implements Comparable<MyRectangle>, Serializable {


    private MyRectangleOver over = new MyRecatngleOverCore();
    /**
     * 左上角
     */
    private MyPoint topLeft;
    /**
     * 左下角
     */
    private MyPoint lowLeft;

    /**
     * 右上角
     */
    private MyPoint topRight;
    /**
     * 右下角
     */
    private MyPoint lowRight;

    /**
     * 左侧线
     */
    private MyLine leftLine;
    /**
     * 右侧线
     */
    private MyLine rightLine;
    /**
     * 上侧线
     */
    private MyLine topLine;
    /**
     * 下侧线
     */
    private MyLine lowLine;
    /**
     * 长度
     */
    private double length;
    /**
     * 宽度
     */
    private double width;


    public MyRectangle(MyPoint lowLeft, double length, double width) {
        this.lowLeft = lowLeft;
        this.length = length;
        this.width = width;
        calcAllPoint(lowLeft, length, width);
    }

    @Override
    public int compareTo(MyRectangle o) {
        return Double.compare(this.getArea(), o.getArea());
    }

    public double getArea() {
        return length * width;
    }

    public double calcAreaIntersectionRectangles(MyRectangle r2) {
        double v = over.calcAreaIntersectionRectangles(this, r2);
        return v;
    }

    private void calcAllPoint(MyPoint lowLeft, double length, double width) {
        this.lowRight = new MyPoint(lowLeft.getX() + length, lowLeft.getY());
        this.topLeft = new MyPoint(lowLeft.getX(), lowLeft.getY() + width);
        this.topRight = new MyPoint(lowLeft.getX() + length, lowLeft.getY() + width);
        this.leftLine = new MyLine(lowLeft, topLeft);
        this.rightLine = new MyLine(lowRight, topRight);
        this.topLine = new MyLine(topLeft, topRight);
        this.lowLine = new MyLine(lowLeft, lowRight);
    }


}
