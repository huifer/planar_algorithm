package com.huifer.planar.aset.mappingalgo.zongduanmian;

import com.huifer.planar.aset.mappingalgo.zongduanmian.lib.AllData;
import com.huifer.planar.aset.mappingalgo.zongduanmian.lib.CrossSection;
import com.huifer.planar.aset.mappingalgo.zongduanmian.lib.HeightCal;
import com.huifer.planar.aset.mappingalgo.zongduanmian.lib.PointInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : Zongduanmian </p>
 * <p>Description : Zongduanmian 运行测试</p>
 *
 * @author huifer
 * @date 2018/11/13
 */
@Slf4j
public class Zongduanmian {

    public static void main(String[] args) {
        ReadHelper readHelper = new ReadHelper(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\Zongduanmian\\断面数据.txt");
        AllData allData = readHelper.getAllData();

        HeightCal heightCal = new HeightCal(allData.pointInfoList);

        allData.verticleSection.calcVerticleSection(heightCal, allData.h0);
        PointInfo p1 = allData.verticleSection.kPoint.get(0);
        PointInfo p2 = allData.verticleSection.kPoint.get(1);
        PointInfo p3 = allData.verticleSection.kPoint.get(2);

        CrossSection c1 = new CrossSection(p1, p2, allData.verticleSection.direction1);
        CrossSection c2 = new CrossSection(p2, p3, allData.verticleSection.direction2);

        c1.calcDataForCrossSection(heightCal, allData.h0);
        c2.calcDataForCrossSection(heightCal, allData.h0);

        c1.kPoint.name = "m";
        c2.kPoint.name = "n";
        allData.crossSections.add(c1);
        allData.crossSections.add(c2);
//        System.out.println(allData);
        log.info("纵断面 = {}", allData);
    }

}
