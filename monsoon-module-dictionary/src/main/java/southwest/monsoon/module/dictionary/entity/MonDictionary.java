package southwest.monsoon.module.dictionary.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import southwest.monsoon.module.common.i18n.LanguageKey;
import southwest.monsoon.module.common.i18n.LanguageKeySerializer;
import southwest.monsoon.module.common.mybatis.entity.CreatorUpdater;
import southwest.monsoon.module.common.web.result.exception.WebException;
import southwest.monsoon.module.common.web.result.msg.SimpleMsg;

/**
 * SysDict   System Dictionary
 *
 * @since 2022-05-05 13:50:30
 */
@Getter
@Setter
@ToString
@JsonSerialize(using = LanguageKeySerializer.class)
public class MonDictionary extends CreatorUpdater {

    /**
     * Dictionary ID
     */
    private String id;

    /**
     * Parent dictionary ID
     */
    private String pid;

    /**
     * Full Path ID
     */
    @JsonIgnore
    private String fullPathId;

    /**
     * Item sequences
     */
    private int seq;

    /**
     * Language Key
     */
    @LanguageKey
    private String langKey;

    /**
     * Dict/Item Name
     */
    private String name;

    /**
     * Dict/Item Description
     */
    private String description;

    /**
     * Is it a directory
     */
    private Boolean beDirectory;

    public void belongTo(MonDictionary parent) {
        if (parent == null || parent.getId() == null) {
            throw WebException.err(SimpleMsg.text("Data Dictionary's parent must not be null!"));
        }
        if (StringUtils.isBlank(this.id)) {
            throw WebException.err(SimpleMsg.text("Please set the dictionary code first!"));
        }
        this.pid = parent.getId();
        this.fullPathId = parent.getFullPathId() + '|' + this.id;
    }
}