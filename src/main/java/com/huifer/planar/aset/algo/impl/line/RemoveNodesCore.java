package com.huifer.planar.aset.algo.impl.line;

import com.huifer.planar.aset.algo.RemoveNodesInterface;
import com.huifer.planar.aset.utils.geo.GeomtryUtil;
import org.locationtech.jts.geom.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : RemoveNodesCore </p>
 * <p>Description : 移除节点通用级别实现方法</p>
 *
 * @author huifer
 * @date 2019-02-25
 */
public class RemoveNodesCore implements RemoveNodesInterface {

    /**
     * 通用级别
     *
     * @param geometry 几何图形
     * @param node     需要删除的节点
     * @return 删除后结果
     */
    public static Geometry removeNode(Geometry geometry, Coordinate node) {
        if (geometry instanceof Point) {
            return removeNode((Point) geometry, node);
        }
        if (geometry instanceof LineString) {
            return removeNode((LineString) geometry, node);
        }
        if (geometry instanceof LinearRing) {
            return removeNode((LinearRing) geometry, node);
        }
        if (geometry instanceof Polygon) {
            return removeNode((Polygon) geometry, node);
        }
        if (geometry instanceof MultiPolygon) {
            return removeNode((MultiPolygon) geometry, node);
        }
        if (geometry instanceof GeometryCollection) {
            return removeNode((GeometryCollection) geometry, node);
        } else {
            return null;
        }
    }

    /**
     * 点删除节点
     *
     * @param point 点
     * @param node  需要删除的节点
     * @return 删除后结果
     */
    private static Point removeNode(Point point, Coordinate node) {
        if (inCoordinates(point.getCoordinates(), node)) {
            return null;
        }
        return point;
    }

    /**
     * 环删除节点
     *
     * @param linearRing 环
     * @param node       需要删除的节点
     * @return 删除后结果
     */
    private static LinearRing removeNode(LinearRing linearRing, Coordinate node) {
        if (!inCoordinates(linearRing.getCoordinates(), node)) {
            return linearRing;
        }
        Coordinate[] coordinates = removeNode(linearRing.getCoordinates(), node);
        if (coordinates.length == linearRing.getCoordinates().length - 2) {
            Coordinate[] c = new Coordinate[coordinates.length + 1];
            for (int i = 0; i < coordinates.length; i++) {
                c[i] = coordinates[i];
            }
            c[coordinates.length] = coordinates[0];
            coordinates = c;
        }
        if (coordinates.length <= 3) {
            return null;
        }
        return new GeometryFactory().createLinearRing(coordinates);
    }

    /**
     * 删除面上节点
     *
     * @param polygon 面
     * @param node    需要删除的节点
     * @return 删除后结果
     */
    private static Polygon removeNode(Polygon polygon, Coordinate node) {
        if (!inCoordinates(polygon.getCoordinates(), node)) {
            return polygon;
        }
        LinearRing linearRing = removeNode((LinearRing) polygon.getExteriorRing(), node);
        if (linearRing == null) {
            return null;
        }
        List<LinearRing> ls = new ArrayList<>();
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            LinearRing lr = (LinearRing) polygon.getInteriorRingN(i);
            LinearRing retain = removeNode(lr, node);
            if (retain == null || retain.isEmpty()) {
                continue;
            }
            ls.add(retain);
        }
        return polygon.getFactory().createPolygon(
                linearRing, ls.toArray(new LinearRing[ls.size()])
        );
    }

    /**
     * 多面删除节点
     *
     * @param mu   多面
     * @param node 需要删除的节点
     * @return 删除后结果
     */
    private static MultiPolygon removeNode(MultiPolygon mu, Coordinate node) {
        if (!inCoordinates(mu.getCoordinates(), node)) {
            return mu;
        }
        List<Geometry> geometries = GeomtryUtil.getGeometries(mu);
        List<Polygon> result = new ArrayList<>();
        for (Geometry geometry : geometries) {
            Polygon p = (Polygon) geometry;
            if (!inCoordinates(geometry.getCoordinates(), node)) {
                result.add(p);
                continue;
            }
            Geometry out = removeNode(p, node);
            if (out == null) {
                continue;
            }
            if (out.isEmpty()) {
                continue;
            }
            if (!(out instanceof Polygon)) {
                continue;
            }
            result.add((Polygon) out);
        }
        if (result.size() == 0) {
            return mu.getFactory().createMultiPolygon(new Polygon[]{});
        }
        return mu.getFactory().createMultiPolygon(result.toArray(new Polygon[result.size()]));
    }

    /**
     * 折线删除节点
     *
     * @param lineString 折线
     * @param node       需要删除的节点
     * @return 删除后结果
     */
    private static LineString removeNode(LineString lineString, Coordinate node) {
        Coordinate[] cs = lineString.getCoordinates();
        if (!inCoordinates(cs, node)) {
            return lineString;
        }
        Coordinate[] coordinates = removeNode(cs, node);
        if (coordinates.length < 2) {
            return null;
        }
        return new GeometryFactory().createLineString(removeNode(cs, node));
    }

    private static Geometry removeNode(GeometryCollection gc, Coordinate node) {
        if (!inCoordinates(gc.getCoordinates(), node)) {
            return gc;
        }
        List<Geometry> geometries = GeomtryUtil.getGeometries(gc);
        List<Geometry> result = new ArrayList<>();
        for (Geometry geometry : geometries) {
            if (!inCoordinates(geometry.getCoordinates(), node)) {
                result.add(geometry);
                continue;
            }
            Geometry geometry1 = removeNode(geometry, node);
            if (geometry1 != null && !geometry1.isEmpty()) {
                result.add(geometry1);
            }
        }
        if (result.size() == 0) {
            return gc.getFactory().createGeometryCollection(new Geometry[]{});
        }
        if (result.size() == 1) {
            return result.get(0);
        }
        return gc.getFactory().createGeometryCollection(
                result.toArray(new Geometry[result.size()])
        );


    }

    /**
     * 节点数组删除节点
     *
     * @param coordinates 节点数组
     * @param node        需要删除的节点
     * @return 删除后结果
     */
    private static Coordinate[] removeNode(Coordinate[] coordinates, Coordinate node) {
        if (!inCoordinates(coordinates, node)) {
            return coordinates;
        }
        List<Coordinate> result = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            if (coordinate.x != node.x || coordinate.y != node.y) {
                result.add(coordinate);
            }
        }
        return result.toArray(new Coordinate[result.size()]);
    }

    /**
     * 判断节点是否在列表中
     *
     * @param coordinates 点集合列表
     * @param node        待判断节点
     * @return Boolean
     */
    private static boolean inCoordinates(Coordinate[] coordinates, Coordinate node) {
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i].x == node.x && coordinates[i].y == node.y) {
                return true;
            }
        }
        return false;
    }


    private static Object[] doRemove(Geometry geometry, List<Coordinate> coordinateList) {
        for (Coordinate coordinate : coordinateList) {
            Geometry geometry1 = removeNode(geometry, coordinate);
            if (!geometry1.isValid()) {
                continue;
            }
            return new Object[]{coordinate, geometry1};
        }
        return null;
    }

    @Override
    public Geometry remove(Geometry geometry, Coordinate coordinate) {
        return removeNode(geometry, coordinate);
    }

    @Override
    public Geometry removeList(Geometry geometry, List<Coordinate> coordinateList) {
        List<Coordinate> result = new ArrayList<>();
        result.addAll(coordinateList);
        Object[] objects = doRemove(geometry, result);
        while (objects != null) {
            result.remove((Coordinate) objects[0]);
            geometry = (Geometry) objects[1];
            objects = doRemove(geometry, result);
        }
        return geometry;
    }


}
