package southwest.monsoon.module.common.mybatis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 4/27/2022
 */
@Getter
@Setter
@Schema(description = "Paging parameters")
public class PageParam {

    @Schema(description = "Current page. Start from 1, the default is 1.", defaultValue = "1")
    protected Integer curPage = 1;

    @Schema(description = "Page size. The default is 10.", defaultValue = "10")
    protected Integer pageSize = 10;
}
