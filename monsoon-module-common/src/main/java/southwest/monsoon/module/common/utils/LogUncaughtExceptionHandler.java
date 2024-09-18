package southwest.monsoon.module.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final LogUncaughtExceptionHandler INSTANCE = new LogUncaughtExceptionHandler();

    private LogUncaughtExceptionHandler() {
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Uncaught Exceptions in multiple threads", e);
    }
}