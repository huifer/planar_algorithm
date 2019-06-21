package com.huifer.planar.aset.mappingalgo.coortrans;

import com.huifer.planar.aset.mappingalgo.coortrans.lib.Ellipsoid;
import com.huifer.planar.aset.mappingalgo.coortrans.lib.*;
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

    private AllData data;

    public AllData getData() {
        return data;
    }

    public ReadHelper(String path) {
        List<String> textData = ReadHelper.readFileHelper(path);
        this.data = getDataForTxt(textData);


    }


    public static void main(String[] args) {

        ReadHelper readHelper = new ReadHelper(
                "E:\\idea_project\\cloud\\src\\main\\java\\com\\te\\coortrans\\坐标数据.txt");
        AllData data = readHelper.getData();
//        System.out.println(data);
        log.info("{}", data);
    }

    private static AllData getDataForTxt(List<String> textData) {

        AllData data = new AllData();

        double a = Double.parseDouble(textData.get(0).split(",")[1]);
        double invF = Double.parseDouble(textData.get(1).split(",")[1]);
        double l0 = Double.parseDouble(textData.get(2).split(",")[1]);

//        System.out.println("a :\t" + a);
//        System.out.println("invF:\t" + invF);
//        System.out.println("l0:\t" + l0);
        // 椭球
        Ellipsoid ellipsoid = new Ellipsoid(a, invF);
        // 设置data参数
        data.ellipsoid = ellipsoid;
        data.l0 = Rad2Dms.dms2rad(l0);
        List<PointInfo> pointData = new ArrayList<>();
        for (int i = 3; i < textData.size(); i++) {
            String[] s = textData.get(i).split(",");
            PointInfo p = new PointInfo();
            p.name = s[0];
            p.b = Rad2Dms.dms2rad(Double.parseDouble(s[1]));
            p.l = Rad2Dms.dms2rad(Double.parseDouble(s[2]));
            p.h = Double.parseDouble(s[3]);
            pointData.add(p);
        }
        data.data = pointData;

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
            log.error("{}",e);
            e.printStackTrace();
        }
        return null;
    }


}
