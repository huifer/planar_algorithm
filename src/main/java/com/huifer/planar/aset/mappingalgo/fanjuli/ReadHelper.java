package com.huifer.planar.aset.mappingalgo.fanjuli;

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
public class ReadHelper {


    private List<MyPoint> points = new ArrayList<>();


    public ReadHelper(String path) {
        List<String> textData = ReadHelper.readFileHelper(path);
        this.points = getPointList(textData);
    }

    public static void main(String[] args) {

        ReadHelper readHelper = new ReadHelper(
                "E:\\idea_project\\cloud\\src\\main\\java\\com\\te\\fanjuli\\测站坐标.txt");
        List<MyPoint> points = readHelper.getPoints();
    }

    /**
     * 获取每一行的 点信息
     */
    private static List<MyPoint> getPointList(List<String> textData) {
        List<MyPoint> points = new ArrayList<>();
        for (int i = 0; i < textData.size(); i++) {
            String[] s = textData.get(i).split(",");
            MyPoint point = new MyPoint(
                    s[0],
                    Double.parseDouble(s[1]),
                    Double.parseDouble(s[2]),
                    Double.parseDouble(s[3])
            );

            points.add(point);
        }
        return points;
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

    public List<MyPoint> getPoints() {
        return points;
    }


}
