package ru.gozhan.pronotesapi.test.unit.web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.gozhan.pronotesapi.config.SecurityConfig;
import ru.gozhan.pronotesapi.service.AuthService;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.provider.InvalidJwtRequestProvider;
import ru.gozhan.pronotesapi.test.provider.InvalidOnCreateUserDtoProvider;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.test.util.JsonUtil;
import ru.gozhan.pronotesapi.test.util.MockMvcUtil;
import ru.gozhan.pronotesapi.web.controller.AuthController;
import ru.gozhan.pronotesapi.web.dto.JwtRefreshRequest;
import ru.gozhan.pronotesapi.web.dto.JwtRequest;
import ru.gozhan.pronotesapi.web.dto.UserDto;
import ru.gozhan.pronotesapi.web.mapper.UserMapper;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
@DisplayName("AuthController Validation Tests: Verifying input validation")
public class AuthControllerValidationTest extends AbstractUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @ParameterizedTest(name = """
            {index} -> notValid field = {1}, errorMessage = {2}, are null values accepted = {3}
            """)
    @ArgumentsSource(InvalidOnCreateUserDtoProvider.class)
    @DisplayName("""
            API Endpoint /register.
            Test UserDto validation.
            """)
    @SneakyThrows
    void givenInvalidUserDtoWhenRegisterThenBadRequest(
            final UserDto userDto,
            final String fieldName,
            final String expectedError,
            final boolean includeNulls
    ) {
        // given
        String requestBody = includeNulls
                ? JsonUtil.toJsonWithNulls(userDto)
                : JsonUtil.toJsonWithoutNulls(userDto);

        var requestBuilder = post(ApiEndpoint.REGISTER.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        // when
        var resultActions = mockMvc.perform(requestBuilder)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath(String.format("$.errors.%s", fieldName))
                        .value(equalTo(expectedError)));
    }

    @ParameterizedTest(name = """
            {index} -> notValid field = {1}, errorMessage = {2}, are null values accepted = {3}
            """)
    @ArgumentsSource(InvalidJwtRequestProvider.class)
    @DisplayName("""
            API Endpoint /login.
            Test JwtRequest validation.
            """)
    @SneakyThrows
    void givenInvalidJwtRequestWhenLoginThenBadRequest(
            final JwtRequest jwtRequest,
            final String fieldName,
            final String expectedError,
            final boolean includeNulls
    ) {
        // given
        String requestBody = includeNulls
                ? JsonUtil.toJsonWithNulls(jwtRequest)
                : JsonUtil.toJsonWithoutNulls(jwtRequest);

        var requestBuilder = post(ApiEndpoint.LOGIN.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        // when
        var resultActions = mockMvc.perform(requestBuilder)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath(String.format("$.errors.%s", fieldName))
                        .value(equalTo(expectedError)));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("""
            API Endpoint POST /refresh.
            Test JwtRefreshRequest validation.
            """)
    @SneakyThrows
    void givenInvalidJwtRefreshRequestWhenLoginThenBadRequest(
            final boolean includeNulls
    ) {
        // given
        JwtRefreshRequest jwtRefreshRequest = new JwtRefreshRequest();

        String requestBody = includeNulls
                ? JsonUtil.toJsonWithNulls(jwtRefreshRequest)
                : JsonUtil.toJsonWithoutNulls(jwtRefreshRequest);

        var requestBuilder = post(ApiEndpoint.REFRESH.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        // when
        var resultActions = mockMvc.perform(requestBuilder)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(
                        jsonPath("$.errors.refreshToken")
                                .value(equalTo("Refresh token must be not null."))
                );
    }

}
