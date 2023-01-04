package ru.bulk.language.adapter;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import ru.bulk.language.annotation.Preset;
import ru.bulk.language.content.ContentResolver;
import ru.bulk.language.util.EnumPreset;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public abstract class AbstractLangAdapter<T> implements LangAdapter<T> {


    protected final Function<String, T> selfTextMapper = this::resolveAsText;
    protected final Map<String /*lang*/, Map<Class<?>, Object>> presets = new HashMap<>();

    @Setter
    protected Function<String, T> textMapper;

    @Override
    public <P> P getPreset(Class<P> clazz, String lang) {
        if (!clazz.isAnnotationPresent(Preset.class))
            throw new IllegalArgumentException("Preset class mast annotated by @Preset");

        return (P) presets.computeIfAbsent(lang.toLowerCase(), __ -> new HashMap<>()).computeIfAbsent(clazz, __ -> {
            val preset = resolvePreset(clazz);
            resolvePresetContents(lang.toLowerCase(), clazz.getAnnotation(Preset.class).value(), preset);
            return preset;
        });
    }

    @Override
    public <E extends Enum<E> & EnumPreset> void resolveEnumPreset(Class<E> enumClass) {
        if (!enumClass.isAnnotationPresent(Preset.class))
            throw new IllegalArgumentException("Preset class mast annotated by @Preset");

        for (E preset : enumClass.getEnumConstants()) {
            presets.computeIfAbsent(preset.getLang(), __ -> new HashMap<>()).computeIfAbsent(enumClass, __ -> {
                resolvePresetContents(preset.getLang(), enumClass.getAnnotation(Preset.class).value(), preset);
                return preset;
            });
        }
    }

    @Override
    public T resolveAsText(String text) {
        return textMapper.apply(text);
    }

    @Override
    public void recompute() {
        presets.forEach((lang, presets) -> presets.forEach((clazz, preset) ->
                resolvePresetContents(lang, clazz.getAnnotation(Preset.class).value(), preset)
        ));
    }

    @Override
    public void cleanup() {
        presets.clear();
    }

    @SneakyThrows
    protected <P> P resolvePreset(Class<P> clazz) {
        val constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    protected void resolvePresetContents(String lang, String id, Object preset) {
        ContentResolver.resolvePresetContents(preset, selfTextMapper, getPresetContent(lang, id));
    }

    protected abstract Map<String, String> getPresetContent(String lang, String id);

}
