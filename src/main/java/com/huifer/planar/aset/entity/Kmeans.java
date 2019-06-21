package com.huifer.planar.aset.entity;

import ca.pjer.ekmeans.EKmeans;
import lombok.Data;

/**
 * <p>Title : Kmeans </p>
 * <p>Description : Kmeans 聚合</p>
 *
 * @author huifer
 * @date 2019-01-15
 */
@Data
public class Kmeans {

    /***
     * 聚合总量
     */
    private int n;
    /***
     * 数据集合
     */
    private double[][] points;
    /**
     * 簇族数量
     */
    private int k;

    /**
     * 中心集合
     */
    private double[][] centroids;
    /**
     * 分组后组id
     */
    private int[] assignments;

    public Kmeans(double[][] points, int k) {
        this.n = points.length;
        this.points = points;
        this.k = k;
        this.centroids = new double[k][2];
        init();
    }

    /**
     * 求得分组数据
     */
    public void init() {
        EKmeans eKmeans = new EKmeans(centroids, points);
        eKmeans.setIteration(128);
        eKmeans.setEqual(true);
        eKmeans.setDistanceFunction(EKmeans.EUCLIDEAN_DISTANCE_FUNCTION);
        eKmeans.run();
        int[] assignments = eKmeans.getAssignments();
        this.assignments = assignments;
        double[][] centroids = eKmeans.getCentroids();
        this.centroids = centroids;
    }

}
