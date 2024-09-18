package southwest.monsoon.module.common.i18n;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import southwest.monsoon.module.common.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MultiLanguageService {
    @Autowired
    private ApplicationContext applicationContext;

    private boolean mapEmpty = true;
    private final Map<String, LanguageParser> map = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        LanguageKeySerializer.languageService = this;
        Map<String, LanguageParser> beans = applicationContext.getBeansOfType(LanguageParser.class);
        if (beans.isEmpty()) {
            log.warn("LanguageParser not found");
            return;
        }
        for (LanguageParser parser : beans.values()) {
            String prefix = parser.keyPrefix();
            if (StringUtils.isBlank(prefix)) {
                log.warn("Illegal LanguageParser prefix {}", parser);
                continue;
            }
            log.info("Found LanguageParser for '{}'", prefix);
            map.put(prefix, parser);
        }
        mapEmpty = map.isEmpty();
    }

    public String parse(Object keyObj) {
        String keyExpression = String.valueOf(keyObj);
        if (StringUtils.isBlank(keyExpression)) {
            return null;
        }
        if (mapEmpty) {
            return null;
        }
        try {
            List<String> expressions;
            if (keyExpression.startsWith("[")) {
                expressions = new ArrayList<>(5);
                for (Object expression : JacksonUtils.parseJson(keyExpression, List.class)) {
                    expressions.add(String.valueOf(expression));
                }
            } else {
                expressions = Collections.singletonList(keyExpression);
            }
            for (String expression : expressions) {
                String val = parseByExpression(expression);
                if (val != null) {
                    return val;
                }
            }
            log.warn("Fail to parse language key: {}", keyExpression);
        } catch (Exception e) {
            log.error("Throw error when parsing language key: {}", keyExpression, e);
        }
        return null;
    }

    public String parseByExpression(String expression) {
        if (StringUtils.isBlank(expression)) {
            return null;
        }
        int i = expression.indexOf(':');
        if (i < 1 || i >= expression.length() - 1) {
            log.warn("Wrong language expression format. {}", expression);
            return null;
        }
        String prefix = expression.substring(0, i);
        if (!map.containsKey(prefix)) {
            log.warn("LanguageParser for '{}' not found", prefix);
            return null;
        }
        LanguageParser parser = map.get(prefix);
        String key = expression.substring(i + 1);
        return parser.parse(key);
    }
}
