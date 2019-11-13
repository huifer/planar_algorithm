package com.huifer.planar.aset.algo.impl.line;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.valid.IsValidOp;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : RemoveLine </p>
 * <p>Description : 删除边</p>
 *
 * @author huifer
 * @date 2019-02-25
 */
public class RemoveLine {

    private static double TOL_ANGLE = 20 * Math.PI / 180;

    /**
     * 根据容差删除线段节点
     *
     * @param line 线段
     * @param tol  容差
     * @return 删除后结果
     */
    private static List<LineEdge> getShort(LineString line, double tol) {
        List<LineEdge> out = new ArrayList<>();
        Coordinate[] cs = line.getCoordinates();
        for (int i = 0; i < cs.length - 1; i++) {
            double len = cs[i].distance(cs[i + 1]);
            if (len < tol) {
                out.add(new LineEdge(len, i));
            }
        }
        return out;
    }

    /**
     * 删除面上小于容差的节点
     *
     * @param poly 面
     * @param tol  容差
     * @return 删除后结果
     */
    private static List<PolygonEdge> getShort(Polygon poly, double tol) {
        List<PolygonEdge> out = new ArrayList<>();
        List<LineEdge> lss = getShort(poly.getExteriorRing(), tol);
        for (LineEdge cls : lss) {
            out.add(new PolygonEdge(-1, cls));
        }
        for (int i = 0; i < poly.getNumInteriorRing(); i++) {
            lss = getShort(poly.getInteriorRingN(i), tol);
            for (LineEdge cls : lss) {
                out.add(new PolygonEdge(i, cls));
            }
        }
        return out;
    }


    /**
     * 删除节点流程
     *
     * @param poly  面
     * @param tolmm 删除容差
     * @param scale 比例
     * @return 删除后结果
     */
    public static Geometry delete(Polygon poly, double tolmm, double scale) {
        return delete(poly, tolmm * scale * 0.001);
    }

    /**
     * 删除节点流程
     *
     * @param poly 面
     * @param tol  容差
     * @return 删除后结果
     */
    public static Geometry delete(Polygon poly, double tol) {
        Polygon clonePolygon = (Polygon) poly.copy();
        List<PolygonEdge> shSegs = getShort(clonePolygon, tol);

        while (shSegs.size() > 0) {
            PolygonEdge shst = shSegs.get(0);
            for (PolygonEdge seg : shSegs) {
                if (seg.edge.length < shst.edge.length) {
                    shst = seg;
                }
            }
            shSegs.remove(shst);
////////////////////////////////////////
            PolygonDelete out = deleteEdge(clonePolygon, shst);

            if (!out.success) {
                continue;
            }

            clonePolygon = out.polygon;
            shSegs = getShort(clonePolygon, tol);
        }
        return clonePolygon;
    }

    /**
     * 面删除节点
     *
     * @param polygon polygon
     * @param seg     {@link PolygonEdge}
     * @return 删除后结果
     */
    private static PolygonDelete deleteEdge(Polygon polygon, PolygonEdge seg) {
        LineString lineString;
        if (seg.ringId == -1) {
            lineString = polygon.getExteriorRing();
        } else {
            lineString = polygon.getInteriorRingN(seg.ringId);
        }

        ////////////////////////////////
        RingeDelete out = deleteEdge(lineString, seg.edge);

        if (!out.success) {
            return new PolygonDelete(null, false);
        }

        lineString = out.ring;

        LinearRing shell;
        LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
        if (seg.ringId == -1) {
            shell = (LinearRing) lineString;
            for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                holes[i] = (LinearRing) polygon.getInteriorRingN(i).copy();
            }
        } else {
            shell = (LinearRing) polygon.getExteriorRing();
            for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                holes[i] = (LinearRing) polygon.getInteriorRingN(i).copy();
            }
            holes[seg.ringId] = (LinearRing) lineString;
        }
        Polygon ocp = new GeometryFactory().createPolygon(shell, holes);

        if (!IsValidOp.isValid(ocp) || ocp.isEmpty()) {
            return new PolygonDelete(ocp, false);
        }

        return new PolygonDelete(ocp, true);
    }

    /**
     * 线段删除节点
     *
     * @param lineString 线段
     * @param seg        {@link LineEdge}
     * @return 删除后结果
     */
    private static RingeDelete deleteEdge(LineString lineString, LineEdge seg) {

        Coordinate[] cs = lineString.getCoordinates();
        if (cs.length <= 4) {
            return new RingeDelete(null, false);
        }

        // 删除点的完整流程
        Coordinate p1 = cs[seg.id];
        Coordinate p2 = cs[seg.id + 1];
        Coordinate p0 = seg.id == 0 ? cs[cs.length - 2] : cs[seg.id - 1];
        Coordinate p3 = seg.id + 2 == cs.length ? cs[1] : cs[seg.id + 2];

        double a = Math.atan2(p3.y - p2.y, p3.x - p2.x) - Math.atan2(p0.y - p1.y, p0.x - p1.x);
        if (a <= -Math.PI) {
            a += 2 * Math.PI;
        } else if (a > Math.PI) {
            a -= 2 * Math.PI;
        }

        if (Math.abs(a) <= Math.PI / 2 + TOL_ANGLE && Math.abs(a) >= Math.PI / 2 - TOL_ANGLE) {

            double x1 = p0.x - p1.x, ya = p0.y - p1.y;
            double x2 = p3.x - p2.x, yb = p3.y - p2.y;
            double t = (x2 * (p1.y - p2.y) - yb * (p1.x - p2.x)) / (x1 * yb - ya * x2);
            Coordinate c = new Coordinate(p1.x + t * x1, p1.y + t * ya);

            Coordinate[] css = new Coordinate[cs.length - 1];

            if (seg.id != 0) {
                for (int i = 0; i < seg.id; i++) {
                    css[i] = cs[i];
                }
                css[seg.id] = c;
                for (int i = seg.id + 1; i < cs.length - 1; i++) {
                    css[i] = cs[i + 1];
                }
                if (seg.id == cs.length - 2) {
                    css[0] = c;
                }
            } else {
                css[0] = c;
                for (int i = 1; i < cs.length - 2; i++) {
                    css[i] = cs[i - 1];
                }
                css[cs.length - 2] = c;
            }

            return getRingeDelete(css);
        } else if (Math.abs(a) >= Math.PI - TOL_ANGLE) {
            double dx = p1.x - p0.x + p3.x - p2.x;
            double dy = p1.y - p0.y + p3.y - p2.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = dx / length;
            dy = dy / length;

            double xMid = (p0.x + p3.x) * 0.5, yMid = (p0.y + p3.y) * 0.5;

            double t1 = (p0.x - xMid) * dx + (p0.y - yMid) * dy;
            double t2 = (p3.x - xMid) * dx + (p3.y - yMid) * dy;
            Coordinate c1 = new Coordinate(xMid + t1 * dx, yMid + t1 * dy);
            Coordinate c2 = new Coordinate(xMid + t2 * dx, yMid + t2 * dy);

            return getRingeDelete(seg, cs, c1, c2);
        } else if (Math.abs(a) <= TOL_ANGLE) {
            if (p0 == p3) {
                return new RingeDelete(null, false);
            }

            double t1 = ((p2.x - p3.x) * (p0.x - p3.x) + (p2.y - p3.y) * (p0.y - p3.y))
                    / ((p2.x - p3.x) * (p2.x - p3.x) + (p2.y - p3.y) * (p2.y - p3.y));
            double t2 = ((p1.x - p0.x) * (p3.x - p0.x) + (p1.y - p0.y) * (p3.y - p0.y))
                    / ((p1.x - p0.x) * (p1.x - p0.x) + (p1.y - p0.y) * (p1.y - p0.y));
            Coordinate c11 = new Coordinate(p3.x + t1 * (p2.x - p3.x), p3.y + t1 * (p2.y - p3.y));
            Coordinate c22 = new Coordinate(p0.x + t2 * (p1.x - p0.x), p0.y + t2 * (p1.y - p0.y));
            boolean v1 = (p3.x - c11.x) * (p2.x - c11.x) + (p3.y - c11.y) * (p2.y - c11.y) < 0;
            boolean v2 = (p0.x - c22.x) * (p1.x - c22.x) + (p0.y - c22.y) * (p1.y - c22.y) < 0;

            Coordinate c1, c2;

            if (!v1 && !v2) {
                return new RingeDelete(null, false);
            } else if (!v1 && v2) {
                c1 = p0;
                c2 = c22;
            } else if (v1 && !v2) {
                c1 = c11;
                c2 = p3;
            } else {
                double d1 = p0.distance(p1);
                double d2 = p3.distance(p2);
                if (d1 < d2) {
                    c1 = c11;
                    c2 = p3;
                } else {
                    c1 = p0;
                    c2 = c22;
                }
            }

            return getRingeDelete(seg, cs, c1, c2);
        } else {
            return new RingeDelete(null, false);
        }
    }

    /**
     * 节点删除
     *
     * @param seg {@link LineEdge}
     * @param cs  节点坐标集合
     * @param c1  c1
     * @param c2  c2
     * @return 删除后结果
     */
    private static RingeDelete getRingeDelete(LineEdge seg, Coordinate[] cs, Coordinate c1,
                                              Coordinate c2) {
        Coordinate[] css = new Coordinate[cs.length - 2];
        css[0] = c1;
        css[1] = c2;
        if (seg.id != 0) {
            for (int i = seg.id + 3; i < cs.length; i++) {
                css[i - seg.id - 1] = cs[i];
            }
            for (int i = 1; i < seg.id - 1; i++) {
                css[cs.length - seg.id - 2 + i] = cs[i];
            }
            css[cs.length - 3] = c1;
        } else {
            for (int i = 2; i < cs.length - 3; i++) {
                css[i] = cs[i + 1];
            }
            css[cs.length - 3] = c1;
        }

        return getRingeDelete(css);
    }

    /**
     * 删除节点
     *
     * @param css 新的坐标组
     * @return 删除节点
     */
    private static RingeDelete getRingeDelete(Coordinate[] css) {
        if (css[0].x != css[css.length - 1].x || css[0].y != css[css.length - 1].y) {
            return new RingeDelete(null, false);
        } else if (css.length <= 3) {
            return new RingeDelete(null, false);
        } else {
            return new RingeDelete(new GeometryFactory().createLinearRing(css), true);
        }
    }

    /**
     * 线段节点
     */
    private static class LineEdge {

        public int id = -1;
        public double length = -1.0;

        public LineEdge(double l, int id) {
            this.length = l;
            this.id = id;
        }
    }

    /**
     * 面节点
     */
    private static class PolygonEdge {

        public int ringId = -999;
        public LineEdge edge = null;

        public PolygonEdge(int ringId, LineEdge seg) {
            this.ringId = ringId;
            this.edge = seg;
        }

    }


    /**
     * 环的删除
     */
    private static class RingeDelete {

        public LinearRing ring = null;
        public boolean success = false;

        public RingeDelete(LinearRing ring, boolean success) {
            this.ring = ring;
            this.success = success;
        }
    }


    /**
     * 面的删除
     */
    private static class PolygonDelete {

        public Polygon polygon = null;
        public boolean success = false;

        public PolygonDelete(Polygon polygon, boolean success) {
            this.polygon = polygon;
            this.success = success;
        }
    }
}
