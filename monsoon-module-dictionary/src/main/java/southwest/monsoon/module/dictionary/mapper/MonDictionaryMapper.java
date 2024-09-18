package southwest.monsoon.module.dictionary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import southwest.monsoon.module.dictionary.dto.DictDto;
import southwest.monsoon.module.dictionary.entity.MonDictionary;

import java.util.List;
import java.util.Set;

/**
 * SysDict   System Dictionary
 *
 * @since 2022-05-05 13:13:39
 */
@Mapper
public interface MonDictionaryMapper extends BaseMapper<MonDictionary> {
    List<DictDto> getDictByCode(Set<String> codeSet);

    @Select("SELECT * FROM mon_dictionary WHERE id = #{id}")
    Page<MonDictionary> getOneByCode(@Param("id") String id, IPage<MonDictionary> pageParam);

    @Delete("DELETE FROM mon_dictionary WHERE full_code like #{fullCode}")
    int deleteByFullCode(@Param("fullCode") String fullCode);

    @Delete("DELETE FROM mon_dictionary WHERE pid = #{pid}")
    int deleteByParentId( @Param("pid") String parentId);

    List<String> getDictIds(Set<String> idSet);

    @Select("SELECT * FROM mon_dictionary WHERE pid = #{val} AND be_directory = false")
    List<MonDictionary> listItemByPid(String pid);
}