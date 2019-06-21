package com.huifer.planar.aset.mappingalgo.sortpath;

/**
 * <p>Title : Edge </p>
 * <p>Description : 起点 ， 终点 ， 长度</p>
 *
 * @author huifer
 * @date 2018/11/08
 */
public class Edge {

    /**
     * 起点
     */
    public String start;
    /***
     * 终点
     */
    public String end;
    /***
     * 距离
     */
    public double length;


    public Edge(String line) {
        String[] split = line.split(",");
        start = split[0];
        end = split[1];
        length = Double.parseDouble(split[2]);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", length=" + length +
                '}';
    }
}
