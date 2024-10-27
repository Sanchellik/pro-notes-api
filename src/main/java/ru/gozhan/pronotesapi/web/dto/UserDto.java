package ru.gozhan.pronotesapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.gozhan.pronotesapi.web.dto.validation.OnCreate;
import ru.gozhan.pronotesapi.web.dto.validation.OnUpdate;

@Getter
@Setter
@Schema(description = "User DTO")
public class UserDto {

    @Schema(
            description = "User id",
            example = "1"
    )
    @NotNull(
            message = "Id must be not null.",
            groups = OnUpdate.class
    )
    private Long id;

    @Schema(
            description = "User name",
            example = "Alexandr Gozhan"
    )
    @NotNull(
            message = "Name must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Name length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @Schema(
            description = "Username",
            example = "sanchellik"
    )
    @NotNull(
            message = "Username must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Username length must be smaller than 255 symbols.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String username;

    @Schema(
            description = "User encrypted password"
    )
    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    @NotNull(
            message = "Password must be not null.",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String password;

    @Schema(
            description = "User password confirmation"
    )
    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    @NotNull(
            message = "Password confirmation must be not null.",
            groups = {OnCreate.class}
    )
    private String passwordConfirmation;

}
