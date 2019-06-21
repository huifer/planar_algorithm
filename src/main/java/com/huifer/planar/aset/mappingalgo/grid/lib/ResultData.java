package com.huifer.planar.aset.mappingalgo.grid.lib;

/**
 * <p>Title : ResultData </p>
 * <p>Description : 结果数据</p>
 *
 * @author huifer
 * @date 2018/11/15
 */
public class ResultData {

    /**
     * 基准高程
     */
    public double baseHeight;

    /**
     * 领域比例
     */
    public double neighbor;

    /**
     * 间距
     */
    public double inteval;

    /**
     * 凸包
     */
    public Convex convex;

    /**
     * 网格
     */
    public Grid grid;

    /**
     * 体积
     */
    public double volumn;

    /**
     * 列
     */
    public Integer col;

    /**
     * 行
     */
    public Integer row;


    @Override
    public String toString() {
        return "ResultData{" +
                "baseHeight=" + baseHeight +
                ", neighbor=" + neighbor +
                ", inteval=" + inteval +
                ", convex=" + convex +
                ", grid=" + grid +
                ", volumn=" + volumn +
                ", col=" + col +
                ", row=" + row +
                '}';
    }
}
