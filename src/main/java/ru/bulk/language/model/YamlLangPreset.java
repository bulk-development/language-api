package ru.bulk.language.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

public class YamlLangPreset {

    @Getter
    @JsonProperty("lang")
    private Map<String, Object> lang;

    @JsonCreator
    public YamlLangPreset(@JsonProperty("lang") Map<String, Object> lang) {
        this.lang = lang;
    }

    public Map<String, String> getRecursiveLang() {
        val resolved = new HashMap<String, String>();
        resolveLang(null, lang, resolved);
        return resolved;
    }

    protected void resolveLang(String parent, Map<String, Object> lang, Map<String, String> resolved) {
        lang.forEach((key, value) -> {
            key = parent == null ? key : parent + "." + key;

            if (value instanceof Map) resolveLang(key, (Map<String, Object>) value, resolved);
            else resolved.put(key, String.valueOf(value));
        });
    }

}
