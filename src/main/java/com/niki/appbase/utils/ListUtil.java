package com.niki.appbase.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Niki on 2017/11/10 0010 10:10
 * E-Mail Address：m13296644326@163.com
 */

public class ListUtil {
    /**
     * 去除一个list集合中的重复元素
     *
     * @param list 集合
     */
    public static void removeDuplicateWithOrder(List list) {
        if (list == null) {
            return;
        }
        //定义一个hashSet, 利用其元素不可重复的特性
        Set set = new HashSet();
        //定义一个arrayList, 在后面如果不重复, 那么添加新元素
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    /**
     * 判断一个集合是否为空
     *
     * @param list 集合
     * @return 是否为空
     */
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmptyList(List list) {
        return list != null && list.size() != 0;
    }

    public static <T> ArrayList<T> copyList(List<T> origin) {
        ArrayList<T> newList = new ArrayList<>();
        newList.addAll(origin);
        return newList;
    }

    public static <T> void filterList(List<T> list, ListFilter<T> filter) {
        if (ListUtil.isEmptyList(list) || filter == null) {
            return;
        }
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()){
            T item = iterator.next();
            if (!filter.onFilter(item)){
                iterator.remove();
            }
        }
    }

    public static interface ListFilter<T> {
        boolean onFilter(T item);
    }
}
