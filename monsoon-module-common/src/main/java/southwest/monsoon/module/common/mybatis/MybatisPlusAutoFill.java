package southwest.monsoon.module.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

/**
 * @version 6/9/2021
 */
@Configuration
public class MybatisPlusAutoFill implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.strictInsertFill(metaObject, "createTime", Timestamp.class, timestamp);
        this.strictInsertFill(metaObject, "updateTime", Timestamp.class, timestamp);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Timestamp.class, new Timestamp(System.currentTimeMillis()));
    }
}
