package com.huifer.planar.aset.utils.simplecycles;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * <p>Title : SimpleCycles </p>
 * <p>Description : 简单闭环</p>
 * <p>https://en.wikipedia.org/wiki/Johnson%27s_algorithm</p>
 *
 * @author huifer
 * @date 2019-03-07
 */
public class SimpleCycles {

    /**
     * 图的邻接矩阵描述
     */
    private List<List<Integer>> adjListOriginal = null;
    /**
     * 子图的邻接矩阵
     */
    private List<List<Integer>> adjList = null;
    /**
     * scc 流程值
     */
    private List<Boolean> visited = null;
    /**
     * scc 流程值
     */
    private Vector stack = null;
    private List<Integer> lowlink = null;
    /**
     * scc 流程值
     */
    private List<Integer> number = null;
    /**
     * scc 流程值
     */
    private int sccCounter = 0;
    /**
     * scc 流程值
     */
    private Vector currentSCCs = null;

    /**
     * 构造器
     *
     * @param adjListOriginal 原始邻接矩阵
     */
    public SimpleCycles(List<List<Integer>> adjListOriginal) {
        this.adjListOriginal = adjListOriginal;
    }


    /**
     * 制造一个空的子图
     *
     * @param ic 数量
     * @return 空的子图
     */
    private List<List<Integer>> initAdj(int ic) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < ic; i++) {
            res.add(new ArrayList<>());
        }
        return res;
    }

    /**
     * 给包含节点的子图创建连接列表 1[1,2,3]
     *
     * @param node 节点
     */
    private void makeAdjListSubGraph(int node) {
        adjList = initAdj(adjListOriginal.size());
        for (int i = node; i < adjList.size(); i++) {
            Vector successors = new Vector();
            for (int j = 0; j < adjListOriginal.get(i).size(); j++) {
                if (adjListOriginal.get(i).get(j) >= node) {
                    successors.add(new Integer(adjListOriginal.get(i).get(j)));
                }
            }
            if (successors.size() > 0) {
                adjList.set(i, new ArrayList<Integer>(successors.size()));
                for (int j = 0; j < successors.size(); j++) {
                    Integer in = (Integer) successors.get(j);
                    adjList.get(i).add(in.intValue());
                }
            }
        }
    }

    /**
     * 找scc中的最低点
     *
     * @return scc连接点数
     */
    private Vector getLowestIdComponent() {
        int min = adjList.size();
        Vector currScc = null;

        for (int i = 0; i < currentSCCs.size(); i++) {
            Vector scc = (Vector) currentSCCs.get(i);
            for (int i1 = 0; i1 < scc.size(); i1++) {
                Integer node = (Integer) scc.get(i1);
                if (node.intValue() < min) {
                    currScc = scc;
                    min = node.intValue();
                }
            }
        }
        return currScc;
    }

    /**
     * 找到子图的邻接矩阵
     *
     * @param nodes 节点
     * @return 子图的邻接矩阵
     */
    private List<Vector> getAdjList(Vector nodes) {
        List<Vector> lowe = null;
        if (nodes != null) {
            lowe = new ArrayList<>(adjList.size());
            for (int i = 0; i < adjList.size(); i++) {
                lowe.add(new Vector());
            }
            for (int i = 0; i < nodes.size(); i++) {
                int node = ((Integer) nodes.get(i)).intValue();
                for (int j = 0; j < adjList.get(node).size(); j++) {
                    int succ = adjList.get(node).get(j);
                    if (nodes.contains(new Integer(succ))) {
                        lowe.get(node).add(new Integer(succ));
                    }
                }
            }
        }
        return lowe;
    }

    /**
     * 初始化lowlink hh和 number的工具
     *
     * @param ic 数量
     * @return 数量为ic的list
     */
    private List<Integer> lowLinkOrNumber(int ic) {
        List<Integer> ls = new ArrayList<>();
        for (int i = 0; i < ic; i++) {
            ls.add(0);
        }
        return ls;
    }

    /**
     * 从节点node开始找到连接可能
     *
     * @param node 节点
     */
    private void getStrongConnectedComponents(int node) {
        sccCounter++;
        lowlink.set(node, sccCounter);
        number.set(node, sccCounter);
        visited.set(node, true);
        stack.add(new Integer(node));

        for (int i = 0; i < adjList.get(node).size(); i++) {
            Integer w = adjList.get(node).get(i);
            if (!visited.get(w)) {
                getStrongConnectedComponents(w);
                lowlink.set(node,
                        Math.min(lowlink.get(node), lowlink.get(w)));
            } else if (number.get(w) < this.number.get(node)) {
                if (stack.contains(new Integer(w))) {
                    lowlink.set(node,
                            Math.min(lowlink.get(node), number.get(w)));
                }
            }
        }

        if ((lowlink.get(node).intValue() == number.get(node).intValue()) && (
                this.stack.size() > 0)) {
            int nex = -1;
            Vector scc = new Vector();
            do {
                nex = ((Integer) stack.get(stack.size() - 1)).intValue();
                stack.remove(this.stack.size() - 1);
                scc.add(new Integer(nex));
            } while (number.get(nex) > number.get(node));

            if (scc.size() > 1) {
                currentSCCs.add(scc);
            }
        }
    }


    /**
     * 初始化visited的工具
     *
     * @param ic 数量
     * @return 数量为ic的list ,值为false
     */
    private List<Boolean> vist(int ic) {
        List<Boolean> booleanArrayList = new ArrayList<>();
        for (int i = 0; i < ic; i++) {
            booleanArrayList.add(false);
        }
        return booleanArrayList;
    }


    /**
     * 用于搜索连接图的工具
     * <p>
     * {节点0 ,节点0+1 , 节点0+2}
     *
     * @param node 节点0
     * @return {@link SimpleResult} 计算结果值
     */
    public SimpleResult getAdjacencyList(int node) {
        visited = vist(adjListOriginal.size());
        lowlink = lowLinkOrNumber(adjListOriginal.size());
        number = lowLinkOrNumber(adjListOriginal.size());
        stack = new Vector();
        currentSCCs = new Vector();

        makeAdjListSubGraph(node);
        for (int i = node; i < adjListOriginal.size(); i++) {
            if (!visited.get(i)) {
                getStrongConnectedComponents(i);
                Vector nodes = this.getLowestIdComponent();
                if (nodes != null &&
                        !nodes.contains(new Integer(node)) &&
                        !nodes.contains(new Integer(node + 1))
                ) {
                    return getAdjacencyList(node + 1);
                } else {
                    List<Vector> adjlist222 = getAdjList(nodes);
                    if (adjlist222 != null) {
                        for (int j = 0; j < adjListOriginal.size(); j++) {

                            if (adjlist222.get(j).size() > 0) {
                                return new SimpleResult(adjlist222, j);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


}
