package net.madand.conferences.db.inflator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

public class BiProductGenerator<T, U, R> implements Iterable<R> {
    private final List<T> list1 = new ArrayList<>();
    private final List<U> list2 = new ArrayList<>();
    private final BiFunction<T, U, R> joiner;

    public BiProductGenerator(T[] values1, U[] values2, BiFunction<T, U, R> joiner) {
        Collections.addAll(list1, values1);
        Collections.addAll(list2, values2);
        this.joiner = joiner;
    }

    @Override
    public Iterator<R> iterator() {
        return list1.stream()
                .flatMap(v1 -> list2.stream()
                        .map(v2 -> joiner.apply(v1, v2)))
                .iterator();
    }
}
