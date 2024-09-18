package southwest.monsoon.module.common.mybatis;

import java.sql.Timestamp;

/**
 * @version 4/25/2022
 */
public class DbConst {

    public static final Long MAX_TIME_MS = 253402185600000L;

    /**
     * 2020-01-01 00:00:00
     */
    public static final Long MIN_TIME_MS = 0L;

    /**
     * 9999-12-31 00:00:00.000 unit: ms
     */
    public static final Timestamp MAX_TIME_TS = new Timestamp(MAX_TIME_MS);

    private DbConst() {
    }
}
