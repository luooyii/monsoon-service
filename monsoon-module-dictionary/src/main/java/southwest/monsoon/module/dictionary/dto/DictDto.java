package southwest.monsoon.module.dictionary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DictDto {
    @NotBlank(message = "Dict code {jakarta.validation.constraints.NotBlank.message}!")
    private String code;

    @NotBlank(message = "Dict name {jakarta.validation.constraints.NotBlank.message}!")
    private String name;

    private String description;

    @NotEmpty(message = "Dict items {jakarta.validation.constraints.NotBlank.message}!")
    private List<DictItemDto> items;
}
