package southwest.monsoon.module.common.web.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AuthOpenApiConfig {
    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI defaultOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        Components components = new Components();
        components.addSecuritySchemes(
                "Authorization in Header",
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization")
        );
        openAPI.components(components)
                .addSecurityItem(new SecurityRequirement().addList("Authorization in Header"));
        return openAPI;
    }
}
