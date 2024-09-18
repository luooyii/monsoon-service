package southwest.monsoon.module.common.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Slf4j
@Component
public class ServiceInfoApplicationRunner implements ApplicationRunner {
    @Autowired
    private ApplicationContext application;
    @Autowired
    private ServerProperties serverProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        printServiceUrl();
    }

    private void printServiceUrl() {
        if (application == null || serverProperties == null) {
            return;
        }

        Environment env = application.getEnvironment();
        Integer port = serverProperties.getPort();
        if (port == null) {
            port = 8080;
        }

        String path = env.getProperty("server.servlet.context-path");
        if (path == null) {
            path = "";
        }

        String serviceName = env.getProperty("spring.application.name");
        if (serviceName == null) {
            serviceName = "Service";
        }

        log.info("\n----------------------------------------------------------\n\t" +
                serviceName + " is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "Swagger: \thttp://localhost:" + port + path + "/swagger\n\t" +
                "Time Zone: \t" + TimeZone.getDefault() + "\n" +
                "----------------------------------------------------------");
    }
}
