package com.huifer.planar.aset.utils.shptools.triangulation;

import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

/**
 * <p>Title : Triangle2dTools </p>
 * <p>Description : 三角形工具</p>
 *
 * @author huifer
 * @date 2018/10/17
 */
public class Triangle2dTools {


    /**
     * 内心
     **/
    public Vector2D neixin;
    /**
     * shp图形数据
     **/
    public Polygon triangle;
    /**
     * 三边中心
     **/
    public List<Vector2D> threeLineCenter;
    /**
     * 三角形重心
     **/
    public Vector2D gravity;
    /**
     * 三角形面积
     **/
    public double area;

    /**
     * 三角形的三条线
     **/
    public List<LineString> threeLinew = new ArrayList<>();

    /***
     * 三角形类别
     **/
    public Triangle2DEnum type;

    /**
     * 虚拟线列表
     **/
    public List<LineString> virtualLine = new ArrayList<>();

    /***
     * 虚拟线中心
     **/
    public List<Vector2D> virtualLineCenter = new ArrayList<>();

    /**
     * 内部线
     */
    public List<LineString> vsl = new ArrayList<>();


    Operation op = new Operation();

    public Triangle2dTools(Triangle2D triangle2D) {
        this.neixin = neixin(triangle2D.a, triangle2D.b, triangle2D.c);
        this.triangle = createPolygon(triangle2D.a, triangle2D.b, triangle2D.c);
        this.threeLineCenter = sanbianzhongxin(triangle2D.a, triangle2D.b, triangle2D.c);
        this.gravity = triangleCenterOfGravity(triangle2D.a, triangle2D.b, triangle2D.c);
    }

    /***
     * 三角形重心计算
     * @param a
     * @param b
     * @param c
     * @return
     */
    private static Vector2D triangleCenterOfGravity(Vector2D a, Vector2D b, Vector2D c) {
        double gravityX = (a.x + b.x + c.x) / 3;
        double gravityY = (a.y + b.y + c.y) / 3;
        return new Vector2D(gravityX, gravityY);
    }

    private void triangleTorM() {

        if (this.virtualLine.size() == 1) {
            // 虚拟线数量==1 那么取对角顶点
            virtualLineCenter = (List<Vector2D>) oneVirtualLineCenter(this.virtualLine)
                    .get("point");
            vsl = (List<LineString>) oneVirtualLineCenter(this.virtualLine).get("line");
        } else if (this.virtualLine.size() == 2) {
            // 虚拟线数量==2 那么取两条线的中点
            virtualLineCenter = (List<Vector2D>) twoVirtualLineCenter(this.virtualLine)
                    .get("point");
            vsl = (List<LineString>) twoVirtualLineCenter(this.virtualLine).get("line");
        } else if (this.virtualLine.size() == 3) {
            virtualLineCenter = (List<Vector2D>) threeVirtualCenter().get("point");
            vsl = (List<LineString>) threeVirtualCenter().get("line");
        }
//        return new Vector2D(1, 1);
    }

    /**
     * 3条虚拟边求内心
     */
    private HashMap threeVirtualCenter() {
        List<Vector2D> res = new ArrayList<>();
        /**连接线+ 点 **/
        HashMap<String, List> pointAndLine = new HashMap<>();

        res.add(neixin);
        res.addAll(this.threeLineCenter);
        /**连接线**/
        List<LineString> connectingLine = new ArrayList<>();

//        System.out.println("三条虚拟线的链接线");

        LineString lineString1 = vectorLine(neixin, threeLineCenter.get(0));
        LineString lineString2 = vectorLine(neixin, threeLineCenter.get(1));
        LineString lineString3 = vectorLine(neixin, threeLineCenter.get(2));

        connectingLine.add(lineString1);
        connectingLine.add(lineString2);
        connectingLine.add(lineString3);

        pointAndLine.put("point", res);
        pointAndLine.put("line", connectingLine);

        return pointAndLine;
    }

    /***
     * 2条虚拟线中心
     * @param lines
     * @return
     */
    private HashMap twoVirtualLineCenter(List<LineString> lines) {

        List<Vector2D> res = new ArrayList<>();
        HashMap<String, List> pointAndLine = new HashMap<>();
        /**连接线**/
        List<LineString> connectingLine = new ArrayList<>();

        for (LineString line : lines) {
            Coordinate[] coordinates = line.getCoordinates();
            Vector2D ab = new Vector2D((coordinates[0].x + coordinates[1].x) / 2,
                    (coordinates[0].y + coordinates[1].y) / 2);
            res.add(ab);
        }

//        System.out.println("二条虚拟线的链接线");
        LineString lineString = vectorLine(res.get(0), res.get(1));
        connectingLine.add(lineString);

        pointAndLine.put("point", res);
        pointAndLine.put("line", connectingLine);

        return pointAndLine;
    }

    /***
     * 一条虚拟线顶点
     * @param lineString
     * @return
     */
    private HashMap oneVirtualLineCenter(List<LineString> lineString) {

        List<Vector2D> res = new ArrayList<>();
        HashMap<String, List> pointAndLine = new HashMap<>();
        /**连接线**/
        List<LineString> connectingLine = new ArrayList<>();

        if (lineString.size() != 1) {

            return null;
        }

        Coordinate[] linePoint = lineString.get(0).getCoordinates();

        Coordinate[] trianglePoint = this.triangle.getCoordinates();
        Vector2D dingdiana = null;
        for (Coordinate c : trianglePoint) {
            if (Arrays.binarySearch(linePoint, c) < 0) {
                dingdiana = new Vector2D(c.x, c.y);
                res.add(dingdiana);

//                System.out.println("一条虚拟线的链接线");
                Vector2D lineC = new Vector2D((linePoint[0].x + linePoint[1].x) / 2,
                        (linePoint[0].y + linePoint[1].y) / 2);
                LineString aaaawww = vectorLine(lineC, dingdiana);
                connectingLine.add(aaaawww);
                pointAndLine.put("point", res);
                pointAndLine.put("line", connectingLine);

                return pointAndLine;
            }
        }

        return null;
    }

    /***
     * 两点连线
     * @param a
     * @param b
     * @return
     */
    private LineString vectorLine(Vector2D a, Vector2D b) {
        LineString abl = null;
        try {
            abl = op.createLineByWKT("LINESTRING(" + a.x + " " + a.y + "," + b.x + " " + b.y + ")");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return abl;
    }

    /***
     * 判断类别 以及数据是几条虚拟线
     * @param mulPolygonByWKT
     * @return
     */
    public int counterLine(MultiPolygon mulPolygonByWKT) {
        List<LineString> collect = this.threeLinew.stream().filter(s -> s.within(mulPolygonByWKT))
                .collect(Collectors.toList());

        this.virtualLine = collect;

        if (collect.size() == 1) {
            this.type = Triangle2DEnum.T;
        } else if (collect.size() == 2) {
            this.type = Triangle2DEnum.M;
        } else if (collect.size() == 3) {
            this.type = Triangle2DEnum.E;
        }

        triangleTorM();
        return collect.size();
    }

    /***
     * 创建三角形面
     * @param a
     * @param b
     * @param c
     * @return
     */
    private Polygon createPolygon(Vector2D a, Vector2D b, Vector2D c) {
        Polygon polygonByWKT = op.createTriangle(a, b, c);
        this.area = polygonByWKT.getArea();
        return polygonByWKT;
    }

    /***
     * 三边中心
     * @param a
     * @param b
     * @return
     */
    private List<Vector2D> sanbianzhongxin(Vector2D a, Vector2D b, Vector2D c) {
        List<Vector2D> lineCenter = new ArrayList<>();
        Vector2D ab = new Vector2D((a.x + b.x) / 2, (a.y + b.y) / 2);
        Vector2D ac = new Vector2D((a.x + c.x) / 2, (a.y + c.y) / 2);
        Vector2D bc = new Vector2D((b.x + c.x) / 2, (b.y + c.y) / 2);

        try {
            LineString abl = op
                    .createLineByWKT("LINESTRING(" + a.x + " " + a.y + "," + b.x + " " + b.y + ")");
            LineString acl = op
                    .createLineByWKT("LINESTRING(" + a.x + " " + a.y + "," + c.x + " " + c.y + ")");
            LineString bcl = op
                    .createLineByWKT("LINESTRING(" + b.x + " " + b.y + "," + c.x + " " + c.y + ")");
            threeLinew.add(abl);
            threeLinew.add(acl);
            threeLinew.add(bcl);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        lineCenter.add(ab);
        lineCenter.add(ac);
        lineCenter.add(bc);
        return lineCenter;
    }

    /***
     * 三角形内心
     * @param a
     * @param b
     * @param c
     * @return
     */
    private Vector2D neixin(Vector2D a, Vector2D b, Vector2D c) {
        double bc = Math.sqrt((b.x - c.x) * (b.x - c.x) + (b.y - c.y) * (b.y - c.y));
        double ca = Math.sqrt((c.x - a.x) * (c.x - a.x) + (c.y - a.y) * (c.y - a.y));
        double ab = Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));

        double x = ((bc * a.x) + (ca * b.x) + (ab * c.x)) / (bc + ca + ab);

        double y = ((bc * a.y) + (ca * b.y) + (ab * c.y)) / (bc + ca + ab);
        return new Vector2D(x, y);
    }

    ////////////////////////////////////
}
