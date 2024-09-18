package southwest.monsoon.module.common.web.trim;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ParameterTrim {
    class ParameterTrimWrapper extends ContentCachingRequestWrapper {
        public ParameterTrimWrapper(HttpServletRequest request) {
            super(request);
        }

        public ParameterTrimWrapper(HttpServletRequest request, int contentCacheLimit) {
            super(request, contentCacheLimit);
        }

        @Override
        public String getParameter(String name) {
            String parameter = super.getParameter(name);
            if (parameter == null) {
                return parameter;
            }
            return parameter.trim();
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> map = super.getParameterMap();
            if (map == null) {
                return map;
            }
            map = new HashMap<>(super.getParameterMap());
            for (String key : map.keySet()) {
                String[] values = StringUtils.trimArrayElements(map.get(key));
                map.put(key, values);
            }
            return map;
        }

        @Override
        public String[] getParameterValues(String name) {
            return StringUtils.trimArrayElements(super.getParameterValues(name));
        }
    }

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> wrapperFilterRegistration() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                ContentCachingRequestWrapper requestWrapper = new ParameterTrimWrapper(request);
                filterChain.doFilter(requestWrapper, response);
            }
        });
        registration.addUrlPatterns("/*");
        registration.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 2);
        return registration;
    }
}
