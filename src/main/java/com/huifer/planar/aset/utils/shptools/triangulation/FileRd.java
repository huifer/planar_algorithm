package com.huifer.planar.aset.utils.shptools.triangulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : FileRd </p>
 * <p>Description : </p>
 *
 * @author huifer
 * @date 2019-02-22
 */
public class FileRd {

    public static void main(String[] args) throws Exception{
        String path = "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\utils\\shptools\\triangulation\\1c.txt";
        readTxtFile(path);
    }

    public static List<Vector2D> readTxtFile(String filePath) {
        List<Vector2D> pointSet = new ArrayList<>();
        try {
            //考虑到编码格式
            InputStreamReader read = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String[] pointStr = lineTxt.split("\t");
//                System.out.println(pointStr[0]+"\t"+pointStr[1]);
                Vector2D v = new Vector2D(Double.valueOf(pointStr[0]), Double.valueOf(pointStr[1]));
                pointSet.add(v);
            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
return pointSet;
    }

}
