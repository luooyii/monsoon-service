package southwind.monsoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(scanBasePackages = {"southwest.monsoon"})
public class IntegrationService {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationService.class, args);
    }
}
