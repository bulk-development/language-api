package ru.bulk.language.content;

import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
public abstract class ContentFormat<T /*text*/, P /*parameter type*/> {

    protected final Function<String, T> textMapper;
    protected final String raw;

    public abstract List<T> format(P... parameters);

    protected List<T> replaceInsideQuotes(String text, Function<String, T> replacer) {
        val result = new ArrayList<T>();
        int openIndex, closeIndex;

        while ((openIndex = text.indexOf('{')) != -1 && (closeIndex = text.indexOf('}')) != -1) {
            if (openIndex > closeIndex) result.add(resolve(text.substring(0, openIndex + 1)));
            else {
                val replacement = replacer.apply(text.substring(openIndex + 1, closeIndex));
                if (replacement == null) result.add(resolve(text.substring(0, closeIndex + 1)));
                else {
                    if (openIndex != 0) result.add(resolve(text.substring(0, openIndex)));
                    result.add(replacement);
                }
            }
            text = text.substring(Math.max(closeIndex, openIndex) + 1);
        }

        if (!text.isEmpty()) result.add(resolve(text));

        return result;
    }

    protected T resolve(Object text) {
//        if (text == null) text = "null";
//        return text instanceofT tп instanceof String ? textMapper.apply((String) text) : (T) text;
        return text instanceof String ? textMapper.apply((String) text) : (T) text;
    }

    @Override
    public String toString() {
        return raw;
    }

}
