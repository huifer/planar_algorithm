package com.huifer.planar.aset.algo;

import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.SortPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title : LineOverInterface </p>
 * <p>Description : 直线两侧排序</p>
 *
 * @author huifer
 * @date 2018/12/17
 */
public interface LineOverInterface {

    /**
     * 直线两侧排序
     * @param line 直线
     * @param points  点列表
     * @return 排序后结果
     */
    Map<String, List<SortPoint>> simpleLineSortPoint(MyLine line, ArrayList<MyPoint> points);

    /**
     * 直线交点
     * @param l1 直线1
     * @param l2 直线2
     * @return 相交点 {@link com.huifer.planar.aset.entity.MyPoint}
     */
    MyPoint intersectPoint(MyLine l1, MyLine l2);

    /**
     * 求直线外一点到直线上的投影点
     * @param line 直线
     * @param out  直线外一点
     * @return 投影点 {@link com.huifer.planar.aset.entity.MyPoint}
     */
    MyPoint getProjectivePoint(MyLine line, MyPoint out);


}
