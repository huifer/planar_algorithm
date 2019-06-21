package com.huifer.planar.aset.utils.shptools.polygontools;

import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : PolygonGravityCenter </p>
 * <p>Description : 多边形重心计算</p>
 *
 * @author huifer
 * @date 2018/10/8
 */
@Slf4j
public class PolygonGravityCenter {
    //1)三角形重心
    // 顶点为a，b，c的三角形重心为x = (xa + xb + xc) / 3，y = (ya + yb + yc) / 3
    //2)多边形重心
    // w 质量
    // x = (x1*w1 + x2*w2 + ... + xn*wn)/w
    //y = (y1*w1 + y2*w2 + ... + yn*wn)/w

    /**
     * 排列长度
     */
    private int num = 3;
    private List resultP = new ArrayList();


    public static void main(String[] args) {

        double[] aa = new double[]{81.008, 25.448};
        double[] bb = new double[]{76.008, 20.448};
        double[] cc = new double[]{86.008, 20.448};

        double[] doubles = triangleCenterOfGravity(aa, bb, cc);

//        System.out.println(doubles[0]);
//        System.out.println(doubles[1]);
        log.info("{}", doubles[0]);
        log.info("{}", doubles[1]);

        double[] a = new double[]{0, 0};
        double[] b = new double[]{10, 0};
        double[] c = new double[]{10, 10};
        double[] d = new double[]{20, 0};

        Map<String, double[]> point = new HashMap();
        point.put("a", a);
        point.put("b", b);
        point.put("c", c);
        point.put("d", d);
        // 所有点排列
        List abd = point.keySet().stream().collect(Collectors.toList());

        PolygonGravityCenter polygonGravityCenter = new PolygonGravityCenter();

        polygonGravityCenter.sort(abd, new ArrayList());

        double xArea = 0.0;
        double yArea = 0.0;
        double aArea = 0.0;
        for (Object oneDataObj : polygonGravityCenter.resultP) {
            List oneDataList = (List) oneDataObj;
//            System.out.println(oneDataList);
//            System.out.println(
//                    point.get(oneDataList.get(0))[0] + " - " + point.get(oneDataList.get(1))[0]
//                            + " - " + point.get(oneDataList.get(2))[0]);

            double[] oneGR = triangleCenterOfGravity(point.get(oneDataList.get(0)),
                    point.get(oneDataList.get(1)), point.get(oneDataList.get(2)));
            double area = getArea(
                    point.get(oneDataList.get(0))[0], point.get(oneDataList.get(0))[1],
                    point.get(oneDataList.get(1))[0], point.get(oneDataList.get(1))[1],
                    point.get(oneDataList.get(2))[0], point.get(oneDataList.get(2))[1]);

            xArea += oneGR[0] * area;
            yArea += oneGR[1] * area;
            aArea += area;

        }

//        System.out.println("重心X" + xArea / aArea);
//        System.out.println("重心Y" + yArea / aArea);
        log.info("重心X = {} , 重心Y = {}", xArea / aArea, yArea / aArea);

//        double[] list1 = triangleCenterOfGravity(a, b, c);
//        double[] list2 = triangleCenterOfGravity(a, d, c);
//        double[] list3 = triangleCenterOfGravity(c, b, d);
//
//        double area1 = getArea(a[0], a[1], b[0], b[1], c[0], c[1]);
//        System.out.println(area1);
//        double area2 = getArea(a[0], a[1], d[0], d[1], c[0], c[1]);
//        System.out.println(area2);
//        double area3 = getArea(c[0], c[1], b[0], b[1], d[0], d[1]);
//        System.out.println(area3);
//        System.out.println("=========");
//        double x = (list1[0] * area1 + list2[0] * area2 + list3[0] * area3) / (area1 + area2 + area3);
//        double y = (list1[1] * area1 + list2[1] * area2 + list3[1] * area3) / (area1 + area2 + area3);
//        System.out.println(x);
//        System.out.println(y);
//
//
//        double[] doubles = triangleCenterOfGravity(a, d, c);
//        System.out.println(doubles[0] + " , " + doubles[1]);
    }

    /***
     * 计算三角形面积
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getArea(double x1, double y1, double x2, double y2, double x3, double y3) {

        return Math.abs(Math.abs(y1 - y3) * Math.abs(x2 - x1) / 2
                + Math.abs(x3 - x1) * Math.abs(y2 - y3) / 2
                - Math.abs(x1 - x3) * Math.abs(y1 - y3) / 2);
    }

    /***
     * 三角形重心计算
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static double[] triangleCenterOfGravity(double[] a, double[] b, double[] c) {

        double gravityX = (a[0] + b[0] + c[0]) / 3;
        double gravityY = (a[1] + b[1] + c[1]) / 3;
        double[] result = new double[]{gravityX, gravityY};
        return result;

    }

    /**
     * 全排列
     */
    private void sort(List datas, List target) {

        if (target.size() == this.num) {
            this.resultP.add(target);
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            List newDatas = new ArrayList(datas);
            List newTarget = new ArrayList(target);
            newTarget.add(newDatas.get(i));
            newDatas.remove(i);
            sort(newDatas, newTarget);
        }
    }


}
