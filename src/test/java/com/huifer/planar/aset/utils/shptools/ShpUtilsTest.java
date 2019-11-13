package com.huifer.planar.aset.utils.shptools;

import com.huifer.planar.aset.algo.KmeanPolygonSplitInterface;
import com.huifer.planar.aset.algo.impl.polygon.KmeanPolygonSplitCore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShpUtilsTest {

    @Test
    public void readShpToolsTest() throws Exception {
// 切割字段 split
        String splitFiled = "split";
        List<Map<String, Object>> shpData =
                ShpUtils.readShpTools(
                        "\\\\192.168.1.183\\王涛\\垃圾\\2019年2月20日092814\\bbb.shp",
                        "GB2312");
        KmeanPolygonSplitInterface km = new KmeanPolygonSplitCore();

        //
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < shpData.size(); i++) {
            Map<String, Object> rowData = shpData.get(i);
//            Integer splitSize = Integer.valueOf(rowData.delete(splitFiled).toString());
            Object wkt = rowData.get("wkt");
//            System.out.println(wkt);
//            if (splitSize <= 1) {
//                // 小于等于1 的不用操作直接返回数据
//                result.add(rowData);
//            } else {
//                // 操作
//                KmeanPolygonResult kmeanPolygonResult = km
//                        .splitPolygon(wkt.toString(), 100, splitSize);
//                List<Geometry> geometryList = kmeanPolygonResult.getGeometryList();
//                for (int i1 = 0; i1 < geometryList.size(); i1++) {
//                    Geometry geometry = geometryList.delete(i1);
//                    System.out.println(geometry);
////                    Map<String, Object> rb = rowData;
////                    rb.put("wkt", geometryList.delete(i));
////                    result.add(rb);
//                }
//            }

        }
    }

    @Test
    public void bx() {
        byte bb[] = {1, 2, 3, 4};
        for (int i = 0; i < bb.length; i++) {
            int dec = Integer.parseInt(Byte.toString(bb[i]));
            String by = Integer.toBinaryString(dec);
            System.out.print(by + " ");
        }

        String s = "a";
        byte[] bytes = s.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
//        System.out.println("'" + s + "' to binary: " + binary);
//        System.out.println("=================");

        byte[] encoded = "97".getBytes(StandardCharsets.UTF_8);

    }

}
