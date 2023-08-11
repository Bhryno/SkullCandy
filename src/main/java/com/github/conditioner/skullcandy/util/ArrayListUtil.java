package com.github.conditioner.skullcandy.util;

import java.util.ArrayList;

public class ArrayListUtil {
    public static <T> String separateList(ArrayList<T> list, String args) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(args);
            }
        }
        return sb.toString();
    }
}
