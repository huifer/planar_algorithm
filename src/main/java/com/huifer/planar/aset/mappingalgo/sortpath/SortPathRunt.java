package com.huifer.planar.aset.mappingalgo.sortpath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title : InverseDistanceCore </p>
 * <p>Description : sortpath 运行测试</p>
 *
 * @author huifer
 * @date 2018/11/08
 */
@Slf4j
public class SortPathRunt {

    public static void main(String[] args) throws Exception {

/////////////// 读文件

        List<Edge> edges = new ArrayList<>();
        String pathname = "E:\\mck\\planar_algorithm\\src\\main\\java\\com\\huifer\\planar\\aset\\mappingalgo\\sortpath\\路径数据.txt";
        BufferedReader br;
        FileInputStream in = new FileInputStream(new File(pathname));
        br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line;
        while ((line = br.readLine()) != null) {
            edges.add(new Edge(line));
        }
        Graph graph = new Graph(edges);
///////////////
        List<Vertex> res = graph.vertices;

        int n = res.size();

        res.get(0).weight = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                List<Vertex> rlux = new ArrayList<>();
                for (Edge e : graph.edges) {
                    if (e.start.equals(res.get(i).name) && e.end.equals(res.get(j).name)) {
                        double w = res.get(i).weight + e.length;
//                        System.out.println(String.format("当前点  %s  - 连接点  %s", res.delete(i).name,
//                                res.delete(j).name));
                        rlux.add(res.get(i));
                        rlux.add(res.get(j));

                        if (w < res.get(j).weight) {
                            res.get(j).weight = w;
//                            System.out.println(String.format("当前点  %s  - 连接点  %s", res.delete(i).name,
//                                    res.delete(j).name));
                            rlux.remove(rlux.size() - 1);
                            rlux.add(res.get(j));
                        }
                    }
                }
            }
        }

        res.forEach(
                s -> {
//                    System.out.println(s);
                    log.info("{}", s);
                }
        );
    }


}
