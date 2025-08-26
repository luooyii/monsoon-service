package southwest.monsoon.module.common.web.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.MultiLangMsg;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Log request and response
 */
public class LoggingFilter extends OncePerRequestFilter {
    @Setter
    @Getter
    private boolean includeQueryString = false;
    @Setter
    @Getter
    private boolean includeHeaders = false;
    @Setter
    @Getter
    private boolean includeRequestPayload = false;

    /**
     * Whether to record response information
     */
    @Setter
    @Getter
    private boolean includeResponsePayload = false;
    /**
     * Whether to record time-consuming
     */
    @Setter
    @Getter
    private boolean includeTakeTime = false;

    @Setter
    @Getter
    private Function<HttpServletRequest, Boolean> skippingJudgment = request -> false;

    /**
     * Start time field identification
     */
    private static final String START_TIME_KEY = "__START_TIME";

    private Set<String> staticExtensions = Set.of("html", "css", "js", "png", "jpg", "jpeg", "gif", "bmp", "ico", "svg", "json");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isStaticResource(request) || skippingJudgment.apply(request);
    }

    private boolean isStaticResource(HttpServletRequest request) {
        String url = request.getRequestURI();
        int index = url.lastIndexOf('.');
        if (index < 0) {
            return false;
        }

        String extension = url.substring(index).toLowerCase().replace(".", "");
        return staticExtensions.contains(extension);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!logger.isInfoEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper;
        if (!(request instanceof ContentCachingRequestWrapper)) {
            throw WebException.err(HttpStatus.NOT_IMPLEMENTED, MultiLangMsg.key("monsoon.web.msg.RequestNotWrapped"));
        }
        requestWrapper = (ContentCachingRequestWrapper) request;

        ContentCachingResponseWrapper responseWrapper;
        boolean wrapped = true;
        if (response instanceof ContentCachingResponseWrapper) {
            responseWrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapped = false;
            responseWrapper = new ContentCachingResponseWrapper(response);
        }

        if (isIncludeTakeTime()) {
            requestWrapper.setAttribute(START_TIME_KEY, System.currentTimeMillis());
        }

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            try {
                logRequest(requestWrapper, responseWrapper);
            } finally {
                if (!wrapped) {
                    responseWrapper.copyBodyToResponse();
                }
            }
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        StringBuilder msg = new StringBuilder();
        msg.append("Request from ");
        msg.append(request.getRemoteAddr());
        msg.append(" [uri=").append(request.getRequestURI());

        if (isIncludeQueryString()) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (isIncludeTakeTime()) {
            // Starting time
            long startTime = (Long) request.getAttribute(START_TIME_KEY);
            // Time-consuming
            long duration = System.currentTimeMillis() - startTime;
            msg.append(";processingTime=").append(duration).append("ms");
        }

        if (isIncludeHeaders()) {
            msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
        }

        if (isIncludeRequestPayload()) {
            String payload = getTextContent(request.getContentType(), request::getContentAsByteArray, request.getCharacterEncoding());
            if (payload != null) {
                msg.append(";requestBody=");
                appendAndTrim(msg,payload);
            }
        }

        if (isIncludeResponsePayload()) {
            String payload = getTextContent(response.getContentType(), response::getContentAsByteArray, response.getCharacterEncoding());
            if (payload != null) {
                msg.append(";responseBody=");
                appendAndTrim(msg,payload);
            }
        }

        msg.append(']');
        logger.info(msg.toString());
    }

    private void appendAndTrim(StringBuilder msg, String payload){
        for (char c : payload.toCharArray()) {
            if (c != '\n' && c != '\r') {
                msg.append(c);
            }
        }
    }

    /**
     * Get text content
     */
    protected String getTextContent(String contentType, Supplier<byte[]> supplier, String charsetName) {
        // Only text information is printed in the log
        if (contentType == null) {
            return "null";
        }
        if (!(contentType.contains("text") || contentType.contains("json"))) {
            return "[Content type: " + contentType + "]";
        }

        byte[] bytes = supplier.get();
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        String content;
        try {
            content = new String(bytes, charsetName);
        } catch (UnsupportedEncodingException ex) {
            content = "[UnsupportedEncoding: " + charsetName + "]";
        }
        return content;
    }
}
