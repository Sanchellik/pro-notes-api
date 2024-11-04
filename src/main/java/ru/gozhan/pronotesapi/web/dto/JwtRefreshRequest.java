package ru.gozhan.pronotesapi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request for refresh")
public class JwtRefreshRequest {

    @Schema(
            description = "refreshToke"
    )
    @NotNull(
            message = "Refresh token must be not null."
    )
    private String refreshToken;

}
