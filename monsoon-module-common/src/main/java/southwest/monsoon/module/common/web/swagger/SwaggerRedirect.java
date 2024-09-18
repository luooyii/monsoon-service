package southwest.monsoon.module.common.web.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @version 12/23/2021
 */
@Slf4j
@Controller
public class SwaggerRedirect {
    private final String redirectUil = "redirect:/swagger-ui/index.html";

    @GetMapping({"/swagger", "/swagger-ui"})
    public String swaggerui() {
        log.info("Redirect to: {}", redirectUil);
        return redirectUil;
    }
}
