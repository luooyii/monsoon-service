package southwest.monsoon.module.common.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.helpers.MessageFormatter;

/**
 * @version 4/14/2022
 */
@UtilityClass
public class FormatUtils {
    public static String formatStr(String pattern, Object arg) {
        String msg = MessageFormatter.format(pattern, arg).getMessage();
        return msg;
    }

    public static String formatStr(String pattern, Object arg1, Object arg2) {
        String msg = MessageFormatter.format(pattern, arg1, arg2).getMessage();
        return msg;
    }

    public static String formatStr(String pattern, Object... argArr) {
        String msg = MessageFormatter.arrayFormat(pattern, argArr).getMessage();
        return msg;
    }
}
