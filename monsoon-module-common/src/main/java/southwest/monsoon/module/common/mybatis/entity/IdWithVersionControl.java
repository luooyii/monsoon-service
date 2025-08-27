package southwest.monsoon.module.common.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString(callSuper = true)
public abstract class IdWithVersionControl<T> {
    public abstract T getId();

    public abstract void setId(T id);

    protected Integer version;

    /**
     * 1 for activation
     */
    @TableLogic(value = "0", delval = "1")
    @Schema(hidden = true)
    protected Boolean deleted;

    /**
     * The creation time of the current version. (The latest version creation time is the update time of record) (Timestamp)
     */
    @Schema(description = "The creation time of the current version. (The latest version creation time is the plan's update time) (Timestamp) (Automatic generation)"
            , type = "integer", format = "int64", example = "1641312000000")
    protected Timestamp updateTime;

    /**
     * The creator of the current version. (The latest creator is the record modifier)
     */
    @Schema(description = "The creator of the current version. (The latest record creator is the plan modifier) (Automatic generation)", example = "USERNAME")
    protected String updateBy;

    /**
     * Creation time of 1st version (Timestamp)
     */
    @Schema(description = "Creation time of 1st version (Timestamp) (Automatic generation)",
            type = "integer", format = "int64", example = "1641312000000")
    protected Timestamp createTime;

    /**
     * Creator of 1st version
     */
    @Schema(description = "Creator of 1st version (Automatic generation)", example = "USERNAME")
    protected String createBy;

    /**
     * Record erased time of this version
     */
    @Schema(description = "Record erased time of this version", type = "integer", format = "int64", example = "1641312000000")
    protected Timestamp eraseTime;

    /**
     * Record this version is erased by
     */
    @Schema(description = "Record this version is erased by", example = "USERNAME")
    protected String eraseBy;

    public void inherit(IdWithVersionControl old) {
        setVersion(old.getVersion() + 1);
        setEraseBy(null);
        setCreateBy(old.getCreateBy());
        setCreateTime(old.getCreateTime());
    }

    public void firstInit(Timestamp initTime) {
        setId(null);
        setVersion(1);
        setEraseBy(null);
        setCreateBy(updateBy);
        setCreateTime(initTime);
    }
}
