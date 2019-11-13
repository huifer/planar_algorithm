package com.huifer.planar.aset.utils;

import com.huifer.planar.aset.entity.Kmeans;
import org.junit.Test;

public class KmeansTest {
    @Test
    public void KmeanRunTest() {
        int n = 20;
        double[][] points = new double[n][2];
        double[] a_1 = new double[]{1, 3};
        double[] a_2 = new double[]{7, 7};
        double[] a_3 = new double[]{4, 4};
        double[] a_4 = new double[]{3, 8};
        double[] a_5 = new double[]{4, 8};
        double[] a_6 = new double[]{8, 6};
        double[] a_7 = new double[]{8, 9};
        double[] a_8 = new double[]{3, 9};
        double[] a_9 = new double[]{2, 7};
        double[] a_10 = new double[]{6, 5};
        double[] a_11 = new double[]{7, 5};
        double[] a_12 = new double[]{3, 1};
        double[] a_13 = new double[]{5, 2};
        double[] a_14 = new double[]{3, 3};
        double[] a_15 = new double[]{8, 9};
        double[] a_16 = new double[]{3, 3};
        double[] a_17 = new double[]{9, 6};
        double[] a_18 = new double[]{5, 2};
        double[] a_19 = new double[]{6, 4};
        double[] a_20 = new double[]{9, 2};
        points[0] = a_1;
        points[1] = a_2;
        points[2] = a_3;
        points[3] = a_4;
        points[4] = a_5;
        points[5] = a_6;
        points[6] = a_7;
        points[7] = a_8;
        points[8] = a_9;
        points[9] = a_10;
        points[10] = a_11;
        points[11] = a_12;
        points[12] = a_13;
        points[13] = a_14;
        points[14] = a_15;
        points[15] = a_16;
        points[16] = a_17;
        points[17] = a_18;
        points[18] = a_19;
        points[19] = a_20;

        int k = 3;
        Kmeans kmeans = new Kmeans(points, k);

        double[][] centroids = kmeans.getCentroids();
        System.out.println();
//        int[] init = kmeans.init();
//        List<Integer> list1 = Arrays.stream(init).boxed().collect(Collectors.toList());
//        System.out.println(list1);
    }

}
