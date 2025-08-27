package southwest.monsoon.module.common.mybatis.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import southwest.monsoon.module.common.mybatis.entity.IdWithVersionControl;

import java.sql.Timestamp;

public interface IdWithVersionControlMapper<T extends IdWithVersionControl> extends BaseMapper<T> {
    default T getByPrimeKey(Object id, Integer version) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        wrapper.eq(T::getVersion, version);
        return selectOne(wrapper);
    }


    default T getLatestById(Object id) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(T::getId, id);
        wrapper.orderByDesc(T::getVersion);
        return selectList(wrapper).getFirst();
    }

    default int eraseActivationById(Timestamp now, String eraseBy, Object id) {
        LambdaUpdateWrapper<T> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(T::getDeleted, true);
        wrapper.set(T::getEraseTime, now);
        wrapper.set(T::getEraseBy, eraseBy);
        wrapper.eq(T::getId, id);
        wrapper.eq(T::getDeleted, false);
        return update(wrapper);
    }
}
