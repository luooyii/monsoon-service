package southwest.monsoon.module.common.mybatis.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @version 5/5/2022
 */
@Getter
@Setter
@ToString
@Schema(description = "Paging Data Container")
public class PageRecord<T> {
    @Schema(description = "Total number of pages")
    private long total;
    @Schema(description = "Current page records")
    private List<T> records;

    public PageRecord() {
    }

    public PageRecord(IPage<T> page) {
        this.total = page.getTotal();
        this.records = page.getRecords();
    }
}
