package com.huifer.planar.aset.algo.impl.concave;

import com.huifer.planar.aset.algo.impl.concave.ConcaveSplit.Radial;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LineEquationTest {


    @Test
    public void testCalcXY() {

//        POINT (88066.9358577728 73719.719751358)	POINT (88066.9358577728 73720.719751358)
//        POINT (88064.0209026337 73719.926618576)	POINT (88065.280916214 73720.9421520233)
//        POINT (88066.9358577728 73719.719751358)	POINT (88066.9358577728 73720.719751358)
//        POINT (88068.1958713531 73722.1269397736)	POINT (88067.0298862457 73721.2430477142)
//        POINT (88065.0895786285 73721.8824596405)	POINT (88065.0895786285 73720.8824596405)
//        88063.8328418732
//        73719.6257190704
//        88068.1958713531
//        73722.1269397736
        Point start = new GeometryFactory().createPoint(new Coordinate(88064.0209026337, 73719.926618576));
        Point end = new GeometryFactory().createPoint(new Coordinate(88065.280916214, 73720.9421520233));
        Radial radial = new Radial(start, end);
//        double y = radial.calcY(88064.123424185);
        double A = end.getY() - start.getY();
        double B = start.getX() - end.getX();
        double C = end.getX() * start.getY() - start.getX() * end.getY();

        // 一般直线方程 ： Ax + By + C = 0
        System.out.println(radial.getA());
        System.out.println(radial.getB());
        System.out.println(radial.getC());
        System.out.println("=======================================");
        System.out.println(A);
        System.out.println(B);
        System.out.println(C);
        double v = radial.calcYY(88063.8328418732);
        System.out.println(v);

    }


}
