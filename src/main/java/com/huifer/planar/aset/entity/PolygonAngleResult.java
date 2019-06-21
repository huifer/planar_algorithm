package com.huifer.planar.aset.entity;

import com.huifer.planar.aset.algo.impl.polygon.PolygonAngleCore.PointF;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

/**
 * <p>Title : PolygonAngleResult </p>
 * <p>Description : polygon 角度计算结果</p>
 *
 * @author huifer
 * @date 2019-01-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonAngleResult {


    private List<HashMap<PointF, Double>> polygonAngles;
    private Polygon polygon;

    private Polygon newPolygon;
    private double removeAngle;

}
