package com.huifer.planar.aset.algo;

import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

/**
 * <p>Title : RemoveNodesInterface </p>
 * <p>Description : 移除节点通用级别</p>
 *
 * @author huifer
 * @date 2019-02-25
 */
public interface RemoveNodesInterface {

    /**
     * 移除节点
     *
     * @param geometry 需要移除节点的坐标
     * @param coordinate 移除节点坐标
     * @return 移除后geometry
     */
    Geometry remove(Geometry geometry, Coordinate coordinate);


    /**
     * 几何图形删除节点组
     *
     * @param geometry 需要移除节点的坐标
     * @param coordinateList 移除节点坐标集合
     * @return 移除后geometry
     */
    Geometry removeList(Geometry geometry, List<Coordinate> coordinateList);
}
