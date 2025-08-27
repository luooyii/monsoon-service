package southwest.monsoon.module.common.mybatis.service;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import southwest.monsoon.module.common.mybatis.entity.LongIdWithVersionControl;
import southwest.monsoon.module.common.mybatis.mapper.VersionControlMapper;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static southwest.monsoon.module.common.mybatis.DbConst.MAX_TIME_TS;

@Slf4j
@Validated
public class ServiceWithVersionControl<M extends VersionControlMapper<T>, T extends LongIdWithVersionControl> extends ServiceImpl<M, T> {
    public void checkVersion(T entity) {
        if (entity.getId() == null || Objects.equals(0L, entity.getId())) {
            entity.setId(null);
        }

        if (entity.getId() != null) {
            T latest = getLatestById(entity);
            if (latest == null) {
                entity.setId(null);
            } else {
                if (Boolean.TRUE.equals(latest.getDeleted())) {
                    throw WebException.err(HttpStatus.GONE, SimpleMsg.format("{} already removed this on {}.", latest.getEraseBy(), latest.getEraseTime()));
                }
                if (!Objects.equals(latest.getVersion(), entity.getVersion())) {
                    throw WebException.err(HttpStatus.GONE, SimpleMsg.format("{} already revised this on {}, please refresh and try again.", entity.getUpdateBy(), entity.getUpdateTime()));
                }
            }
        }
    }

    public void saveNewVersion(T entity) {
        log.info("Validation passed. Update: {}", entity);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String updateBy = entity.getUpdateBy();
        if (entity.getId() != null) {
            T latest = getLatestById(entity);
            if (latest != null) {
                eraseActivation(now, updateBy, latest.getId());
                entity.inherit(latest);
            }
        } else {
            entity.firstInit(now);
        }
        entity.setEraseTime(MAX_TIME_TS);
        entity.setDeleted(null);
        entity.setUpdateTime(now);
        baseMapper.insert(entity);
    }

    public int eraseActivation(Timestamp time, String updateBy,
                               @NotNull(message = "Id {jakarta.validation.constraints.NotNull.message}!") Long id) {
        return baseMapper.eraseActivationById(time, updateBy, id);
    }

    private T getLatestById(T entity) {
        return baseMapper.getLatestById(entity.getId());
    }

    public void checkFieldDuplication(T entity, String propertyName, String columnName) throws InvocationTargetException, IllegalAccessException {
        String val = getProperty(entity, propertyName);
        List<T> sameNameList = query().eq(columnName, val)
                .ne("id", entity.getId()).list();
        if (!CollectionUtils.isEmpty(sameNameList)) {
            T sameNameEntity = sameNameList.get(0);
            if (!Objects.equals(sameNameEntity.getId(), entity.getId())) {
                throw WebException.err(HttpStatus.CONFLICT, SimpleMsg.format("{} ({}) already exists.", propertyName, val));
            }
        }
    }

    private String getProperty(Object object, String name) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(object.getClass(), name);
        if (pd == null) {
            throw WebException.err(HttpStatus.NOT_FOUND, SimpleMsg.format("{} field not found", name));
        }
        Method getter = pd.getReadMethod();
        return (String) getter.invoke(object);
    }

    public T getByPrimeKey(@NotNull(message = "Id {jakarta.validation.constraints.NotNull.message}!") Long id,
                           @NotNull(message = "Version {jakarta.validation.constraints.NotNull.message}!") Integer version) {
        return baseMapper.getByPrimeKey(id, version);
    }
}
