package com.huifer.planar.aset.mappingalgo.zongduanmian;

import com.huifer.planar.aset.mappingalgo.zongduanmian.lib.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : ReadHelper </p>
 * <p>Description : 读文件工具</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
@Slf4j
public class ReadHelper {

    /***
     * 点操作
     */
    private AllData allData;

    public ReadHelper(String path) {
        List<String> textData = ReadHelper.readFileHelper(path);
        this.allData = getPointList(textData);
    }

    public AllData getAllData() {
        return allData;
    }

    public static void main(String[] args) {

        ReadHelper readHelper = new ReadHelper(
                "E:\\idea_project\\cloud\\src\\main\\java\\com\\te\\Zongduanmian\\断面数据.txt");
    }

    /**
     * 获取每一行的 点信息
     */
    private static AllData getPointList(List<String> textData) {
        AllData allData = new AllData();
        // 获取H0
        String[] h0 = textData.get(0).split(",");
        allData.h0 = Double.parseDouble(h0[1]);
        // 把K 写入关键数据中
        textData.forEach(
                s ->
                {
                    if (s.contains("K")) {
                        String[] kkkkkkkkkk = s.split(",");
                        if (kkkkkkkkkk.length == 4) {
                            PointInfo p1 = new PointInfo();
                            p1.name = kkkkkkkkkk[0];
                            p1.xx = Double.parseDouble(kkkkkkkkkk[1]);
                            p1.yy = Double.parseDouble(kkkkkkkkkk[2]);
                            p1.hh = Double.parseDouble(kkkkkkkkkk[3]);
                            p1.tag = "Vkey";
                            allData.verticleSection.kPoint.add(p1);
                        }
                    }
                }
        );

        // 得到所有点
        PointInfo p;
        for (int i = 2; i < textData.size(); i++) {
            String[] s = textData.get(i).split(",");
            p = new PointInfo();
            p.name = s[0];
            p.xx = Double.parseDouble(s[1]);
            p.yy = Double.parseDouble(s[2]);
            p.hh = Double.parseDouble(s[3]);
            allData.pointInfoList.add(p);
        }

        return allData;
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
            log.error("{}", e);
            e.printStackTrace();
        }
        return null;
    }


}
