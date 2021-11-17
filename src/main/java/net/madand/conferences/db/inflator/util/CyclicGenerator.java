package net.madand.conferences.db.inflator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CyclicGenerator<T> implements Iterable<T> {
    private List<T> list = new ArrayList<>();
    private int cursor = 0;

    public CyclicGenerator(T[] values) {
        // Input array because Java 8 does not yet have List.of().
        Collections.addAll(list, values);
    }

    public int size() {
        return list.size();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                T result = list.get(cursor);
                cursor = ++cursor % list.size();
                return result;
            }
        };
    }

    public static void main(String[] args) {
        BiProductGenerator<String, Integer, String> generator = new BiProductGenerator<>(
                new String[] {"A", "B", "C"},
                new Integer[] {1, 2, 3, 4},
                (v1, v2) -> v1 + " " + v2);
        for (String s : generator) {
            System.out.println(s);
        }
    }
}
