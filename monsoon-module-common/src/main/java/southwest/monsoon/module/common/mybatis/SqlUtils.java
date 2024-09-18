package southwest.monsoon.module.common.mybatis;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * @version 5/23/2022
 */
public class SqlUtils {
    private SqlUtils() {
    }

    public static String escapeLike(String value) {
        return escapeLike(value, true, true);
    }

    public static String escapeLike(String value, boolean start, boolean end) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (start) {
            sb.append('%');
        }
        char[] charArr = value.toCharArray();
        for (char c : charArr) {
            if (c == '%' || c == '_' || c == '[') {
                sb.append('[').append(c).append(']');
            } else {
                sb.append(c);
            }
        }
        if (end) {
            sb.append('%');
        }
        return sb.toString();
    }
}
