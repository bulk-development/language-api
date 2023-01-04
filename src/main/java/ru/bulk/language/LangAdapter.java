package ru.bulk.language;

import java.util.function.Function;

public interface LangAdapter<T> {

    <P> P getPreset(Class<P> clazz, String lang);

    void setTextMapper(Function<String, T> mapper);

    T resolveAsText(String text);

    void recompute();

    void cleanup();

}
