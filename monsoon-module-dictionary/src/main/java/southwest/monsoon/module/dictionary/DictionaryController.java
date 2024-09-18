package southwest.monsoon.module.dictionary;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import southwest.monsoon.module.common.web.result.R;
import southwest.monsoon.module.dictionary.dto.DictDto;
import southwest.monsoon.module.dictionary.entity.MonDictionary;
import southwest.monsoon.module.dictionary.service.MonDictionaryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dictionary-api")
@Tag(name = "Data Dictionary API")
@MapperScan("southwest.monsoon.module.dictionary.mapper")
public class DictionaryController {
    @Autowired
    private MonDictionaryService dictService;

    @GetMapping("/directory-list")
    public R<List<MonDictionary>> directoryList(@RequestParam(required = false) String id) {
        List<MonDictionary> list = dictService.directoryList(id);
        return R.success(list);
    }

    @GetMapping("/item-list")
    public R<List<MonDictionary>> itemList(String pid) {
        List<MonDictionary> list = dictService.listItemByPid(pid);
        return R.success(list);
    }

    @PostMapping
    public R save(@Valid @RequestBody DictDto dict) {
        dictService.save(dict, "sys");
        return R.success();
    }
}
