package com.huifer.planar.aset.entity;

import com.huifer.planar.aset.algo.impl.polygon.PolygonRemoveNodeByLength.PointWithPolygon;
import com.huifer.planar.aset.algo.impl.polygon.PolygonRemoveNodeByLength.PolygonEditor;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : PolygonRemoveNodeResult </p>
 * <p>Description : 面移除节点结果接收类</p>
 *
 * @author huifer
 * @date 2019-02-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PolygonRemoveNodeResult {

    /**
     * 异常点集合
     */
    private Set<Point> errorPoints;
    /**
     * 没有保留精度的面，直接从数据中获取的面
     */
    private List<Polygon> oldGeoms;
    /**
     * 精度保留后的面
     */
    private List<Polygon> newGeoms;
    /**
     * 异常点和面的关系类
     */
    private List<PointWithPolygon> pointWithPolygonArrayList;
    /**
     * 异常点的连接线
     */
    private List<LineString> lineStrings;
    /**
     * 保留小数后当前面是否能够被修改的判断类
     */
    private List<PolygonEditor> polygonEditorArrayList;
    /**
     * 推断需要删除的点集合
     */
    private List<Point> deletPointList;
    /**
     * 删除点集合后的面
     */
    private List<Polygon> resultPolygon;
}
