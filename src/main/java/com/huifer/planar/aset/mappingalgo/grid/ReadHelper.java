package com.huifer.planar.aset.mappingalgo.grid;

import com.huifer.planar.aset.mappingalgo.grid.lib.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : ReadHelper </p>
 * <p>Description : 读文件工具</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
@Slf4j
public class ReadHelper {


    private ResultData resultData;

    /**
     * @param path     文件
     * @param neighbor 领域比例
     * @param interval 间距
     */
    public ReadHelper(String path, double neighbor, double interval) {
        List<String> textData = ReadHelper.readFileHelper(path);
        resultData = getPointList(textData, neighbor, interval);
    }

    public static void main(String[] args) {

        ReadHelper readHelper = new ReadHelper(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\grid\\GRID数据.txt", 0.4, 10);
        ResultData resultData = readHelper.getResultData();
//        System.out.println(resultData);
        log.info("grid = {}", resultData);
    }

    /**
     * 获取每一行的 点信息
     */
    private static ResultData getPointList(List<String> textData, double neighbor,
                                           double interval) {

        // 数据点信息
        List<PointInfo> pointInfos = new ArrayList<>();
        // 基准值
        double baseHight = Double.parseDouble(textData.get(0).split(",")[1]);
        // 领域比例
//        double neighbor = 0.4;
        // 间距
//        double interval = 10;

        // 第一个点用作基准值
        for (int i = 1; i < textData.size(); i++) {
            String[] s = textData.get(i).split(",");
            PointInfo pointInfo = new PointInfo();
            pointInfo.name = s[0];
            pointInfo.x = Double.parseDouble(s[1]);
            pointInfo.y = Double.parseDouble(s[2]);
            pointInfo.h = Double.parseDouble(s[3]);
            pointInfos.add(pointInfo);
        }

        // 凸包算法
        Convex convex = new Convex(pointInfos);
        // 网格计算
        Grid grid = new Grid(convex, interval);
        // 高度计算
        HeightCalc heightCalc = new HeightCalc(grid, pointInfos, neighbor);
        heightCalc.runCalc(baseHight);
        // 体积计算
        double v = GeoCalc.calcVolumn(grid, baseHight);

        ResultData data = new ResultData();
        data.baseHeight = baseHight;
        data.neighbor = neighbor;
        data.inteval = interval;
        data.convex = convex;
        data.grid = grid;
        data.volumn = v;
        data.col = grid.height;
        data.row = grid.width;

        return data;
    }

    /***
     * 读文件
     * @param path
     * @return
     */
    private static List<String> readFileHelper(String path) {
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(new FileInputStream(path), "utf-8");
            BufferedReader read = new BufferedReader(isr);
            String s = null;
            List<String> list = new ArrayList<String>();
            while ((s = read.readLine()) != null) {
                if (s.trim().length() > 1) {
                    list.add(s.trim());
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultData getResultData() {
        return resultData;
    }


}
