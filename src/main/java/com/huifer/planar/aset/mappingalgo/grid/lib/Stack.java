package com.huifer.planar.aset.mappingalgo.grid.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title : Stack </p>
 * <p>Description : 堆</p>
 *
 * @author huifer
 * @date 2018/11/14
 */
public class Stack {

    public List<PointInfo> data;
    public int top;

    public Stack() {
        top = -1;
        data = new ArrayList<>();
    }

    /**
     * 向点集追加数据返回总量
     * @param pointInfo
     * @return
     */
    public int pushStack(PointInfo pointInfo) {
        data.add(pointInfo);
        top++;
        return top;
    }

    /***
     * 删除一个点 返回点信息
     * @return
     */
    public PointInfo popStack(){
        PointInfo p = data.get(top);
        data.remove(p);
        top--;
        return p;
    }

    @Override
    public String toString() {
        return "Stack{" +
                "data=" + data +
                ", top=" + top +
                '}';
    }
}
