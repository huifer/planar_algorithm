package com.huifer.planar.aset.utils.polygonselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

/**
 * <p>Title : TocePolygon </p>
 * <p>Description : 单个面属性</p>
 *
 * @author huifer
 * @date 2019-04-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TocePolygon {
    private String id;
    private Geometry geom;

}
