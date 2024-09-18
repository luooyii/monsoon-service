package southwest.monsoon.module.dictionary.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import southwest.monsoon.module.common.mybatis.vo.SelectOption;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;
import southwest.monsoon.module.dictionary.dto.DictDto;
import southwest.monsoon.module.dictionary.dto.DictItemDto;
import southwest.monsoon.module.dictionary.entity.MonDictionary;
import southwest.monsoon.module.dictionary.mapper.MonDictionaryMapper;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SysDict   System Dictionary
 *
 * @since 2022-05-05 13:13:38
 */
@Slf4j
@Service
public class MonDictionaryService extends ServiceImpl<MonDictionaryMapper, MonDictionary> {

    public Map<String, List<SelectOption>> getDictByCode(String code) {
        Set<String> codeSet = new HashSet<>();
        if (StringUtils.isNotBlank(code)) {
            String[] codeArr = code.split(";");
            for (String c : codeArr) {
                if (StringUtils.isNotBlank(c)) {
                    codeSet.add(c);
                }
            }
        }

        if (codeSet.isEmpty()) {
            throw WebException.err(SimpleMsg.format("Dict code({}) must not be blank!", code));
        }
        List<String> codes = baseMapper.getDictIds(codeSet);
        if (codes == null || codes.size() < codeSet.size()) {
            codeSet.removeAll(codes);
            throw WebException.err(SimpleMsg.format("Unknown dictionary code {}.", codeSet));
        }

        List<DictDto> dictDtos = baseMapper.getDictByCode(codeSet);
        Map<String, List<SelectOption>> dict = new HashMap<>();
        for (DictDto dictDto : dictDtos) {
            List<SelectOption> options = dictDto.getItems().stream()
                    .map(item -> new SelectOption(item.getCode(), item.getName()))
                    .collect(Collectors.toList());
            dict.put(dictDto.getCode(), options);
        }
        return dict;
    }

    public List<DictDto> listDict(String code) {
        QueryWrapper<MonDictionary> wrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(code)) {
            wrapper.eq("code", code);
        }
        List<MonDictionary> list = list(wrapper);

        List<DictDto> dicts = new ArrayList<>();
        list.forEach(item -> {
            DictDto dictDto = getDictDto(item);
            dicts.add(dictDto);
        });
        return dicts;
    }

    private DictDto getDictDto(MonDictionary par) {
        if (par == null) {
            return null;
        }

        QueryWrapper<MonDictionary> wrapper = new QueryWrapper();
        wrapper.eq("parent_id", par.getId());
        List<MonDictionary> list = list(wrapper);

        DictDto dict = new DictDto();
        dict.setCode(par.getId());
        dict.setName(par.getName());
        dict.setDescription(par.getDescription());
        dict.setItems(new ArrayList<>());

        list.forEach(item -> {
            DictItemDto itemDto = new DictItemDto();
            itemDto.setCode(item.getId());
            itemDto.setName(item.getName());
            itemDto.setSeq(item.getSeq());
            itemDto.setDescription(item.getDescription());
            dict.getItems().add(itemDto);
        });
        return dict;
    }

    @Transactional
    public void save(DictDto dict, String userId) {
        Page<MonDictionary> page = baseMapper.getOneByCode(dict.getCode(), Page.of(1, 1));
        MonDictionary monDictionary = CollectionUtils.firstElement(page.getRecords());
        if (monDictionary != null) {
            if (!Boolean.TRUE.equals(monDictionary.getBeDirectory())) {
                throw WebException.err(SimpleMsg.format("The dictionary code({}) already exists.", monDictionary.getId()));
            }
            deleteItems(monDictionary);
        }

        Set<String> codeSet = new HashSet<>();
        List<DictItemDto> items = dict.getItems();
        for (DictItemDto dto : items) {
            if (dto.getCode().contains("|")) {
                throw WebException.err(SimpleMsg.text("Dictionary code must not contain '|'."));
            }
            codeSet.add(dto.getCode());
        }
        List<String> exist = baseMapper.getDictIds(codeSet);
        if (exist != null && !exist.isEmpty()) {
            throw WebException.err(SimpleMsg.format("The dictionary code {} already exists.", exist));
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (monDictionary == null) {
            if (dict.getCode().contains("|")) {
                throw WebException.err(SimpleMsg.text("Dictionary code must not contain '|'."));
            }
            MonDictionary parent = getById(1522125783626784775L);
            monDictionary = new MonDictionary();
            monDictionary.setId(dict.getCode());
            monDictionary.belongTo(parent);
            monDictionary.setCreateBy(userId);
        }
        monDictionary.setName(dict.getName());
        monDictionary.setDescription(dict.getDescription());
        monDictionary.setUpdateBy(userId);
        monDictionary.setUpdateTime(now);
        monDictionary.setBeDirectory(true);
        saveOrUpdate(monDictionary);

        for (DictItemDto dto : items) {
            MonDictionary item = new MonDictionary();
            item.setId(dto.getCode());
            item.belongTo(monDictionary);
            item.setName(dto.getName());
            item.setSeq(dto.getSeq());
            item.setDescription(dto.getDescription());
            item.setBeDirectory(false);
            item.setCreateBy(userId);
            item.setUpdateBy(userId);
            save(item);
        }
    }

    @Transactional
    public void delete(Long id, String eid) {
        MonDictionary dict = getById(id);
        if (dict == null || dict.getId() == null) {
            throw WebException.err(SimpleMsg.text("No such dictionary item was found."));
        }
        deleteItems(dict);
        removeById(id);
    }

    private void deleteItems(MonDictionary dict) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String fullCode = dict.getFullPathId() + "|";
        //fullCode = SqlUtils.escapeLike(fullCode, false, true);
        baseMapper.deleteByFullCode(fullCode);
        baseMapper.deleteByParentId(dict.getId());
    }

    public List<MonDictionary> directoryList(String id) {
        LambdaQueryWrapper<MonDictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonDictionary::getBeDirectory, true);
        if (StringUtils.isNotBlank(id)) {
            wrapper.eq(MonDictionary::getId, id);
        }
        List<MonDictionary> list = list(wrapper);
        return list;
    }

    public List<MonDictionary> itemList(String id) {
        LambdaQueryWrapper<MonDictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonDictionary::getBeDirectory, true);
        if (StringUtils.isNotBlank(id)) {
            wrapper.eq(MonDictionary::getPid, id);
        }
        List<MonDictionary> list = list(wrapper);
        return list;
    }

    public List<MonDictionary> listItemByPid(String pid) {
        return baseMapper.listItemByPid(pid);
    }
}