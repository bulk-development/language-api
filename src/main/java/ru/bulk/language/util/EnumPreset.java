package ru.bulk.language.util;

public interface EnumPreset {

    default String getLang() {
        return ((Enum<?>) this).name().toLowerCase();
    }

}
