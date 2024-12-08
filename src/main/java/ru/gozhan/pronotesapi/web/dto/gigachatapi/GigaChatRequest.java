package ru.gozhan.pronotesapi.web.dto.gigachatapi;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "Request for GigaChat")
public class GigaChatRequest {

    @Schema(
            description = "questionMessage"
    )
    @NotNull(
            message = "Question message must be not null."
    )
    private String questionMessage;

}
