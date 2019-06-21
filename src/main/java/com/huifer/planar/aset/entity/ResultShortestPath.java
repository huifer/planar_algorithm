package com.huifer.planar.aset.entity;

import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * <p>Title : ResultShortestPath </p>
 * <p>Description : DijlstraInterface 结果集合</p>
 *
 * @author huifer
 * @date 2018/12/12
 */
@Data
@ToString
public class ResultShortestPath {

    private double length;
    private List<Integer> pointsIndex;

}
