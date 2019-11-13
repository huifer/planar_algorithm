package com.huifer.planar.aset.utils.simplecycles;

import com.huifer.planar.aset.utils.AdjacentMatrixUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * <p>Title : SearchCycles </p>
 * <p>Description : 搜索所有的闭环</p>
 * <p>https://en.wikipedia.org/wiki/Johnson%27s_algorithm</p>
 *
 * @author huifer
 * @date 2019-03-07
 */
public class SearchCycles {

    /**
     * 闭环列表
     */
    private List cycles = null;
    /**
     * 邻接矩阵
     */
    private List<List<Integer>> adjList = null;
    /**
     * 节点
     */
    private List graphNodes = null;
    /**
     * johnson算法总的是否阻塞
     */
    private List<Boolean> block = null;
    /**
     * johnson 算法b 数组
     */
    private Vector[] B = null;
    /**
     * johnson 算法的节点对战
     */
    private Vector stack = null;

    /**
     * 构造器
     *
     * @param matrix     邻接矩阵
     * @param graphNodes 节点列表
     */
    public SearchCycles(List<List<Boolean>> matrix, List graphNodes) {
        this.graphNodes = graphNodes;
        this.adjList = AdjacentMatrixUtil.getAdjacencyList(matrix);
    }


    /**
     * 是否阻塞的初始化全部定义成flase
     *
     * @param ic 数组大小
     * @return 长度为ic的全是false的数组
     */
    private List<Boolean> initBlock(int ic) {
        List<Boolean> res = new ArrayList<>();
        for (int i = 0; i < ic; i++) {
            res.add(false);
        }
        return res;
    }

    /**
     * 找到闭环的结果并返回
     *
     * @return 环
     */
    public List getNodeCycles() {
        this.cycles = new Vector();
        this.block = initBlock(this.adjList.size());
        this.B = new Vector[this.adjList.size()];

        this.stack = new Vector();
        SimpleCycles simpleCycles = new SimpleCycles(this.adjList);
        int s = 0;
        while (true) {
            SimpleResult result = simpleCycles.getAdjacencyList(s);
            if (result != null && result.getAdjList() != null) {
                List<Vector> scc = result.getAdjList();
                s = result.getLowesNodeIndex();
                for (int i = 0; i < scc.size(); i++) {
                    if ((scc.get(i) != null) && (scc.get(i).size() > 0)) {
                        this.block.set(i, false);
//                        this.B.get(i).add(new Vector());
                        this.B[i] = new Vector();
                    }
                }
                this.findCycles(s, s, scc);
                s++;
            } else {
                break;

            }
        }
        return this.cycles;
    }


    /**
     * 从node开始扫描阻塞节点
     *
     * @param node 节点
     */
    private void unBlock(int node) {
        this.block.set(node, false);
        Vector bnode = this.B[node];

        while (bnode.size() > 0) {

            Integer o = (Integer) bnode.get(0);
            bnode.remove(0);
            if (this.block.get(o.intValue())) {
                this.unBlock(o.intValue());

            }
        }
    }

    /**
     * 找到闭环的核心算法
     *
     * @param v       点索引
     * @param s       点索引
     * @param adjList 邻接矩阵
     * @return yes : 找到闭环/no ：没有找到闭环
     */
    private boolean findCycles(int v, int s, List<Vector> adjList) {
        boolean flg = false;
        this.stack.add(new Integer(v));
        this.block.set(v, true);

        for (int i = 0; i < adjList.get(v).size(); i++) {
            int w = ((Integer) adjList.get(v).get(i)).intValue();
            if (w == s) {
                Vector cycle = new Vector();
                for (int j = 0; j < this.stack.size(); j++) {
                    int index = ((Integer) this.stack.get(j)).intValue();
                    cycle.add(this.graphNodes.get(index));
                }
                this.cycles.add(cycle);
                flg = true;
            } else if (!this.block.get(w)) {
                if (this.findCycles(w, s, adjList)) {
                    flg = true;
                }
            }
        }

        if (flg) {
            this.unBlock(v);
        } else {
            for (int i = 0; i < adjList.get(v).size(); i++) {
                int w = ((Integer) adjList.get(v).get(i)).intValue();
                if (!this.B[w].contains(new Integer(v))) {
                    this.B[w].add(new Integer(v));
                }
            }
        }
        this.stack.remove(new Integer(v));
        return flg;

    }

}
