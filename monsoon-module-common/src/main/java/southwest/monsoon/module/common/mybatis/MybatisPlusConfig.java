package southwest.monsoon.module.common.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig implements MybatisPlusPropertiesCustomizer {
    @Override
    public void customize(MybatisPlusProperties properties) {
        if (properties.getConfiguration() == null) {
            properties.setConfiguration(new MybatisPlusProperties.CoreConfiguration());
        }

        MybatisPlusProperties.CoreConfiguration configuration = properties.getConfiguration();
        configuration.setLogImpl(LevelUpSlf4jImpl.class);
    }
}
