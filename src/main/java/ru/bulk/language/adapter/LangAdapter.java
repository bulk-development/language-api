package ru.bulk.language.adapter;

import ru.bulk.language.util.EnumPreset;

import java.util.function.Function;

public interface LangAdapter<T> {

    <P> P getPreset(Class<P> clazz, String lang);

    <E extends Enum<E> & EnumPreset> void resolveEnumPreset(Class<E> enumClass);

    void setTextMapper(Function<String, T> mapper);

    T resolveAsText(String text);

    void recompute();

    void cleanup();

}
