package southwest.monsoon.module.common.web.result.msg;

import lombok.Getter;
import lombok.ToString;
import southwest.monsoon.module.common.utils.FormatUtils;

@ToString
public class SimpleMsg implements ReMsg {
    @Getter
    private final String pattern;
    @Getter
    private final Object[] params;

    private String msg;

    private SimpleMsg(String pattern, Object[] params) {
        this.pattern = pattern;
        this.params = params;
        if (params == null) {
            this.msg = pattern;
        }
    }

    public static SimpleMsg format(String pattern, Object... args) {
        return new SimpleMsg(pattern, args);
    }

    public static SimpleMsg text(String text) {
        return new SimpleMsg(text, null);
    }

    @Override
    public String getMsg() {
        if (msg != null) {
            return msg;
        }
        if (pattern == null) {
            return null;
        }
        if (params == null) {
            msg = pattern;
        } else {
            msg = FormatUtils.formatStr(pattern, params);
        }
        return msg;
    }
}
