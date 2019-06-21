package com.huifer.planar.aset.utils.kriging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>Title : ReadFile </p>
 * <p>Description : 读文件</p>
 *
 * @author huifer
 * @date 2019-04-19
 */
public class ReadFile {

    private static final String TEST_PATH = "E:\\mck\\bg\\data_status\\src\\main\\java\\com\\huifer\\data\\idw\\text.txt";

    public static void main(String[] args) {
        ArrayList<ArrayList<Kpoint>> arrayLists = readFile();
        System.out.println();
    }

    public static ArrayList<ArrayList<Kpoint>> readFile() {
        File f = new File(TEST_PATH);
        BufferedReader reader = null;

        ArrayList<ArrayList<Kpoint>> rs = null;

        try {

            reader = new BufferedReader(new FileReader(f));

            String tem = null;
            int count = 0;
            rs = new ArrayList<>();
            ArrayList<Kpoint> points = null;
            while ((tem = reader.readLine()) != null) {
                String[] split = tem.split(",");
                points = new ArrayList<Kpoint>();
                count++;
                points.add(new Kpoint(Double.valueOf(split[1]), Double.valueOf(split[2]),
                        Double.valueOf(split[3])));
                rs.add(points);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return rs;


    }


}
