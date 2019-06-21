package com.huifer.planar.aset.utils.shptools.center;


import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import com.huifer.planar.aset.utils.shptools.triangulation.DelaunayTriangulator;
import com.huifer.planar.aset.utils.shptools.triangulation.Triangle2D;
import com.huifer.planar.aset.utils.shptools.triangulation.Triangle2DEnum;
import com.huifer.planar.aset.utils.shptools.triangulation.Triangle2dTools;
import com.huifer.planar.aset.utils.shptools.triangulation.Vector2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : Run </p>
 * <p>Description : 中心测试</p>
 *
 * @author huifer
 * @date 2018/10/16
 */
public class Run {

    public static void main(String[] args) throws Exception {

//        double[] a = new double[]{0, 0};
//        double[] b = new double[]{60, 0};
//        double[] c = new double[]{30, 51.9615};
//
//        double[] neixin = neixin(a, b, c);


        Operation op = new Operation();
        MultiPolygon mulPolygonByWKT = op.createMulPolygonByWKT("MULTIPOLYGON (((132.35322761535645 34.827157974243164, 138.73223686218262 23.707780838012695, 143.17999076843262 28.623716354370117, 145.05272483825684 20.547536849975586, 154.416410446167 22.771413803100586, 155.93800926208496 12.998064041137695, 147.56921577453613 4.746316909790039, 141.4242992401123 9.545206069946289, 141.65839195251465 15.865694046020508, 135.86461067199707 13.40772819519043, 129.07593727111816 26.10722541809082, 120.1804370880127 25.99017906188965, 125.97421836853027 33.832265853881836, 132.35322761535645 34.827157974243164)))");

        Coordinate[] coordinates = mulPolygonByWKT.getCoordinates();
        List<Vector2D> pointSet = new ArrayList<>();
        Arrays.stream(coordinates).forEach(
                s -> {
                    pointSet.add(new Vector2D(s.x, s.y));
                }
        );


        DelaunayTriangulator delaunayTriangulator = new DelaunayTriangulator(pointSet);

        delaunayTriangulator.triangulate();

        List<Triangle2D> triangles = delaunayTriangulator.getTriangles();

        // list x
        List<Double> polygonX = new ArrayList<>();
        // list y
        List<Double> polygonY = new ArrayList<>();
        pointSet.forEach(
                s -> {
                    polygonX.add(s.x);
                    polygonY.add(s.y);
                }
        );


        // 一条虚拟边的点中心点
        List<Vector2D> v1CenterPoint = new ArrayList<>();
        // 二条虚拟边的点中心点
        List<Vector2D> v2CenterPoint = new ArrayList<>();
        // 三条虚拟边的点中心点
        List<Vector2D> v3CenterPoint = new ArrayList<>();
        // 一条虚拟边的内链线
        List<LineString> v1CenterLine = new ArrayList<>();
        // 二条虚拟边的内链线
        List<LineString> v2CenterLine = new ArrayList<>();
        // 三条虚拟边的内链线
        List<LineString> v3CenterLine = new ArrayList<>();
        // 所有三角形
        List<Polygon> vaTriangle = new ArrayList<>();
        // 耳朵边缘 内部线数量==1
        List<LineString> oe = new ArrayList<>();
        // 内部边缘 内部线数量==3
        List<LineString> le = new ArrayList<>();
        // 3.1 Length
        List<Double> oeLength = new ArrayList<Double>();
        //3.2 area
        List<Double> oeArea = new ArrayList<Double>();
        //3.3  Angularity
        List<Vector2D> angularityHelp = new ArrayList<>();
        // 图形的最小二乘法
        Regression reg = new Regression(polygonX, polygonY);
        double a1 = reg.beta1;
        // GEE
        List<Vector2D> gee = new ArrayList<>();
        // Si
        List<Double> si = new ArrayList<>();
        // woi list
        List<Double> woilist = new ArrayList<>();


        double xArea = 0.0;
        double yArea = 0.0;
        double aArea = 0.0;


        for (Triangle2D triangle2D : triangles) {
            Triangle2dTools triangle2DTools = new Triangle2dTools(triangle2D);
            Polygon triangle = triangle2DTools.triangle;

            if (triangle.within(mulPolygonByWKT)) {
                xArea += triangle2DTools.gravity.x * triangle2DTools.area;
                yArea += triangle2DTools.gravity.y * triangle2DTools.area;
                aArea += triangle2DTools.area;

                int i = triangle2DTools.counterLine(mulPolygonByWKT);

                Triangle2DEnum a = triangle2DTools.type;
//                System.out.println(i);
//                System.out.println("虚拟线"+triangle2DTools.virtualLine);
//                System.out.println("虚拟线中间点"+triangle2DTools.virtualLineCenter);

//                System.out.println(i);
//                triangle2DTools.virtualLineCenter.forEach(
//                        s -> System.out.println(s)
//                );
//
//                System.out.println(triangle);
//                System.out.println("==========");


                vaTriangle.add(triangle);


//                System.out.println( "耳朵 、 内判断 \t"+ triangle2DTools.vsl.size());

                angularityHelp.addAll(triangle2DTools.virtualLineCenter);

                // Oe Le
                if (triangle2DTools.vsl.size() == 3) {
                    le.addAll(triangle2DTools.vsl);
                } else if (triangle2DTools.vsl.size() == 1) {
                    oe.addAll(triangle2DTools.vsl);
                    oeArea.add(triangle2DTools.area);
                    oeLength.add(triangle2DTools.vsl.get(0).getLength());
                }


//                 内部中心点 + 内部连接线
                if (i == 1) {
                    triangle2DTools.virtualLineCenter.forEach(
                            vOnePoint -> v1CenterPoint.add(vOnePoint)
                    );
                    triangle2DTools.vsl.forEach(
                            vOneLine -> {
//                                System.out.println(asdadasdas);
                                v2CenterLine.add(vOneLine);
                            }
                    );

                } else if (i == 2) {
                    triangle2DTools.virtualLineCenter.forEach(
                            vTwoPoint -> v2CenterPoint.add(vTwoPoint)
                    );
                    triangle2DTools.vsl.forEach(
                            vTwoLine -> {
//                                System.out.println(vTwoLine)
                                v2CenterLine.add(vTwoLine);
                            }
                    );

                } else if (i == 3) {
                    triangle2DTools.virtualLineCenter.forEach(
                            vThreePoint -> v3CenterPoint.add(vThreePoint)
                    );

                    triangle2DTools.vsl.forEach(
                            vThreeLine -> {
//                                System.out.println(asdasd);
                                v3CenterLine.add(vThreeLine);
                            }
                    );
                    gee.add(triangle2DTools.neixin);
                }

            }
        }


        //  v3CenterLine 每一条边 与最小二乘法求出的直线进行角度判断


//        ///////////////////////
//        System.out.println("===V1P===");
//        v1CenterPoint.forEach(s -> System.out.println(s));
//        System.out.println("===V2P===");
//        v2CenterPoint.forEach(s -> System.out.println(s));
//        System.out.println("===V3P===");
//        v3CenterPoint.forEach(s -> System.out.println(s));
//        System.out.println("======================================");
//
//        System.out.println("===V1L===");
//        v1CenterLine.forEach(s -> System.out.println(s));
//        System.out.println("===V2L===");
//        v2CenterLine.forEach(s -> System.out.println(s));
//        System.out.println("===V3L===");
//        v3CenterLine.forEach(s -> System.out.println(s));
//
//
//        vaTriangle.forEach(
//                s -> System.out.println(s)
//        );
//
//        System.out.println("重心X  " + xArea / aArea);
//        System.out.println("重心Y  " + yArea / aArea);

//        System.out.println("耳朵边缘");
//        Oe.forEach(
//                s -> System.out.println(s)
//        );
//
//        System.out.println("内部边缘");
//        Le.forEach(
//                s -> System.out.println(s)
//        );
        System.out.println(oe);
        System.out.println(oe.size());
        System.out.println(le.size());
        System.out.println(gee);
//        System.out.println(reg.beta0);
//        System.out.println(reg.beta1);
//        //////////////////


        // 计算 SI
        oe.forEach(
                s -> {
                    double k = k(s);
                    double v = si(reg.beta1, k);
                    double nowDo;
                    if (v > 90) {
                        nowDo = 180 - v;
                    } else {
                        nowDo = v;
                    }
                    si.add(nowDo);
                }
        );


        for (int i = 0; i < oe.size(); i++) {
            double woi = woi(i, si, oeLength, oeArea);
            woilist.add(woi);
        }


    }

    private static double woi(int oi, List<Double> si, List<Double> oeLength, List<Double> oeArea) {
        double l = 1;
        double a = 0;
        double s = 0;

        double woi = l * loi(oi, oeLength) + a * aoi(oi, oeArea) + s * soi(oi, si);

        return woi;
    }


    private static double si(double a1, double ali) {
        return Math.abs(Math.toDegrees(Math.atan(a1)) - Math.toDegrees(Math.atan(ali)));
    }


    private static double soi(int oi, List<Double> si) {
        Double max = Collections.max(si);
        Double min = Collections.min(si);
        double res = (si.get(oi) - min) / (max - min);
        return res;
    }


    /***
     *
     * @param oi
     * @param oeLength
     * @return
     */
    private static double loi(int oi, List<Double> oeLength) {
//        l(o i )=(l’(o i )−l min )/(l max −l min)
        Double max = Collections.max(oeLength);
        Double min = Collections.min(oeLength);
        double res = (oeLength.get(oi) - min) / (max - min);
        return res;
    }


    private static double k(LineString l) {
        Coordinate[] coordinates = l.getCoordinates();
        return (coordinates[1].y - coordinates[0].y) / (coordinates[1].x - coordinates[0].x);
    }


    /***
     * @param n
     * @param x
     * @param y
     * @return
     */
    private double ali(int n, double x, double y) {
        return (n * (x * y) - x * y) / (n * x * x - x * x);
    }


    /***
     * @param oi Oe 面积的索引
     * @param oeArea Oe面积列表
     * @return
     */
    private static double aoi(int oi, List<Double> oeArea) {
//        A(oi )=(A’(oi )−Amin )/(Amax −Amin )

        Double max = Collections.max(oeArea);
        Double min = Collections.min(oeArea);
        double res = (oeArea.get(oi) - min) / (max - min);
        return res;

    }


    /***
     * 三边中心
     * @param a
     * @param b
     * @return
     */
    private static double[] sanbianzhongxin(Vector2D a, Vector2D b) {


        return new double[]{(a.x + b.x) / 2, (a.y + b.y) / 2};
    }


    /***
     * 三角形重心计算
     * @param a
     * @param b
     * @param c
     * @return
     */
    private static double[] triangleCenterOfGravity(Vector2D a, Vector2D b, Vector2D c) {
        double gravityX = (a.x + b.x + c.x) / 3;
        double gravityY = (a.y + b.y + c.y) / 3;
        double[] result = new double[]{gravityX, gravityY};
        return result;
    }


}
