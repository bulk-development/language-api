package ru.bulk.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.val;
import ru.bulk.language.model.YamlLangPreset;

import java.net.URL;
import java.util.Map;
import java.util.function.Function;

public class RepositoryLangAdapter<T /*text*/> extends AbstractLangAdapter<T> {

    public static final String GITHUB_URL_FORMAT = "https://raw.githubusercontent.com/%s/%s/%s/%s/%s.yml";
    public static final ObjectMapper YML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    private final String account, repository, branch;

    public RepositoryLangAdapter(Function<String, T> textMapper, String account, String repository, String branch) {
        super(textMapper);
        this.account = account;
        this.repository = repository;
        this.branch = branch;
    }

    @Override
    @SneakyThrows
    public Map<String, String> getPresetContent(String id, String lang) {
        val url = createGitHubLangUrl(lang, id);
        val preset = YML_OBJECT_MAPPER.readValue(url, YamlLangPreset.class);
        return preset.getRecursiveLang();
    }

    @SneakyThrows
    protected URL createGitHubLangUrl(String id, String lang) {
        return new URL(String.format(GITHUB_URL_FORMAT, account, repository, branch, lang, id));
    }

}
