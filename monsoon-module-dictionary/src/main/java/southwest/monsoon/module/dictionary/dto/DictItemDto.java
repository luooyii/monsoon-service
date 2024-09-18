package southwest.monsoon.module.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DictItemDto {
    @NotBlank(message = "Dict item code {jakarta.validation.constraints.NotBlank.message}!")
    private String code;

    @NotBlank(message = "Dict item name {jakarta.validation.constraints.NotBlank.message}!")
    private String name;

    private String description;

    private Integer seq;
}
