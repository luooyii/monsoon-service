package southwest.monsoon.module.common;

import db.DbUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Slf4j
@Configuration
public class AotProcessor implements BeanFactoryInitializationAotProcessor {
    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
        return (context, code) -> {
            RuntimeHints hints = context.getRuntimeHints();
            try {
                hints.reflection().registerMethod(DbUtil.class.getMethod("now"), ExecutableMode.INVOKE);

                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources("classpath*:monsoon/i18n/*.properties");
                for (Resource resource : resources) {
                    String path = "monsoon/i18n/" + resource.getFilename();
                    log.info("Add {} to AOT resources.", path);
                    hints.resources().registerPattern(path);
                }

                hints.resources().registerPattern("common-logback.xml");
            } catch (Exception e) {
                log.error("Fail to execute SQL scripts.", e);
                throw new RuntimeException(e);
            }
        };
    }
}
