package com.huifer.planar.aset.utils.simplecycles;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * <p>Title : SimpleResult </p>
 * <p>Description : 闭环搜索结果存放 </p>
 * <p>https://en.wikipedia.org/wiki/Johnson%27s_algorithm</p>
 *
 * @author huifer
 * @date 2019-03-07
 */
@Data
public class SimpleResult {

    /**
     * 当前节点的闭环
     */
    private Set nodeOfScc = null;
    /**
     * 当前点的邻接矩阵
     */
    private List<Vector> adjList = null;
    /**
     * 当前节点索引
     */
    private int lowesNodeIndex = -1;

    public SimpleResult(List<Vector> adjList, int lowesNodeIndex) {
        this.adjList = adjList;
        this.lowesNodeIndex = lowesNodeIndex;
        this.nodeOfScc = new HashSet();

        if (this.adjList != null) {
            for (int i = 0; i < this.adjList.size(); i++) {
                if (this.adjList.get(i).size() > 0) {
                    this.nodeOfScc.add(new Integer(i));
                }
            }
        }
    }

}
