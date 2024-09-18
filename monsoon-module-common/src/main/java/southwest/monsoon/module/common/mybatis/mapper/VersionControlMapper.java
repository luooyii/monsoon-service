package southwest.monsoon.module.common.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

public interface VersionControlMapper<T> extends BaseMapper<T> {
    @Select("SELECT * FROM ${tableName} WHERE id = #{id} AND version = #{version}")
    T getByPrimeKey(@Param("id") Long id, @Param("version") Integer version, @Param("tableName") String tableName);

    @Select("SELECT TOP 1 * FROM ${tableName} WHERE id= #{id} ORDER BY version DESC")
    T getLatestById(@Param("id") long id, @Param("tableName") String tableName);

    @Update("UPDATE ${tableName} SET deleted = 1, erase_time = #{now}, erase_by = #{eraseBy} WHERE id= #{id} AND deleted = 0")
    int eraseActivationById(@Param("now") Timestamp now, @Param("eraseBy") String eraseBy, @Param("id") Long id, @Param("tableName") String tableName);
}
