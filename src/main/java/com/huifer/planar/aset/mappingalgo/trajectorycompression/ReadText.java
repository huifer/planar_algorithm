package com.huifer.planar.aset.mappingalgo.trajectorycompression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : ReadText </p>
 * <p>Description : 读文件</p>
 *
 * @author huifer
 * @date 2018/10/19
 */
public class ReadText {

    public static void main(String[] argv) {
        String filePath = "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\trajectorycompression\\read.txt";
    }

    public List<Point> readTxtFile(String filePath) {

        List<Point> pointList = new ArrayList<>();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                //考虑到编码格式
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String[] pointStr = lineTxt.split("\t");
                    Point p = new Point(Double.valueOf(pointStr[0]), Double.valueOf(pointStr[1]),
                            Double.valueOf(pointStr[2]));
                    pointList.add(p);
                }
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return pointList;
    }
}
