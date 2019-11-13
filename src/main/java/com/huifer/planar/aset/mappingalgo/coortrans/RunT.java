package com.huifer.planar.aset.mappingalgo.coortrans;

import com.huifer.planar.aset.mappingalgo.coortrans.lib.*;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : InverseDistanceCore </p>
 * <p>Description : coortrans 测试运行</p>
 *
 * @author huifer
 * @date 2018/11/12
 */
@Slf4j
public class RunT {

    public static void main(String[] args) {
        ReadHelper readHelper = new ReadHelper(
                "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\coortrans\\坐标数据.txt");
        AllData allData = readHelper.getData();
//        System.out.println("============= 输入值 =============");
        log.info("{}", "============= 输入值 =============");
        printdata(allData);

        //BLH -> XYZ
        // 大地测量转换为笛卡尔坐标
        Position position = new Position(allData.ellipsoid);
        for (int i = 0; i < allData.data.size(); i++) {
            double x, y, z;
            double b = allData.data.get(i).b;
            double l = allData.data.get(i).l;
            double h = allData.data.get(i).h;
            double[] cartesian = position.geodetic2Cartesian(b, l, h);
            allData.data.get(i).x = cartesian[0];
            allData.data.get(i).y = cartesian[1];
            allData.data.get(i).z = cartesian[2];
        }
//        System.out.println("============= 大地坐标转笛卡尔坐标系 =============");
        log.info("{}", "============= 大地坐标转笛卡尔坐标系 =============");

        printdata(allData);

        Gauss gauss = new Gauss(allData.ellipsoid, allData.l0);
        for (int i = 0; i < allData.data.size(); i++) {
            double b = allData.data.get(i).b;
            double l = allData.data.get(i).l;
            double[] xy = gauss.bl2Xy(b, l);
            allData.data.get(i).gsx = xy[0];
            allData.data.get(i).gsy = xy[1];
        }
//        System.out.println("============= 大地坐标转高斯 =============");
        log.info("{}", "============= 大地坐标转高斯 =============");
        printdata(allData);
    }

    private static void printdata(AllData allData) {
        allData.data.stream().forEach(
                so -> {
//                    System.out.println(so);
                    log.info("{}", so);
                }
        );
    }

    private static void bl2Xy(AllData allData) {
        // 高斯正算
        for (PointInfo pointInfo : allData.data) {
            String name = pointInfo.name;
            String x = Rad2Dms.rad2Str(pointInfo.b);
            String y = Rad2Dms.rad2Str(pointInfo.l);
//            System.out.println(name + " | " + x + " | " + y);
            log.info("{}\t{}\t{}\t", name, x, y);
        }
    }

}
