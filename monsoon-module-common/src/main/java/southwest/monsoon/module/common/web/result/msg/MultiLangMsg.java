package southwest.monsoon.module.common.web.result.msg;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class MultiLangMsg implements ReMsg {
    private static final String DEFAULT_NAME = "monsoon/i18n/WebMsg";
    private static final String MSG_NOT_FOUND_KEY = "monsoon.web.msg.MsgNotFound";

    @Getter
    private final String key;
    @Getter
    private final Object[] params;
    @Getter
    private final String resourceName;

    private String msg;

    private MultiLangMsg(String key, String resourceName, Object[] params) {
        this.key = key;
        this.params = params;
        this.resourceName = resourceName;
    }

    public static MultiLangMsg key(String key, Object... args) {
        return new MultiLangMsg(key, DEFAULT_NAME, args);
    }

    public static MultiLangMsg key(String key) {
        return new MultiLangMsg(key, DEFAULT_NAME, null);
    }

    public static MultiLangMsg keyAt(String key, String resourceName, Object... args) {
        return new MultiLangMsg(key, resourceName, args);
    }

    public static MultiLangMsg keyAt(String key, String resourceName) {
        return new MultiLangMsg(key, resourceName, null);
    }

    @Override
    public String getMsg() {
        if (msg != null) {
            return msg;
        }
        if (key == null) {
            return null;
        }
        String pattern = getPattern(key);
        if (pattern == null) {
            msg = getNotFoundMsg(key);
            return msg;
        }
        if (params == null) {
            msg = pattern;
        } else {
            msg = MessageFormat.format(pattern, params);
        }
        return msg;
    }

    private String getPattern(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        String pattern = getVal(key, locale);
        if (pattern == null) {
            pattern = getVal(key, Locale.ENGLISH);
        }
        return pattern;
    }

    private String getNotFoundMsg(String key) {
        String pattern = getPattern(MSG_NOT_FOUND_KEY);
        if (pattern != null) {
            return key(MSG_NOT_FOUND_KEY, key).getMsg();
        } else {
            return SimpleMsg.format("Message({}) not found", key).getMsg();
        }
    }

    private String getVal(String key, Locale locale) {
        String val = null;
        try {
            ResourceBundle rb = ResourceBundle.getBundle(resourceName, locale);
            val = rb.getString(key);
        } catch (Exception e) {
            log.warn("Fail to get language resource: '{}'", key, e);
        }
        return val;
    }
}
