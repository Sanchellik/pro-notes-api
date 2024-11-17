package ru.gozhan.pronotesapi.test.unit.web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.gozhan.pronotesapi.config.SecurityConfig;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.AuthService;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.constant.TokenConstant;
import ru.gozhan.pronotesapi.test.data.builder.JwtRequestBuilder;
import ru.gozhan.pronotesapi.test.data.builder.JwtResponseBuilder;
import ru.gozhan.pronotesapi.test.data.builder.UserBuilder;
import ru.gozhan.pronotesapi.test.data.builder.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.test.util.JsonUtil;
import ru.gozhan.pronotesapi.test.util.MockMvcUtil;
import ru.gozhan.pronotesapi.web.controller.AuthController;
import ru.gozhan.pronotesapi.web.dto.JwtRefreshRequest;
import ru.gozhan.pronotesapi.web.dto.JwtRequest;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;
import ru.gozhan.pronotesapi.web.dto.UserDto;
import ru.gozhan.pronotesapi.web.mapper.UserMapper;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest extends AbstractUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @DisplayName("""
            API Endpoint POST /login.
            Given correct JwtRequest.
            Then returns JwtResponse.
            """)
    @SneakyThrows
    void givenJwtRequest_whenLogin_thenReturnsJwtResponse() {
        // given
        JwtRequest jwtRequest = new JwtRequestBuilder().build();

        var request = post(ApiEndpoint.LOGIN.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(jwtRequest));

        JwtResponse jwtResponse = new JwtResponseBuilder().build();

        // when
        when(authService.login(refEq(jwtRequest)))
                .thenReturn(jwtResponse);

        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jwtResponse.getId()))
                .andExpect(jsonPath("$.username").value(jwtResponse.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.getRefreshToken()));
    }

    @Test
    @DisplayName("""
            API Endpoint POST /register.
            Given correct UserDto.
            Then returns UserDto.
            """)
    @SneakyThrows
    void givenUserDto_whenLogin_thenReturnsJwtResponse() {
        // given
        User mappedUser = new UserBuilder()
                .id(null)
                .roles(null)
                .build();

        User createdUser = new UserBuilder().build();

        UserDto userDtoResponse = new UserDtoBuilder()
                .passwordConfirmation(null)
                .build();

        UserDto userDtoRequest = new UserDtoBuilder()
                .id(null)
                .build();

        var request = post(ApiEndpoint.REGISTER.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(userDtoRequest));

        // when
        when(userMapper.toEntity(refEq(userDtoRequest)))
                .thenReturn(mappedUser);

        when(userService.create(refEq(mappedUser)))
                .thenReturn(createdUser);

        when(userMapper.toDto(refEq(createdUser)))
                .thenReturn(userDtoResponse);

        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDtoResponse.getId()))
                .andExpect(jsonPath("$.name").value(userDtoResponse.getName()))
                .andExpect(jsonPath("$.username").value(userDtoResponse.getUsername()));
    }

    @Test
    @DisplayName("""
            API Endpoint POST /refresh.
            Given JwtRefreshRequest.
            Then returns JwtResponse.
            """)
    @SneakyThrows
    void givenJwtRefreshRequest_whenRefresh_thenReturnsJwtResponse() {
        // given
        JwtRefreshRequest jwtRefreshRequest = new JwtRefreshRequest();
        jwtRefreshRequest.setRefreshToken(TokenConstant.FRESH_REFRESH_TOKEN);

        var request = post(ApiEndpoint.REFRESH.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(jwtRefreshRequest));

        JwtResponse jwtResponse = new JwtResponseBuilder().build();

        // when
        when(authService.refresh(refEq(TokenConstant.FRESH_REFRESH_TOKEN)))
                .thenReturn(jwtResponse);

        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jwtResponse.getId()))
                .andExpect(jsonPath("$.username").value(jwtResponse.getUsername()))
                .andExpect(jsonPath("$.accessToken").value(jwtResponse.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(jwtResponse.getRefreshToken()));
    }

}
