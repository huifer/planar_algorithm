package com.huifer.planar.aset.utils.polygonselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

import java.util.List;

/**
 * <p>Title : TocePoint </p>
 * <p>Description : 面上的线段</p>
 *
 * @author huifer
 * @date 2019-04-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ToceLineString {

    private String polygonID;
    private List<LineString> lineStringList;

    public ToceLineString(String polygonID) {
        this.polygonID = polygonID;
    }
}
