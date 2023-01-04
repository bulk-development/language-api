package ru.bulk.language.content;

import lombok.val;
import ru.bulk.language.util.Pair;

import java.util.List;
import java.util.function.Function;

public class NamedContentFormat<T> extends ContentFormat<T, Pair<String, Object>> {

    public NamedContentFormat(Function<String, T> textMapper, String raw) {
        super(textMapper, raw);
    }

    @Override
    @SafeVarargs
    public final List<T> format(Pair<String, Object>... parameters) {
        val map = Pair.toMap(parameters);
        return replaceInsideQuotes(raw, input -> map.containsKey(input) ? resolve(map.get(input)) : null);
    }

}
