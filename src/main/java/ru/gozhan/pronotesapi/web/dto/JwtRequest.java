package ru.gozhan.pronotesapi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(
            description = "username",
            example = "sanchellik"
    )
    @NotNull(
            message = "Username must be not null."
    )
    private String username;

    @Schema(
            description = "password",
            example = "123"
    )
    @NotNull(
            message = "Password must be not null."
    )
    private String password;

}
