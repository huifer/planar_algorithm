package com.huifer.planar.aset.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;


/**
 * <p>Title : AdjacentMatrixUtil </p>
 * <p>Description : 邻接矩阵工具</p>
 *
 * @author huifer
 * @date 2019-03-05
 */
public class AdjacentMatrixUtil {


    /**
     * 点的连接情况
     *
     * @param adjacencyMatrix 邻接矩阵
     * @return 连接情况 0[1,2];1[2,3]
     */
    public static List<List<Integer>> getAdjacencyList(
            List<List<Boolean>> adjacencyMatrix) {

        List<List<Integer>> oc = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.size(); i++) {
            Vector v = new Vector();
            for (int j = 0; j < adjacencyMatrix.get(i).size(); j++) {
                if (adjacencyMatrix.get(i).get(j)) {
                    v.add(new Integer(j));
                }
            }
            oc.add(new ArrayList(v.size()));
            for (int j = 0; j < v.size(); j++) {
                Integer in = (Integer) v.get(j);
                oc.get(i).add(in.intValue());
            }
        }

        return oc;

    }


    /**
     * 矩阵BST
     *
     * @param ls 矩阵
     * @return bst结果
     */
    public static ArrayList<Integer> getBST(List<List<Integer>> ls) {
        int size = ls.size();
        ArrayList<Integer> result = new ArrayList<>();
        Queue<Integer> theQueue = new LinkedList<>();

        Boolean[] b = new Boolean[size];
        for (int i = 0; i < size; i++) {
            b[i] = false;
        }
        int startNodeIndex = 3;

        theQueue.offer(startNodeIndex);
        result.add(startNodeIndex);
        b[startNodeIndex] = true;
        while (!theQueue.isEmpty()) {
            Integer poll = theQueue.poll();
            for (int i = 0; i < size; i++) {
                if (Integer.valueOf(ls.get(poll).get(i)) > 0 && b[i] == false && poll != i) {
                    theQueue.offer(i);
                    b[i] = true;
                    result.add(i);
                }
            }
        }
        return result;
    }


    /**
     * 邻接矩阵构造器
     *
     * @param points 点列表
     * @param lines 连接线列表
     * @return 邻接矩阵
     */
    public static List<List<Integer>> createAdjacentMatrix(List<Point> points,
            List<LineString> lines) {

        int n = points.size();
        List<List<Integer>> matrix = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>(n));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.get(i).add(0);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            Point startPoint = lines.get(i).getStartPoint();
            Point endPoint = lines.get(i).getEndPoint();
            int index1 = queryIndex(points, startPoint);
            int index2 = queryIndex(points, endPoint);
            matrix.get(index1).set(index2, 1);
            matrix.get(index2).set(index1, 1);

        }
        return matrix;
    }


    public static List<List<Boolean>> createAdjacentMatrixBoolean(List<Point> points,
            List<LineString> lines) {
        int n = points.size();
        List<List<Boolean>> matrix = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>(n));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.get(i).add(false);
            }
        }
        for (int i = 0; i < lines.size(); i++) {
            Point startPoint = lines.get(i).getStartPoint();
            Point endPoint = lines.get(i).getEndPoint();
            int index1 = queryIndex(points, startPoint);
            int index2 = queryIndex(points, endPoint);
            matrix.get(index1).set(index2, true);
            matrix.get(index2).set(index1, true);
        }
        return matrix;
    }


    /**
     * 搜索当前点
     *
     * @param points 点集合
     * @param find 需要寻找的点
     * @return 寻找点的索引
     */
    private static int queryIndex(List<Point> points, Point find) {
        int index = 0;
        for (int i = 0; i < points.size(); i++) {
            if (find.equalsExact(points.get(i))) {
                index = i;
            }
        }
        return index;
    }


    static class Graph {

        /**
         * 邻接矩阵
         */
        private List<List<Integer>> matrix;


        /**
         * 顶点集合
         */
        private List vertex;


        /**
         * 顶点数量
         */
        private int vetCount;

        /**
         * 有没有下一个点了 ， 如果没有结束
         */
        private boolean doNext = false;

        /**
         * 路径结果集合
         */
        private List<List> result = new ArrayList<>();


        /**
         * 构造器
         *
         * @param matrix 邻接矩阵
         * @param vertex 顶点列表
         */
        public Graph(List<List<Integer>> matrix, List vertex) {
            this.matrix = matrix;
            this.vertex = vertex;
            this.vetCount = matrix.size();
        }

        /**
         * 判断这个路径是否在结果集合中
         *
         * @param nodeList 已有结果
         * @param edge 路径边
         * @return yes/no
         */
        private static boolean inResult(List<List> nodeList, List edge) {
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).containsAll(edge)) {
                    return true;
                }
            }
            return false;
        }

        private void dfs(int begin, List path) {
            path.add(vertex.get(begin));
            int rollBackNumb = -1;
            for (int i = 0; i < vetCount; i++) {
                if (matrix.get(begin).get(i) > 0) {
                    path.add(vertex.get(i));
                    // 确认路径是否在结果中
                    if (inResult(result, path)) {
                        path.remove(vertex.get(i));
                        rollBackNumb = i;
                        continue;
                    } else {
                        path.remove(vertex.get(i));
                        dfs(i, path);
                    }
                }
                if (doNext) {
                    return;
                }


            }
            if (rollBackNumb > -1) {
                dfs(rollBackNumb, path);
            } else {
                doNext = true;
            }

        }

        /**
         * dfs的遍历开始
         */
        public List<List> startDFSfind() {
            return result;
        }

        /**
         * 求分支总量
         *
         * @return 分支总量
         */
        private int countPathSize() {
            int[] intArr = new int[vetCount];
            for (int i = 0; i < vetCount; i++) {
                for (int j = 0; j < vetCount; j++) {
                    if (matrix.get(i).get(j) > 0) {
                        intArr[j]++;
                    }
                }
            }
            int num = 1;
            for (int i = 0; i < vetCount; i++) {
                if (intArr[i] > 1) {
                    num++;
                }
            }
            return num;

        }
    }


}

@Data
@NoArgsConstructor
@ToString
class Node {

    private String name;
    private ArrayList<Node> linkNode = new ArrayList<>();

}

@Data
@NoArgsConstructor
class NodeSort {

    /**
     * 节点集合
     */
    public static Stack<Node> nodeStack = new Stack<>();
    /**
     * 路径集合
     */
    public static ArrayList<Object[]> res = new ArrayList<>();

    /***
     * 是否在堆栈中
     * @param node  节点
     * @return yes/ no
     */
    public static boolean inNodeStack(Node node) {
        Iterator<Node> iterator = nodeStack.iterator();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (node.equals(next)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 堆栈中的节点组成具体的路径
     */
    public static void showPath() {
        Object[] objects = nodeStack.toArray();
        for (int i = 0; i < objects.length; i++) {
            Node nd = (Node) objects[i];
            if (i < objects.length - 1) {
                System.out.print(nd.getName() + "--->");
            } else {
                System.out.print(nd.getName());
            }
        }

        res.add(objects);
        System.out.println("\n");
    }


    /**
     * findPath
     *
     * @param cNode 当前起始点
     * @param pNode 当前起始点的上一个节点
     * @param sNode 开始点
     * @param eNode 结束点
     */
    public static boolean findPath(Node cNode, Node pNode, Node sNode, Node eNode) {
        Node nNode = null;
        if (cNode != null && pNode != null && cNode == pNode) {
            return false;
        }
        if (cNode != null) {
            int i = 0;
            nodeStack.push(cNode);
            if (cNode == eNode) {
                showPath();
                return true;
            } else {
                nNode = cNode.getLinkNode().get(i);
                while (nNode != null) {
                    if (pNode != null && (nNode == sNode || nNode == pNode || inNodeStack(nNode))) {
                        i++;
                        if (i >= cNode.getLinkNode().size()) {
                            nNode = null;
                        } else {
                            nNode = cNode.getLinkNode().get(i);
                        }
                        continue;
                    }
                    if (findPath(nNode, cNode, sNode, eNode)) {
                        nodeStack.pop();
                    }
                    i++;
                    if (i >= cNode.getLinkNode().size()) {
                        nNode = null;
                    } else {
                        nNode = cNode.getLinkNode().get(i);
                    }

                }
            }
            nodeStack.pop();
            return false;
        } else {
            return false;
        }
    }

}
