package com.huifer.planar.aset.algo.impl.polygon;

import static com.huifer.planar.aset.utils.CommonUtils.reserveDecimal;
import static com.huifer.planar.aset.utils.CommonUtils.reserveDecimalGeometry;

import com.huifer.planar.aset.algo.PolygonRemoveNodeInterface;
import com.huifer.planar.aset.entity.PolygonRemoveNodeResult;
import com.huifer.planar.aset.utils.FileCommonMethod;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : PolygonRemoveNodeByLength </p>
 * <p>Description : PolygonRemoveNodeInterface 实现</p>
 * 把类型明确化 不适用Geometry 使用 Polygon 或者 point 不适用 Multi
 *
 * @author huifer
 * @date 2019-02-14
 */
public class PolygonRemoveNodeByLength implements PolygonRemoveNodeInterface {

    /**
     * 修改比例允许值
     */
    public static final double FINAL_PROPORTION = 1;

    /**
     * 保留小数位数
     */
    public static final int SAVE_NUMB = 3;

    @Override
    public PolygonRemoveNodeResult polygonRemoveNodeByLength(List<Polygon> polygonList,
            double tolerance)
            throws Exception {

        Set<Point> errorPoints = new HashSet<>();
        List<Polygon> oldGeoms = new ArrayList<>();
        List<Polygon> newGeoms = new ArrayList<>();
        List<PointWithPolygon> pointWithPolygonArrayList = new ArrayList<>();
        List<LineString> lineStrings = new ArrayList<>();
        List<PolygonEditor> polygonEditorArrayList = new ArrayList<>();
        List<Point> deletPointList;
        List<Polygon> resultPolygon;
        List<Point> outLinePointList = new ArrayList<>();
        // 获取计算需要的基本参数
        doCalcParam(polygonList, errorPoints, oldGeoms, newGeoms, lineStrings,
                polygonEditorArrayList);
        // TODO: 2019/2/20/0020 获取整体图形的外轮廓
        doPolygonOutLine(newGeoms, outLinePointList);
        // TODO: 2019/2/21/0021 从异常点中删除轮廓的点
        doRemovePoint(outLinePointList, errorPoints);

        System.out.println("===========================轮廓点===========================");
        outLinePointList.forEach(s -> System.out.println(s));
        System.out.println("===========================轮廓点===========================");
        System.out.println("===========================error===========================");
        errorPoints.forEach(s -> System.out.println(s));
        System.out.println("===========================error===========================");


        // 获取一个 PointWithPolygon 结果集合
        doCalacPointWithPolygonArgs(errorPoints, newGeoms, pointWithPolygonArrayList, lineStrings);

        // 获取需要删除的点
        deletPointList = doCalcDeletePoint(pointWithPolygonArrayList, polygonEditorArrayList);

        // polygon 移除异常点
        resultPolygon = polygonDeletePoint(newGeoms, deletPointList,
                polygonEditorArrayList, outLinePointList);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("===================面的编辑情况=======================");
        for (int i = 0; i < polygonEditorArrayList.size(); i++) {
            System.out.println(polygonEditorArrayList.get(i).getGeometry().toString() + "\t"
                    + polygonEditorArrayList.get(i).getEditor());
        }
        System.out.println("====================面移除异常点后显示======================");
        resultPolygon.forEach(s -> System.out.println(s));
        System.out.println("==========================================");
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        PolygonRemoveNodeResult polygonRemoveNodeResult = new PolygonRemoveNodeResult(
                errorPoints, oldGeoms, newGeoms, pointWithPolygonArrayList, lineStrings,
                polygonEditorArrayList, deletPointList, resultPolygon
        );

        return polygonRemoveNodeResult;
    }

    private void doRemovePoint(List<Point> outLinePointList, Set<Point> errorPoints) {
        List<Point> collect = errorPoints.stream().collect(Collectors.toList());
        for (int i = 0; i < collect.size(); i++) {
            Point point = collect.get(i);
            boolean contains = outLinePointList.contains(point);
            if (contains) {
                collect.remove(i);
                errorPoints.remove(point);
            }
        }
        System.out.println();
    }

    private void doPolygonOutLine(List<Polygon> polygonList, List<Point> outLinePointList) {
        // TODO: 2019/2/21/0021 融合面列表
        Geometry mergePolygon = doMergePolygon(polygonList);
        List<Coordinate> collect = Arrays.stream(mergePolygon.getCoordinates())
                .collect(Collectors.toList());

        for (int i = 0; i < collect.size(); i++) {
            Coordinate coordinate = collect.get(i);
            Point p = new GeometryFactory().createPoint(coordinate);
            outLinePointList.add(p);
        }


    }

    /**
     * 融合面列表
     *
     * @param polygonList 面列表
     * @return 融合后结果
     */
    private Geometry doMergePolygon(List<Polygon> polygonList) {
        Geometry union = null;
        for (int i = 0; i < polygonList.size(); i++) {
            union = polygonList.get(0).union(polygonList.get(1));
            for (int i1 = 2; i1 < polygonList.size(); i1++) {
                union = union.union(polygonList.get(i1));
            }
        }
        return union;
    }

    /**
     * 计算基础参数结果
     *
     * @param geometryList 面坐标列表
     * @param errorPoints 异常点
     * @param oldGeoms 输入老的面
     * @param newGeoms 输入老的面经过数据精度保留后的面
     * @param lineStrings 异常点的连接线
     * @param polygonEditorArrayList {@link PolygonEditor}
     */
    private void doCalcParam(List<Polygon> geometryList, Set<Point> errorPoints,
            List<Polygon> oldGeoms, List<Polygon> newGeoms, List<LineString> lineStrings,
            List<PolygonEditor> polygonEditorArrayList) {
        for (int i = 0; i < geometryList.size(); i++) {
            Polygon polygon = geometryList.get(i);
            Coordinate[] coordinates = polygon.getCoordinates();
            Polygon newGeom = reserveDecimalGeometry(coordinates);
            polygonEditorArrayList.add(new PolygonEditor(false, newGeom));
            newGeoms.add(newGeom);
            oldGeoms.add(polygon);

            for (int j = 0; j < coordinates.length - 1; j++) {
                Coordinate start = coordinates[j];
                Coordinate end = coordinates[j + 1];
                // 计算距离
                double xLen;
                double yLen;
                xLen = Math.abs(start.x - end.x);
                yLen = Math.abs(start.y - end.y);
                double length = Math.pow((xLen * xLen + yLen * yLen), 0.5);
                if (length < 2.0) {
                    errorPoints.add(new GeometryFactory().createPoint(
                            new Coordinate(reserveDecimal(end.x, SAVE_NUMB),
                                    reserveDecimal(end.y, SAVE_NUMB))));
                    errorPoints.add(new GeometryFactory().createPoint(
                            new Coordinate(reserveDecimal(start.x, SAVE_NUMB),
                                    reserveDecimal(start.y, SAVE_NUMB))));
                    LineString lineString = new GeometryFactory()
                            .createLineString(new Coordinate[]{
                                    new Coordinate(reserveDecimal(end.x, SAVE_NUMB),
                                            reserveDecimal(end.y, SAVE_NUMB)),
                                    new Coordinate(reserveDecimal(start.x, SAVE_NUMB),
                                            reserveDecimal(start.y, SAVE_NUMB))
                            });
                    lineStrings.add(lineString);
                }
            }
        }
    }

    /**
     * 计算 PointWithPolygon 结果
     *
     * @param errorPoints 异常点
     * @param newGeoms 精度保留后的面
     * @param pointWithPolygonArrayList {@link PointWithPolygon}
     * @param lineStrings 异常点的连接线
     */
    private void doCalacPointWithPolygonArgs(Set<Point> errorPoints, List<Polygon> newGeoms,
            List<PointWithPolygon> pointWithPolygonArrayList, List<LineString> lineStrings) {
        List<Point> collect = errorPoints.stream().collect(Collectors.toList());

        for (int i = 0; i < collect.size(); i++) {
            Point point = collect.get(i);
            List<Polygon> nearPolygons = new ArrayList<>();

            for (int j = 0; j < newGeoms.size(); j++) {
                Polygon newG = newGeoms.get(j);
                if (point.intersects(newG) && !nearPolygons.contains(newG)) {
                    nearPolygons.add(newG);
                }
            }
            PointWithPolygon pwg = new PointWithPolygon();
            pwg.setPoint(point);
            pwg.setNearPolygons(nearPolygons);
            pwg.setCount(nearPolygons.size());
            // 确定PointWithPolygon基准点的连接线
            for (int i1 = 0; i1 < lineStrings.size(); i1++) {
                LineString lineString = lineStrings.get(i1);
                boolean intersects = pwg.getPoint().intersects(lineString);
                if (intersects) {
                    pwg.setLineString(lineString);
                }
            }
            pointWithPolygonArrayList.add(pwg);
        }
    }

    /**
     * 计算删除点
     *
     * @param pointWithPolygonArrayList {@link PointWithPolygon}
     * @param polygonEditorArrayList {@link PolygonEditor }
     * @return 删除点
     */
    private List<Point> doCalcDeletePoint(List<PointWithPolygon> pointWithPolygonArrayList,
            List<PolygonEditor> polygonEditorArrayList) {
        List<Point> deletPointList;
        deletPointList = getDeletePointList(pointWithPolygonArrayList);
        // 对  deletPointList 修改 得到应该有效删除点  1. 这个点不能在三角形内 2. 四边形确认手段：  ABS(( 当前面 - 原始面)/原始面 ) < 固定比例

        for (int j = 0; j < deletPointList.size(); j++) {
            Point point = deletPointList.get(j);
            for (int i = 0; i < polygonEditorArrayList.size(); i++) {
                PolygonEditor polygonEditor = polygonEditorArrayList.get(i);
                if (isEditor(polygonEditor.getGeometry())) {
                    boolean intersects = polygonEditor.getGeometry().intersects(point);
                    if (intersects) {
                        deletPointList.remove(point);
                    }
                }
            }
        }
        return deletPointList;
    }

    /**
     * polygon 删除点后的结果
     *
     * @param newGeoms 保留小数过后的面列表
     * @param deleteResult 需要删除的点集合
     * @param polygonEditorArrayList {@link PolygonEditor}
     * @return polygon 修改后结果
     */
    private List<Polygon> polygonDeletePoint(List<Polygon> newGeoms,
            List<Point> deleteResult, List<PolygonEditor> polygonEditorArrayList,
            List<Point> outLinePointList)
            throws Exception {
        System.out
                .println("============================polygon 删除点后的结果============================");
        List<Polygon> resultPolygon = new ArrayList<>();
        List<Double> roles = new ArrayList<>();

        for (int i = 0; i < deleteResult.size(); i++) {
            Point deletePoint = deleteResult.get(i);
            Coordinate deletePointCoordinate = deletePoint.getCoordinate();
            // TODO: 2019/2/15/0015 不需要遍历NewGeoms 直接遍历polygonEditorArrayList

            for (int i1 = 0; i1 < polygonEditorArrayList.size(); i1++) {
                PolygonEditor polygonEditor = polygonEditorArrayList.get(i1);
                // 这个地方直接处理出结果
                if (isEditor(polygonEditor.getGeometry())) {
                    // 如果是三角形那么设置false
                    polygonEditor.setEditor(false);
                } else {
                    // 如果不是进行重构面操作 ， 构面规则如下 按照顺序做一个list 如果最后一个点和去重的！ 如果点的数量小于2 那么无效
                    Polygon oldM = polygonEditor.getGeometry();
                    // 1. 获取 oldM 的节点列表 去重的
                    List<Point> polygonIndexPoint = polygonIndexPoint(oldM);
                    // 2. 将 oldM 节点删除 deletePoint
                    // 3. 构造这个面
                    Polygon pg = oldPolygonDeleteNode(polygonIndexPoint, deletePoint);
                    double va = Math.abs((pg.getArea() - oldM.getArea()) / oldM.getArea());
                    if (va < FINAL_PROPORTION && va > 0) {
                        polygonEditor.setEditor(true);
                        System.out.println(pg.toString() + "\t" + va);
                    }

//                    Object[] polygon2role = asssssss(deletePointCoordinate, oldM);
//                    if (polygon2role != null) {
//                        System.out.println(polygon2role[0].toString() + "\t" + polygon2role[1]);
//                    }

                }
            }

/////////
//            for (int i1 = 0; i1 < newGeoms.size(); i1++) {
//                Geometry polygon = newGeoms.delete(i1);
//                if (polygon.intersects(deletePoint)) {
//                    List<Object[]> newPolygons = geometryDeletePoint(polygon, deletePointCoordinate,
//                            polygonEditorArrayList);
//                    for (int i2 = 0; i2 < newPolygons.size(); i2++) {
//                        Geometry geom = (Geometry) newPolygons.delete(i2)[0];
//                        double role = (Double) newPolygons.delete(i2)[1];
//                        if (!resultPolygon.contains(geom)) {
//                            resultPolygon.add(geom);
//                            roles.add(role);
//
//                        }
//                    }
//                }
//            }
//

        }
//        doWriteCSV(resultPolygon, roles);
        System.out
                .println("============================polygon 删除点后的结果============================");

        return resultPolygon;
    }

    /**
     * 从节点中删除一个点重新构造面
     *
     * @param polygonIndexPoint 原始面的节点
     * @param deletePoint 需要删除的节点
     */
    private Polygon oldPolygonDeleteNode(List<Point> polygonIndexPoint, Point deletePoint) {
        // 删除点
        for (int i = 0; i < polygonIndexPoint.size(); i++) {
            Point point = polygonIndexPoint.get(i);
            if (point.equals(deletePoint)) {
                polygonIndexPoint.remove(i);
            }
        }

        Polygon pg = null;
        Coordinate[] coordinates = null;
        if (polygonIndexPoint.size() >= 2) {
            polygonIndexPoint.add(polygonIndexPoint.get(0));
            coordinates = pointCreateCoordinate(polygonIndexPoint);
            pg = new GeometryFactory().createPolygon(coordinates);
            return pg;
        } else {
            return null;
        }
    }

    /***
     * 获取 oldM 的节点列表 去重的 顺序不变
     * @param oldM 老的面
     * @return 顺序点表
     */
    private List<Point> polygonIndexPoint(Polygon oldM) {
        // TODO: 2019/2/15
        List<Point> rankedPoint = new ArrayList<>();
        Coordinate[] coordinates = oldM.getCoordinates();

        for (int i = 0; i < coordinates.length; i++) {
            Point point = new GeometryFactory().createPoint(coordinates[i]);
            if (!rankedPoint.contains(point)) {
                rankedPoint.add(point);
            }
        }
        return rankedPoint;
    }


    private Object[] asssssss(Coordinate deletePointCoordinate, Polygon oldM) {
        List<Coordinate> polygonNode = Arrays.stream(oldM.getCoordinates())
                .collect(Collectors.toList());
        Object[] polygonToRole;
        for (int i = 0; i < polygonNode.size(); i++) {
            // 删除点是否在这个面的节点中 如果是 那么这个面会被修改
            if (polygonNode.get(i).equals(deletePointCoordinate)) {
                polygonNode.remove(i);
            }
        }
        if (polygonNode.get(0).equals(polygonNode.get(polygonNode.size() - 1))) {

            polygonToRole = doCalcNewPolygon(oldM, polygonNode);
        } else {

            polygonNode.add(polygonNode.get(0));
            polygonToRole = doCalcNewPolygon(oldM, polygonNode);
        }

        return polygonToRole;
    }


    /**
     * 当前面移除删除点后的图形
     *
     * @param oldPolygon 当前面
     * @param deletePoint 需要移除的点
     * @param polygonEditorArrayList {@link PolygonEditor}
     * @return [geom, 比例]
     */
    private List<Object[]> geometryDeletePoint(Polygon oldPolygon, Coordinate deletePoint,
            List<PolygonEditor> polygonEditorArrayList) {
        List<Coordinate> polygonNode = Arrays.stream(oldPolygon.getCoordinates())
                .collect(Collectors.toList());

        List<Object[]> hs = new ArrayList<>();
        for (PolygonEditor polygonEditor : polygonEditorArrayList) {
            // todo : bug 一个被编辑过的面还被输出true
            if (polygonEditor.getEditor() == false) {
                Object[] polygonToRole;

                for (int i = 0; i < polygonNode.size(); i++) {
                    // 删除点是否在这个面的节点中 如果是 那么这个面会被修改
                    if (polygonNode.get(i).equals2D(deletePoint)) {
                        polygonEditor.setEditor(true);
                        polygonNode.remove(i);
                    }
                }

                if (polygonNode.get(0).equals2D(polygonNode.get(polygonNode.size() - 1))) {
                    if (polygonNode.size() == 3) {
                        System.out.println();
                    }

                    polygonToRole = doCalcNewPolygon(oldPolygon, polygonNode);
                    if (polygonToRole != null) {
                        hs.add(polygonToRole);
                    }

                } else {
                    if (polygonNode.size() == 3) {
                        System.out.println();
                    }
                    polygonNode.add(polygonNode.get(0));
                    polygonToRole = doCalcNewPolygon(oldPolygon, polygonNode);
                    if (polygonToRole != null) {
                        hs.add(polygonToRole);
                    }
                }
            }

        }

        return hs;
    }

    /**
     * 计算新的面
     *
     * @param oldPolygon 老的面
     * @param polygonNode 老的面的节点集合
     * @return [geom, 比例]
     */
    private Object[] doCalcNewPolygon(Polygon oldPolygon,
            List<Coordinate> polygonNode) {
        Polygon newPolygon;
        Object[] polygonToRole = new Object[2];

        Coordinate[] coordinate = createCoordinate(polygonNode);
        try {

            newPolygon = new GeometryFactory().createPolygon(coordinate);

            //  验证 newPolygon 修改后的面积和 原始 oldPolygon 的面积比例 简单判断规则: (当前面-原始面)/原始面 < 0.2
            double role = role(oldPolygon, newPolygon);
            polygonToRole[0] = newPolygon;
            polygonToRole[1] = role;

            if (role < FINAL_PROPORTION) {
                return polygonToRole;

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证规则
     *
     * @param oldGeom 旧面
     * @param newGeom 新面
     * @return ABS(( 当前面 - 原始面)/原始面 )
     */
    private double role(Polygon oldGeom, Polygon newGeom) {
        double v = (newGeom.getArea() - oldGeom.getArea()) / oldGeom.getArea();
        return Math.abs(v);
    }


    /**
     * Coordinate list 转 数组
     *
     * @param collect CoordinateList 转换成 Coordinate数组
     * @return Coordinate数组
     */
    private Coordinate[] createCoordinate(List<Coordinate> collect) {
        Coordinate[] coordinates = new Coordinate[collect.size()];
        for (int i = 0; i < collect.size(); i++) {
            coordinates[i] = collect.get(i);
        }
        return coordinates;
    }

    /**
     * Coordinate list 转 数组
     *
     * @param collect CoordinateList 转换成 Coordinate数组
     * @return Coordinate数组
     */
    private Coordinate[] pointCreateCoordinate(List<Point> collect) {
        Coordinate[] coordinates = new Coordinate[collect.size()];

        for (int i = 0; i < collect.size(); i++) {
            Point point = collect.get(i);
            coordinates[i] = new Coordinate(point.getX(), point.getY());
        }
        return coordinates;
    }

    /**
     * 是否能够编辑这个面 小于四个点不能编辑
     * <p>1. 节点数量少于四个不能编辑</p>
     * <p>2. 节点数量大于四的情况下， 判断是否出现三点共线情况 </p>
     *
     * @param geometry 面
     * @return boolean
     */
    private boolean isEditor(Polygon geometry) {
        Set<Coordinate> collect = Arrays.stream(geometry.getCoordinates())
                .collect(Collectors.toSet());
        return collect.size() < 4;

    }

    /**
     * 分号风格的csv文件
     */
    private void doWriteCSV(List<Polygon> polygon, List<Double> roles) throws Exception {

        if (polygon.size() != roles.size()) {
            throw new IllegalArgumentException("参数异常");
        }

        List<String> ls = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    UUID.randomUUID().toString().replaceAll("-", "") + ";" +
                            polygon.get(i).toString() + ";" + roles.get(i)
            );
            ls.add(sb.toString());
        }

        FileCommonMethod.writeCSV(
                "E:\\work-java\\planar_algorithm\\src\\main\\resources\\1.csv",
                new String[]{"uuid", "wkt", "proportion "},
                ls
        );
    }


    /**
     * 获取删除点的列表
     *
     * @param res {@link PointWithPolygon}
     * @return 删除点列表
     */
    private List<Point> getDeletePointList(List<PointWithPolygon> res) {
        // 根据连接线分组
        Map<LineString, List<PointWithPolygon>> collect1 = res.stream()
                .collect(Collectors.groupingBy(PointWithPolygon::getLineString));

        // 分组后寻找需要删除的线
        List<Point> deleteResult = new ArrayList<>();
        for (LineString lineString : collect1.keySet()) {
            List<PointWithPolygon> pointWithPolygons = collect1.get(lineString);

            for (int i = 0; i < pointWithPolygons.size(); i++) {
                for (int j = i + 1; j < pointWithPolygons.size(); j++) {
                    PointWithPolygon csi = pointWithPolygons.get(i);
                    PointWithPolygon csj = pointWithPolygons.get(j);

                    if (csi.getCount() < csj.getCount()) {
                        if (csi.getCount() > 1) {
                            deleteResult.add(csi.getPoint());
                        }
                    } else {
                        if (csj.getCount() > 1) {
                            deleteResult.add(csj.getPoint());
                        }
                    }
                }
            }
        }
        return deleteResult;
    }

    /**
     * 点和面的关系类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointWithPolygon {

        /**
         * 基准点
         */
        private Point point;

        /**
         * 基准点相邻的面
         */
        private List<Polygon> nearPolygons;

        /**
         * 基准点连接了几个面
         */
        private int count;

        /**
         * 基准点的连接线（条件：在容差内）
         */
        private LineString lineString;
    }

    /**
     * 面是否能够被修改的判断类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolygonEditor {

        /**
         * 是否被编辑过
         */
        private Boolean editor;
        /**
         * 面坐标
         */
        private Polygon geometry;


    }
}
