package com.huifer.planar.aset.mappingalgo.sortpath;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : Digraph </p>
 * <p>Description : 图</p>
 *
 * @author huifer
 * @date 2018/11/08
 */
public class Graph {

    public List<Edge> edges;
    public List<Vertex> vertices;


    public Graph(List<Edge> edges) {
        this.edges = edges;
        parse(edges);
    }


    @Override
    public String toString() {
        return "Digraph{" +
                "edges=" + edges +
                ", vertices=" + vertices +
                '}';
    }

    private void parse(List<Edge> edgeList) {
        List<Vertex> aaa = new ArrayList<>();
        for (Edge d : edgeList) {
            Vertex v = new Vertex(d.start);
            if (!aaa.contains(v)) {
                aaa.add(v);
            }
            v = new Vertex(d.end);
            if (!aaa.contains(v)) {
                aaa.add(v);
            }
        }
        vertices = aaa;
    }


}
