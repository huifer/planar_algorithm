package com.huifer.planar.aset.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : KmeanPolygonResult </p>
 * <p>Description : Kmean+泰森多边形平分多边形结果</p>
 *
 * @author huifer
 * @date 2019-01-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KmeanPolygonResult {

    /**
     * 原始平面
     */
    private Geometry polygon;
    /**
     * 随机点集合
     */
    private ArrayList<Point> pointList;
    /**
     * 分组后组id
     */
    private int[] assignments;
    /**
     * 中心集合
     */
    private double[][] centroids;
    /**
     * xlist
     */
    private ArrayList<Double> xlist;
    /**
     * ylist
     */
    private ArrayList<Double> ylist;
    /**
     * 构造泰森多边形
     */
    private List<Geometry> voronoi;

    /**
     * 切割后图形
     */
    private List<Geometry> geometryList;
}
