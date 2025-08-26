package southwest.monsoon.module.common.web.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DefaultSkippingConfig {
    @Value("${server.servlet.context-path:}")
    private String path;

    @Bean
    @ConditionalOnMissingBean(LoggingSkipping.class)
    public LoggingSkipping defaultLoggingSkipping() {
        List<String> list = new ArrayList<>();
        list.add(path + "/swagger");
        list.add(path + "/v3/api-docs");
        return request -> {
            String uri = request.getRequestURI();
            if (uri == null) {
                return false;
            }
            for (String s : list) {
                if (uri.startsWith(s)) {
                    return true;
                }
            }
            return false;
        };
    }
}
