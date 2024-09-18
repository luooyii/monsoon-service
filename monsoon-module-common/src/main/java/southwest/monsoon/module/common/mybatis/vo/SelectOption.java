package southwest.monsoon.module.common.mybatis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @version 5/6/2022
 */
@Getter
@Setter
@ToString
@Schema(description = "Options for the drop-down box")
public class SelectOption {
    @Schema(description = "Option's label", requiredMode = Schema.RequiredMode.REQUIRED)
    private String label;
    @Schema(description = "Option's value", requiredMode = Schema.RequiredMode.REQUIRED)
    private String value;

    public SelectOption() {
    }

    public SelectOption(String val) {
        this.value = val;
        this.label = val;
    }

    public SelectOption(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
