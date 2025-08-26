package southwest.monsoon.module.common.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Slf4j
@Component
public class ServiceInfoApplicationRunner implements ApplicationRunner {
    @Autowired
    private ServerProperties serverProperties;
    @Value("${server.servlet.context-path:}")
    private String path;
    @Value("${spring.application.name:}")
    private String serviceName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printServiceUrl();
    }

    private void printServiceUrl() {
        if (serverProperties == null) {
            return;
        }

        Integer port = serverProperties.getPort();
        if (port == null) {
            port = 8080;
        }

        if (StringUtils.isBlank(serviceName)) {
            serviceName = "Service";
        }

        String protocol = "http";
        Ssl ssl = serverProperties.getSsl();
        if (ssl != null) {
            protocol = "https";
        }

        log.info("\n----------------------------------------------------------\n\t" +
                serviceName + " is running! Access URLs:\n\t" +
                "Local: \t\t" + protocol + "://localhost:" + port + path + "/\n\t" +
                "Swagger: \t" + protocol + "://localhost:" + port + path + "/swagger\n\t" +
                "Time Zone: \t" + TimeZone.getDefault() + "\n" +
                "----------------------------------------------------------");
    }
}
