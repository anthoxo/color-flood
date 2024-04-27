package com.anthoxo.hackhaton.utils;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    private ListUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> List<List<T>> copy(List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            List<T> line = new ArrayList<>();
            for (int j = 0; j < lists.get(i).size(); j++) {
                line.add(lists.get(i).get(j));
            }
            result.add(line);
        }
        return result;
    }
}
