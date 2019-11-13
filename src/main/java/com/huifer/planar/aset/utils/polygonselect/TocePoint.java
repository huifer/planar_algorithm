package com.huifer.planar.aset.utils.polygonselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

/**
 * <p>Title : TocePoint </p>
 * <p>Description : 面上的点</p>
 *
 * @author huifer
 * @date 2019-04-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TocePoint {

    private String polygonID;
    private List<Point> pointList;

    public TocePoint(String polygonID) {
        this.polygonID = polygonID;
    }
}
