package southwest.monsoon.module.common.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import southwest.monsoon.module.common.mybatis.entity.LongIdWithVersionControl;

import java.sql.Timestamp;

public interface VersionControlMapper<T extends LongIdWithVersionControl> extends BaseMapper<T> {
    default T getByPrimeKey(Long id, Integer version) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        wrapper.eq(T::getVersion, version);
        return selectOne(wrapper);
    }


    default T getLatestById(long id) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        wrapper.orderByDesc(T::getVersion);
        return selectList(wrapper).getFirst();
    }

    default int eraseActivationById(Timestamp now, String eraseBy, Long id) {
        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper();
        wrapper.set(T::getDeleted, true);
        wrapper.set(T::getEraseTime, now);
        wrapper.set(T::getEraseBy, eraseBy);
        wrapper.eq(T::getId, id);
        wrapper.eq(T::getDeleted, false);
        return update(wrapper);
    }
}
