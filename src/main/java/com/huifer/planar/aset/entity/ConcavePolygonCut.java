package com.huifer.planar.aset.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : ConcavePolygonCut </p>
 * <p>Description : 凹多边形切割结果类</p>
 *
 * @author huifer
 * @date 2019-03-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcavePolygonCut {

    /**
     * 原始面
     */
    private Polygon originalPolygon;
    /**
     * 凹点
     */
    private List<Point> troughList;
    /**
     * 内部线段
     */
    private List<LineString> internalLine;

    /**
     * 切割后的面结果
     */
    private List<Polygon> cutPolygonList;

}
