package southwest.monsoon.module.common.mybatis.mapper;

import java.util.List;

/**
 * @version 6/28/2021
 */
public interface BaseMergeMapper<T> {

    int merge(T entity);

    int mergeBatch(List<T> list);

}
