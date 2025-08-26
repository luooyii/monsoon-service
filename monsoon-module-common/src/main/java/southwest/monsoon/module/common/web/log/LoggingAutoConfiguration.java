package southwest.monsoon.module.common.web.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingAutoConfiguration {
    @Autowired
    private LoggingSkipping loggingSkipping;

    /**
     * Instantiate CommonsLoggingFilter to print request response logs
     */
    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistration() {
        LoggingFilter loggingFilter = new LoggingFilter();
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludeHeaders(false);
        loggingFilter.setIncludeRequestPayload(true);
        loggingFilter.setIncludeResponsePayload(true);
        loggingFilter.setIncludeTakeTime(true);
        loggingFilter.setSkippingJudgment(loggingSkipping::shouldSkip);

        FilterRegistrationBean<LoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(loggingFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
        return registration;
    }
}
