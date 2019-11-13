package com.huifer.planar.aset.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

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
