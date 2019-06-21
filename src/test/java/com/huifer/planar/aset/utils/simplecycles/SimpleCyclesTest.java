package com.huifer.planar.aset.utils.simplecycles;

import com.huifer.planar.aset.utils.AdjacentMatrixUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import org.junit.Test;

public class SimpleCyclesTest {

    @Test
    public void te() {
        List<List<Boolean>> bs = new ArrayList<>();
        Boolean[] b1 = new Boolean[]{false, true, false, false, false, false, false, false, false,
                false};
        Boolean[] b2 = new Boolean[]{false, false, true, false, false, false, false, false, false,
                false};
        Boolean[] b3 = new Boolean[]{true, false, false, false, false, false, true, false, false,
                false};
        Boolean[] b4 = new Boolean[]{false, false, false, false, true, false, false, false, false,
                false};
        Boolean[] b5 = new Boolean[]{false, false, false, false, true, true, true, false, false,
                false};
        Boolean[] b6 = new Boolean[]{false, false, false, true, false, false, false, false, false,
                false};
        Boolean[] b7 = new Boolean[]{false, true, false, true, false, false, false, true, false,
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

        SimpleCycles scc = new SimpleCycles(adjacencyList);

        for (int i = 0; i < adjacencyList.size(); i++) {
            System.out.print("i: " + i + "\n");
            SimpleResult result = scc.getAdjacencyList(i);
            if (result != null) {
                List<Vector> adjList = scc.getAdjacencyList(i).getAdjList();
                for (int j = i; j < adjList.size(); j++) {
                    System.out.print("j: " + j  );
                    for (int k = 0; k < adjList.get(j).size(); k++) {
                        System.out.print(" _" + adjList.get(j).get(k).toString());

                    }
                    System.out.println();
                }
            }
            System.out.println();
        }



    }

}
