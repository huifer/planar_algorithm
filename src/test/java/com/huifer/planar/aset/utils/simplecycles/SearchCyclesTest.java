package com.huifer.planar.aset.utils.simplecycles;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchCyclesTest {
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

        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            nodes.add("node " + i);
        }

        SearchCycles searchCycles = new SearchCycles(bs, nodes);
        List nodeCycles = searchCycles.getNodeCycles();

        for (int i = 0; i < nodeCycles.size(); i++) {
            List cyc = (List) nodeCycles.get(i);
            for (int j = 0; j < cyc.size(); j++) {
                Object o = cyc.get(j);
                if (j < cyc.size() - 1) {
                    System.out.print(o.toString() + " -->");
                } else {
                    System.out.print(o.toString());
                }
            }
            System.out.println();
        }
    }

}
