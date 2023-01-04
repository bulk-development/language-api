package ru.bulk.language.content;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class SimpleContentFormat<T> extends ContentFormat<T, String> {

    public SimpleContentFormat(Function<String, T> textMapper, String raw) {
        super(textMapper, raw);
    }

    @Override
    public List<T> format(String... parameters) {
        return Collections.singletonList(resolve(String.format(raw, parameters)));
    }

}
