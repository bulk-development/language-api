package ru.bulk.language.content;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import ru.bulk.language.annotation.Content;
import ru.bulk.language.annotation.RawContent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class ContentResolver {

    public void resolvePresetContents(Object preset, Function<String, ?> textMapper, Map<String, String> rawContent) {
        Arrays.stream(preset.getClass().getDeclaredFields())
                .filter(ContentResolver::isContentField)
                .forEach(field -> {
                    if (field.isAnnotationPresent(RawContent.class)) {
                        try {
                            field.setAccessible(true);
                            field.set(preset, rawContent);
                        } catch (Throwable throwable) {
                            throw new RuntimeException(throwable);
                        }
                    } else {
                        val contentName = resolveContentName(field);
                        if (rawContent.get(contentName) != null)
                            writeContentField(field, preset, textMapper, rawContent.get(contentName));
                    }
                });
    }

    private String resolveContentName(Field field) {
        val annotation = field.getAnnotation(Content.class);
        return annotation == null || annotation.value().isEmpty() ? resolveNameAsFieldName(field.getName()) : annotation.value();
    }

    private String resolveNameAsFieldName(String fieldName) {
        val builder = new StringBuilder();
        char code;
        for (int i = 0; i < fieldName.length(); i++) {
            code = fieldName.charAt(i);
            if (Character.isUpperCase(code)) builder.append('.');
            builder.append(Character.toLowerCase(code));
        }
        return builder.toString();
    }

    private boolean isContentField(Field field) {
        return (Map.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(RawContent.class))
                || (ContentFormat.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(Content.class));
    }

    @SneakyThrows
    private void writeContentField(Field field, Object object, Function<String, ?> textMapper, String raw) {
        val content = resolveContent((Class<? extends ContentFormat<?, ?>>) field.getType(), textMapper, raw);

        field.setAccessible(true);
        field.set(object, content);
    }

    @SneakyThrows
    private ContentFormat<?, ?> resolveContent(Class<? extends ContentFormat<?, ?>> clazz, Function<String, ?> textMapper, String raw) {
        val constructor = clazz.getDeclaredConstructor(Function.class, String.class);
        constructor.setAccessible(true);
        return constructor.newInstance(textMapper, raw);
    }

}
