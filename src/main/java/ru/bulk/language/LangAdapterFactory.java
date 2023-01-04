package ru.bulk.language;

import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@UtilityClass
public class LangAdapterFactory {

    public final UnaryOperator<String> STRING_NO_MAPPER = text -> text;

    public <T> LangAdapter<T> newRepositoryAdapter(Function<String, T> textMapper, String account, String repository, String branch) {
        return new RepositoryLangAdapter<>(textMapper, account, repository, branch);
    }

    public <T> LangAdapter<T> newRepositoryAdapter(Function<String, T> textMapper, String repository, String account) {
        return newRepositoryAdapter(textMapper, account, repository, "master");
    }

    public LangAdapter<String> newStringRepositoryAdapter(String account, String repository, String branch) {
        return newRepositoryAdapter(STRING_NO_MAPPER, account, repository, branch);
    }

    public LangAdapter<String> newStringRepositoryAdapter(String account, String repository) {
        return newRepositoryAdapter(STRING_NO_MAPPER, account, repository);
    }

}
