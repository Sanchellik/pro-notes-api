package ru.gozhan.pronotesapi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Response for GigaChat")
public class GigaChatResponse {

    @Schema(
            description = "answerMessage"
    )
    @NotNull(
            message = "Answer message must be not null."
    )
    private String answerMessage;

}
