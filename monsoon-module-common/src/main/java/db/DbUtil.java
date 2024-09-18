package db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DbUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    private DbUtil() {
    }

    /**
     * ${&#064;db.DbUtil@now() }
     */
    public static String now() {
        return "'" + LocalDateTime.now().format(FORMATTER) + "'";
    }
}
