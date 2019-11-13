package com.huifer.planar.aset.algo.impl.line;

import com.huifer.planar.aset.algo.DijlstraInterface;
import com.huifer.planar.aset.entity.ResultShortestPath;

import java.util.ArrayList;

/**
 * 迪杰斯特拉算法实现
 *
 * @author huifer
 */
public class SimpleDijkstra implements DijlstraInterface {


    /**
     * 指定起点和终点，求最短路径
     */
    @Override
    public final ResultShortestPath shortestPathByStartAndEnd(ArrayList<ArrayList<Double>> cost,
                                                              int startPoint, int endPoint) {

        ResultShortestPath resultShortestPath = new ResultShortestPath();

        double sig = 0;
        if (cost == null || startPoint < 0 || startPoint > cost.size() - 1 || endPoint < 0
                || endPoint > cost.size() - 1) {
            sig = 0.0;
            resultShortestPath.setLength(0);
            return resultShortestPath;
        }
        ArrayList<Integer> singlePath = new ArrayList<Integer>();
        if (startPoint == endPoint) {
            sig = 0.0;
            singlePath.add(startPoint);
            resultShortestPath.setLength(0);
            resultShortestPath.setPointsIndex(singlePath);
            return resultShortestPath;
        }
        int n = cost.size();
        ArrayList<ArrayList<Integer>> path = getPathList(n);
        ArrayList<Double> dist = new ArrayList<Double>(n);
        //是否找到最短路径的标志数组
        ArrayList<Integer> arrayFlag = new ArrayList<Integer>(n);
        //初始化
        for (int i = 0; i < n; i++) {
            arrayFlag.add(0);
            dist.add(cost.get(startPoint).get(i));
            path.get(i).add(startPoint);
        }
        arrayFlag.set(startPoint, 1);
        int count = 1;
        int u = startPoint;
        //控制循环n-1次
        while (count < n) {
            double temp = 1000000000;
            //寻找没找到最短路径的当前路径权值最小的顶点
            for (int i = 0; i < n; i++) {
                if (arrayFlag.get(i) == 0 && dist.get(i) < temp) {
                    u = i;
                    temp = dist.get(i);
                }
            }
            arrayFlag.set(u, 1);
            path.get(u).add(u);
            if (u == endPoint) {
                break;
            }
            //寻找与u直接联通但没有确定最短路径的顶点,修改权值和路径
            for (int i = 0; i < n; i++) {
                if (arrayFlag.get(i) == 0 && dist.get(u) + cost.get(u).get(i) < dist.get(i)) {
                    dist.set(i, dist.get(u) + cost.get(u).get(i));
                    path.get(i).clear();
                    for (int k = 0; k < path.get(u).size(); k++) {
                        path.get(i).add(path.get(u).get(k));
                    }
                }
            }
            count++;
        }

        resultShortestPath.setLength(dist.get(endPoint));
        resultShortestPath.setPointsIndex(path.get(endPoint));

        return resultShortestPath;
    }


    /**
     * 单点所有最短距离
     */
    @Override
    public final ArrayList<ResultShortestPath> shortestPathByStart(
            ArrayList<ArrayList<Double>> cost, int startPoint) {

        ArrayList<ResultShortestPath> allRes = new ArrayList<>();
        ResultShortestPath oneRes = new ResultShortestPath();

        if (cost == null || startPoint < -1 || startPoint > cost.size()) {
            oneRes.setLength(0);
            oneRes.setPointsIndex(null);
            allRes.add(oneRes);
            return allRes;
        }

        int n = cost.size();
        // 存储路径点
        ArrayList<ArrayList<Integer>> path = getPathList(n);
        // 存储路径长度
        ArrayList<Double> pathLength = new ArrayList<>(n);

        //是否找到最短路径的标志数组
        ArrayList<Integer> arrayFlag = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            arrayFlag.add(0);
            pathLength.add(cost.get(startPoint).get(i));
            path.get(i).add(startPoint);
        }
        arrayFlag.set(startPoint, 1);
        int count = 1;
        int u = startPoint;
        //控制循环n-1次
        while (count < n) {
            double temp = 1000000000;
            // 寻找没找到最短路径的当前路径权值最小的顶点
            for (int i = 0; i < n; i++) {
                if (arrayFlag.get(i) == 0 && pathLength.get(i) < temp) {
                    u = i;
                    temp = pathLength.get(i);
                }
            }
            arrayFlag.set(u, 1);
            path.get(u).add(u);
            // 寻找与u直接联通但没有确定最短路径的顶点,修改权值和路径
            for (int i = 0; i < n; i++) {
                if (arrayFlag.get(i) == 0
                        && pathLength.get(u) + cost.get(u).get(i) < pathLength.get(i)) {
                    pathLength.set(i, pathLength.get(u) + cost.get(u).get(i));
                    path.get(i).clear();
                    for (int k = 0; k < path.get(u).size(); k++) {
                        path.get(i).add(path.get(u).get(k));
                    }
                }
            }
            count++;
        }

        // 处理存放结果
        for (int i = 0; i < path.size(); i++) {
            oneRes.setPointsIndex(path.get(i));
            oneRes.setLength(pathLength.get(i));
            allRes.add(oneRes);
        }

        return allRes;
    }


    private ArrayList<ArrayList<Integer>> getPathList(int n) {
        ArrayList<ArrayList<Integer>> path = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            path.add(new ArrayList<Integer>());
        }
        return path;
    }
}
