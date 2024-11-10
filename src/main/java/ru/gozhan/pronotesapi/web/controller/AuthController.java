package ru.gozhan.pronotesapi.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gozhan.pronotesapi.config.openapi.UserView;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.AuthService;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.web.dto.JwtRefreshRequest;
import ru.gozhan.pronotesapi.web.dto.JwtRequest;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;
import ru.gozhan.pronotesapi.web.dto.UserDto;
import ru.gozhan.pronotesapi.web.dto.validation.OnCreate;
import ru.gozhan.pronotesapi.web.mapper.UserMapper;

@Slf4j
@RestController
@RequestMapping("pro-notes/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Auth Controller",
        description = "Auth API"
)
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(
            @Validated @RequestBody final JwtRequest loginRequest
    ) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @JsonView(UserView.Detail.class)
    public UserDto register(
            @Validated(OnCreate.class)
            @RequestBody @JsonView(UserView.Create.class) final UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(
            @Validated @RequestBody final JwtRefreshRequest jwtRefreshRequest
    ) {
        return authService.refresh(jwtRefreshRequest.getRefreshToken());
    }

}
