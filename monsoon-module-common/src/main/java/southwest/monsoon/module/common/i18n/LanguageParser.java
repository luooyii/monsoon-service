package southwest.monsoon.module.common.i18n;

public interface LanguageParser {
    String keyPrefix();

    String parse(String key);
}
