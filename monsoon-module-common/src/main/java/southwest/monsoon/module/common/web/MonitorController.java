package southwest.monsoon.module.common.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import southwest.monsoon.module.common.web.result.R;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/monitor")
@Tag(name = "Monitor", description = "Show some runtime info, only for troubleshooting")
public class MonitorController {
    @GetMapping("/request-headers")
    @Operation(summary = "Show request header from browser")
    public R requestHeaders(HttpServletRequest request) {
        Map<String, Map<String, String>> map = new HashMap<>(2);

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> header = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            header.put(key, value);
        }
        map.put("Header", header);

        Map<String, String> attribute = new HashMap<>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            Object value = request.getAttribute(key);
            if (!"org.springframework.core.convert.ConversionService".equals(key)) {
                attribute.put(key, value.toString());
            }
        }
        map.put("Attribute", attribute);
        return R.success(map);
    }
}
