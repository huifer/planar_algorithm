package com.huifer.planar.aset.algo;


import com.huifer.planar.aset.algo.impl.ovlayer.LineOverCore;
import com.huifer.planar.aset.entity.MyLine;
import com.huifer.planar.aset.entity.MyPoint;
import com.huifer.planar.aset.entity.SortPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LineOverCoreTest {

    @Test
    public void simpleLineSortPointTest() {
        MyLine l = new MyLine(new MyPoint(10, 0), new MyPoint(10, 10));
        MyPoint p1 = new MyPoint(7, 7);
        MyPoint p2 = new MyPoint(7, 4);
        MyPoint p3 = new MyPoint(12, 7);
        MyPoint p4 = new MyPoint(12, 4);
        ArrayList<MyPoint> ps = new ArrayList<>();
        ps.add(p1);
        ps.add(p2);
        ps.add(p3);
        ps.add(p4);
        LineOverCore lineOverCore = new LineOverCore();
        Map<String, List<SortPoint>> stringListMap = lineOverCore.simpleLineSortPoint(l, ps);
        stringListMap.forEach(
                new BiConsumer<String, List<SortPoint>>() {
                    @Override
                    public void accept(String s, List<SortPoint> sortPoints) {
                        sortPoints.forEach(a -> {
//                            System.out.println(a);

                            log.info("{}", a);
                        });
                    }
                }
        );
    }

}
