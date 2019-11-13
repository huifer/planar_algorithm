package com.huifer.planar.aset.algo.impl;

import com.huifer.planar.aset.algo.VoronoiInterface;
import com.huifer.planar.aset.algo.impl.polygon.VoronoiInterfaceImpl;
import com.huifer.planar.aset.utils.shptools.overlay.Operation;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoronoiInterfaceImplTest {

    private VoronoiInterface vo = new VoronoiInterfaceImpl();
    private Operation op = new Operation();
    private List<Coordinate> coords = new ArrayList<>();

    @Before
    public void initData() throws ParseException {
        int xmin = 0, xmax = 1000;
        int ymin = 0, ymax = 1000;
        Random random = new Random();
        List<Coordinate> coords = new ArrayList<Coordinate>();
        for (int i = 0; i < 600000; i++) {
            int x = random.nextInt(xmax) % (xmax - xmin + 1) + xmin;
            int y = random.nextInt(ymax) % (ymax - ymin + 1) + ymin;
            Coordinate coord = new Coordinate(x, y, i);
            coords.add(coord);
        }
        this.coords = coords;
    }


    @Test
    public void voronoi() {
        long startTime = System.currentTimeMillis();
        List<Geometry> voronoi = vo.voronoi(coords);
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        System.out.println(voronoi.size());
//        voronoi.forEach(s -> System.out.println(s));
    }

    @Test
    public void delaunay() throws ParseException {
        List<Geometry> delaunay = vo.delaunay(coords);
        String wkt = "POLYGON((411 80, 569 125, 675 238, 795 321, 917 416, 866 597, 600 699, 443 614, 500 483, 399 338, 411 80))";
        Polygon polygon = op.createPolygonByWKT(wkt);
        delaunay.forEach(
                s -> System.out.println(s)
        );
    }
}
