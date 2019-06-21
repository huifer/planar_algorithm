package com.huifer.planar.aset.utils.threepoint;

import java.util.Stack;

/**
 * <p>Title : SortThreePoint </p>
 * <p>Description : 三点排序问题</p>
 *
 * @author huifer
 * @date 2019-03-13
 */
public class SortThreePoint {

    /**
     * 记录总量三点一个圆
     */
    private static int count = 0;
    /**
     * 三点一个元的结果集合
     */
    private static Stack<Integer> allStack = new Stack<Integer>();
    /**
     * 是否使用
     */
    private static boolean[] isUsing = new boolean[10000];

    public static void main(String[] args) {
        sortThreePoint(1, 4, 0, 3);
    }

    /**
     * @param minv 当前最小值
     * @param maxv 当前最大值
     * @param curnum 确认过的内容
     * @param maxnum 选取几个点作为一组
     */
    public static void sortThreePoint(int minv, int maxv, int curnum, int maxnum) {
        if (curnum == maxnum) {
            count++;
            System.out.println(allStack);
            return;
        }
        for (int i = minv; i <= maxv; i++) {
            if (!isUsing[i]) {
                allStack.push(i);
                isUsing[i] = true;
                sortThreePoint(minv, maxv, curnum + 1, maxnum);
                allStack.pop();
                isUsing[i] = false;
            }
        }
    }
}
