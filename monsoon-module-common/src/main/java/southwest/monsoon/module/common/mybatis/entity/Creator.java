package southwest.monsoon.module.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Creator {
    @Schema(example = "USERNAME")
    protected String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(type = "integer", format = "int64", example = "1641312000000")
    protected Timestamp createTime;
}
