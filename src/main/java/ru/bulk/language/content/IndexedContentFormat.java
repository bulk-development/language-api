package ru.bulk.language.content;

import lombok.val;

import java.util.List;
import java.util.function.Function;

public class IndexedContentFormat<T> extends ContentFormat<T, Object> {

    public IndexedContentFormat(Function<String, T> textMapper, String raw) {
        super(textMapper, raw);
    }

    @Override
    public List<T> format(Object... parameters) {
        return replaceInsideQuotes(raw, input -> {
            try {
                val index = Integer.parseInt(input);
                return index < 0 || index >= parameters.length ? null : resolve(parameters[index]);
            } catch (NumberFormatException e) {
                return null;
            }
        });
//        val list = new ArrayList<T>();
//
//        var text = raw;
//        int openIndex, closeIndex;
//
//        while ((openIndex = text.indexOf('{')) != -1 && (closeIndex = text.indexOf('}')) != -1) {
//            val index = Integer.parseInt(text.substring(openIndex + 1, closeIndex));
//            if (openIndex != 0) list.add(resolve(text.substring(0, openIndex)));
//            list.add(resolve(parameters[index]));
//            text = text.substring(closeIndex + 1);
//        }
//
//        if (!text.isEmpty()) list.add(resolve(text));
//
//        return list;
    }

}
