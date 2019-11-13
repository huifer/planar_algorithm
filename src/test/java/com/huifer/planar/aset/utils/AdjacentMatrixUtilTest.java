package com.huifer.planar.aset.utils;

import com.huifer.planar.aset.utils.AdjacentMatrixUtil.Graph;
import com.huifer.planar.aset.utils.simplecycles.SearchCycles;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdjacentMatrixUtilTest {


    @Test
    public void createAdjacentMatrixBooleanTest() throws Exception {

        Point p0 = (Point) new WKTReader().read("POINT(1 1)");
        Point p1 = (Point) new WKTReader().read("POINT(3 1)");
        Point p2 = (Point) new WKTReader().read("POINT(2 3)");
        Point p3 = (Point) new WKTReader().read("POINT(4 4)");
        Point p4 = (Point) new WKTReader().read("POINT(3 5)");
        Point p5 = (Point) new WKTReader().read("POINT(1 5)");

        LineString l0 = new GeometryFactory().createLineString(new Coordinate[]{
                p0.getCoordinate(),
                p1.getCoordinate()
        });
        LineString l1 = new GeometryFactory().createLineString(new Coordinate[]{
                p2.getCoordinate(),
                p1.getCoordinate()
        });
        LineString l2 = new GeometryFactory().createLineString(new Coordinate[]{
                p0.getCoordinate(),
                p2.getCoordinate()
        });
        LineString l3 = new GeometryFactory().createLineString(new Coordinate[]{
                p2.getCoordinate(),
                p5.getCoordinate()
        });
        LineString l4 = new GeometryFactory().createLineString(new Coordinate[]{
                p2.getCoordinate(),
                p3.getCoordinate()
        });
        LineString l5 = new GeometryFactory().createLineString(new Coordinate[]{
                p4.getCoordinate(),
                p5.getCoordinate()
        });
        LineString l6 = new GeometryFactory().createLineString(new Coordinate[]{
                p3.getCoordinate(),
                p4.getCoordinate()
        });

        ArrayList<Point> pointList = new ArrayList<>();
        pointList.add(p0);
        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);
        pointList.add(p4);
        pointList.add(p5);
        ArrayList<LineString> lineList = new ArrayList<>();
        lineList.add(l0);
        lineList.add(l1);
        lineList.add(l2);
        lineList.add(l3);
        lineList.add(l4);
        lineList.add(l5);
        lineList.add(l6);

        List<List<Boolean>> bs = AdjacentMatrixUtil
                .createAdjacentMatrixBoolean(pointList, lineList);
        List<List<Integer>> adjacencyList = AdjacentMatrixUtil.getAdjacencyList(bs);

//        SimpleCycles scc = new SimpleCycles(adjacencyList);
//
//        for (int i = 0; i < adjacencyList.size(); i++) {
//            System.out.print("i: " + i + "\n");
//            SimpleResult result = scc.getAdjacencyList(i);
//            if (result != null) {
//                List<Vector> adjList = scc.getAdjacencyList(i).getAdjList();
//                for (int j = i; j < adjList.size(); j++) {
//                    System.out.print("j: " + j  );
//                    for (int k = 0; k < adjList.get(j).size(); k++) {
//                        System.out.print(" _" + adjList.get(j).get(k).toString());
//
//                    }
//                    System.out.println();
//                }
//            }
//            System.out.println();
//        }


        SearchCycles searchCycles = new SearchCycles(bs, pointList);
        List nodeCycles = searchCycles.getNodeCycles();

        for (int i = 0; i < nodeCycles.size(); i++) {
            List cyc = (List) nodeCycles.get(i);

            if (cyc.size() >= 3) {
                cyc.add(cyc.get(0));
                Polygon polygonByPointList = CommonUtils.createPolygonByPointList(cyc);
                System.out.println();
            }
        }

    }


    @Test
    public void getAdjacencyListTest() {
        List<List<Boolean>> bs = new ArrayList<>();
        Boolean[] b1 = new Boolean[]{false, true, false, false, false, false, false, false, false,
                false};
        Boolean[] b2 = new Boolean[]{false, false, true, false, false, false, false, false, false,
                false};
        Boolean[] b3 = new Boolean[]{true, false, false, false, false, false, true, false, false,
                false};
        Boolean[] b4 = new Boolean[]{false, false, false, false, true, false, false, false, false,
                false};
        Boolean[] b5 = new Boolean[]{false, false, false, false, false, true, true, false, false,
                false};
        Boolean[] b6 = new Boolean[]{false, false, false, true, false, false, false, false, false,
                false};
        Boolean[] b7 = new Boolean[]{false, true, false, false, false, false, false, true, false,
                false};
        Boolean[] b8 = new Boolean[]{false, false, false, false, false, false, false, false, true,
                false};
        Boolean[] b9 = new Boolean[]{false, false, false, false, false, false, true, false, false,
                false};
        Boolean[] b10 = new Boolean[]{false, false, false, false, false, false, false, false, false,
                false};
        bs.add(Arrays.stream(b1).collect(Collectors.toList()));
        bs.add(Arrays.stream(b2).collect(Collectors.toList()));
        bs.add(Arrays.stream(b3).collect(Collectors.toList()));
        bs.add(Arrays.stream(b4).collect(Collectors.toList()));
        bs.add(Arrays.stream(b5).collect(Collectors.toList()));
        bs.add(Arrays.stream(b6).collect(Collectors.toList()));
        bs.add(Arrays.stream(b7).collect(Collectors.toList()));
        bs.add(Arrays.stream(b8).collect(Collectors.toList()));
        bs.add(Arrays.stream(b9).collect(Collectors.toList()));
        bs.add(Arrays.stream(b10).collect(Collectors.toList()));
        List<List<Integer>> adjacencyList = AdjacentMatrixUtil.getAdjacencyList(bs);
        System.out.println();
    }

    @Test
    public void startDFSfindTest() throws Exception {

        Point p0 = (Point) new WKTReader().read("POINT(0 0)");
        Point p1 = (Point) new WKTReader().read("POINT(1 1)");
        Point p2 = (Point) new WKTReader().read("POINT(3 2)");
        Point p3 = (Point) new WKTReader().read("POINT(2 4)");
        Point p4 = (Point) new WKTReader().read("POINT(3 5)");
        Point p5 = (Point) new WKTReader().read("POINT(0 3)");
        Point p6 = (Point) new WKTReader().read("POINT(2 0)");
        Point p7 = (Point) new WKTReader().read("POINT(3 1)");
        Point p8 = (Point) new WKTReader().read("POINT(3 0)");
        Point p9 = (Point) new WKTReader().read("POINT(4 1)");
        Point p10 = (Point) new WKTReader().read("POINT(4 6)");
        Point p11 = (Point) new WKTReader().read("POINT(4 4)");

        LineString l0 = new GeometryFactory().createLineString(new Coordinate[]{
                p0.getCoordinate(),
                p6.getCoordinate()
        });
        LineString l1 = new GeometryFactory().createLineString(new Coordinate[]{
                p6.getCoordinate(),
                p8.getCoordinate()
        });
        LineString l2 = new GeometryFactory().createLineString(new Coordinate[]{
                p3.getCoordinate(),
                p4.getCoordinate()
        });
        LineString l3 = new GeometryFactory().createLineString(new Coordinate[]{
                p1.getCoordinate(),
                p6.getCoordinate()
        });
        LineString l4 = new GeometryFactory().createLineString(new Coordinate[]{
                p6.getCoordinate(),
                p2.getCoordinate()
        });
        LineString l5 = new GeometryFactory().createLineString(new Coordinate[]{
                p8.getCoordinate(),
                p7.getCoordinate()
        });
        LineString l6 = new GeometryFactory().createLineString(new Coordinate[]{
                p9.getCoordinate(),
                p8.getCoordinate()
        });
        LineString l7 = new GeometryFactory().createLineString(new Coordinate[]{
                p5.getCoordinate(),
                p0.getCoordinate()
        });
        LineString l8 = new GeometryFactory().createLineString(new Coordinate[]{
                p4.getCoordinate(),
                p11.getCoordinate()
        });
        LineString l9 = new GeometryFactory().createLineString(new Coordinate[]{
                p11.getCoordinate(),
                p10.getCoordinate()
        });
        LineString l10 = new GeometryFactory().createLineString(new Coordinate[]{
                p9.getCoordinate(),
                p11.getCoordinate()
        });

        ArrayList<Point> pointList = new ArrayList<>();
        pointList.add(p0);
        pointList.add(p1);
        pointList.add(p2);
        pointList.add(p3);
        pointList.add(p4);
        pointList.add(p5);
        pointList.add(p6);
        pointList.add(p7);
        pointList.add(p8);
        pointList.add(p9);
        pointList.add(p10);
        pointList.add(p11);
        ArrayList<LineString> lineList = new ArrayList<>();
        lineList.add(l0);
        lineList.add(l1);
        lineList.add(l2);
        lineList.add(l3);
        lineList.add(l4);
        lineList.add(l5);
        lineList.add(l6);
        lineList.add(l7);
        lineList.add(l8);
        lineList.add(l9);
        lineList.add(l10);

        List<List<Integer>> adjacentMatrix = AdjacentMatrixUtil
                .createAdjacentMatrix(pointList, lineList);
        for (int i = 0; i < adjacentMatrix.size(); i++) {
            System.out.println(adjacentMatrix.get(i));
        }

//        Miact maict = new Miact();
//        ArrayList<ArrayList<Double>> distances = Miact.getDistances(pointList, lineList);
//        for (int i = 0; i < distances.size(); i++) {
//            System.out.println(distances.get(i));
//        }

        Graph gp = new Graph(adjacentMatrix, pointList);
        List<List> lists = gp.startDFSfind();
        System.out.println(lists);

        ArrayList<Integer> bst = AdjacentMatrixUtil.getBST(adjacentMatrix);
        System.out.println();

    }


    public static class NodePath {

        @Test
        public void testNodePath() throws Exception {
            int[][] linkNode = {
                    {2, 3},
                    {4, 3},
                    {0, 4},
                    {0, 1, 4},
                    {1, 2, 3}
            };

            Node[] node = new Node[linkNode.length];

            for (int i = 0; i < linkNode.length; i++) {
                node[i] = new Node();
                node[i].setName("node" + i);
            }

            for (int i = 0; i < linkNode.length; i++) {
                ArrayList<Node> List = new ArrayList<>();

                for (int j = 0; j < linkNode[i].length; j++) {
                    List.add(node[linkNode[i][j]]);
                }
                node[i].setLinkNode(List);
                List = null;
            }

            NodeSort.findPath(node[0], null, node[0], node[1]);

            ArrayList<Object[]> res = NodeSort.res;


        }

        @Test
        public void test2() {
            List<List<Integer>> nodeLine = new ArrayList<>();
            List<Integer> l1 = Arrays.asList(2, 3);
            List<Integer> l2 = Arrays.asList(4, 3);
            List<Integer> l3 = Arrays.asList(0, 4);
            List<Integer> l4 = Arrays.asList(0, 1, 4);
            List<Integer> l5 = Arrays.asList(1, 2, 3);
            nodeLine.add(l1);
            nodeLine.add(l2);
            nodeLine.add(l3);
            nodeLine.add(l4);
            nodeLine.add(l5);
            List<Node> nodeList = new ArrayList<>();
            for (int i = 0; i < nodeLine.size(); i++) {
                Node node1 = new Node();
                node1.setName("Node" + i);
                nodeList.add(node1);
            }
            for (int i = 0; i < nodeLine.size(); i++) {
                ArrayList<Node> list = new ArrayList<>();
                for (int j = 0; j < nodeLine.get(i).size(); j++) {
                    list.add(nodeList.get(nodeLine.get(i).get(j)));
                }
                nodeList.get(i).setLinkNode(list);
                list = null;
            }
            NodeSort.findPath(nodeList.get(0), null, nodeList.get(0), nodeList.get(1));

        }
    }

}
