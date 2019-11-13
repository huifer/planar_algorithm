package com.huifer.planar.aset.mappingalgo.zongduanmian.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : AllData </p>
 * <p>Description : 数据集</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
public class AllData {

    /**
     * 离散点集合
     * {@link com.huifer.planar.aset.mappingalgo.zongduanmian.lib.PointInfo}
     */
    public List<PointInfo> pointInfoList;

    /***
     * 参考高程
     */
    public double h0;

    /***
     * 横断面集合
     * {@link com.huifer.planar.aset.mappingalgo.zongduanmian.lib.CrossSection}
     */
    public List<CrossSection> crossSections;

    /**
     * 纵断面
     * {@link com.huifer.planar.aset.mappingalgo.zongduanmian.lib.VerticleSection}
     */
    public VerticleSection verticleSection;


    public AllData() {
        pointInfoList = new ArrayList<>();
        crossSections = new ArrayList<>();
        verticleSection = new VerticleSection();
    }

    @Override
    public String toString() {
        return "AllData{" +
                "pointInfoList=" + pointInfoList +
                ", h0=" + h0 +
                ", crossSections=" + crossSections +
                ", verticleSection=" + verticleSection +
                '}';
    }
}
