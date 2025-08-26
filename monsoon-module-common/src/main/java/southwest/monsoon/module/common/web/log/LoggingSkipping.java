package southwest.monsoon.module.common.web.log;

import jakarta.servlet.http.HttpServletRequest;

public interface LoggingSkipping {
    boolean shouldSkip(HttpServletRequest request);
}
