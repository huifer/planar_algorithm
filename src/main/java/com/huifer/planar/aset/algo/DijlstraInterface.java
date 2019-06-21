package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.ResultShortestPath;
import java.util.ArrayList;

/**
 * DijlstraInterface 接口
 *
 * @author huifer
 */
public interface DijlstraInterface {

    /**
     * 指定起点和终点，求最短路径
     *
     * @param cost 点列表
     * @param startPoint 起点
     * @param endPoint 终点
     * @return 路径值
     */
    ResultShortestPath shortestPathByStartAndEnd(ArrayList<ArrayList<Double>> cost, int startPoint,
            int endPoint);

    /**
     * 单点所有最短距离
     *
     * @param cost 点列表
     * @param startPoint 指定点
     * @return 路径值
     */
    ArrayList<ResultShortestPath> shortestPathByStart(
            ArrayList<ArrayList<Double>> cost, int startPoint);

}
