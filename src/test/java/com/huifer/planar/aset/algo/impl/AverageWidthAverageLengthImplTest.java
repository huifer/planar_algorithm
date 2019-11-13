package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.AverageWidthAverageLength;
import org.junit.Test;

public class AverageWidthAverageLengthImplTest {


    private AverageWidthAverageLength averageWidthAverageLength = new AverageWidthAverageLengthImpl();

    @Test
    public void calcAverageWidth() {
        String polygon = "POLYGON((0 0, 10 20, 20 20, 10 0, 0 0))";
        double v = averageWidthAverageLength.calcAverageWidth(10, polygon);

    }

    @Test
    public void calcAverageLength() {
        String polygon = "POLYGON((0 0, 10 20, 20 20, 10 0, 0 0))";
        double v = averageWidthAverageLength.calcAverageLength(10, polygon);
    }


}
