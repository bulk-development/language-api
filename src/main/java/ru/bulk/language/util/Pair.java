package ru.bulk.language.util;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Pair<K, V> {

    private final K key;
    private final V value;

    public static <K, V> Map<K, V> toMap(List<Pair<K, V>> pairs) {
        return pairs.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public static <K, V> Map<K, V> toMap(Pair<K, V>... pairs) {
        return toMap(Arrays.asList(pairs));
    }

    public static <K, V> Pair<K, V> of(K first, V second) {
        return new Pair<>(first, second);
    }

}
