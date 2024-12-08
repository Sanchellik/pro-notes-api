package ru.gozhan.pronotesapi.test.unit.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.service.impl.AuthServiceImpl;
import ru.gozhan.pronotesapi.test.constant.TokenConstant;
import ru.gozhan.pronotesapi.test.data.builder.JwtRequestBuilder;
import ru.gozhan.pronotesapi.test.data.builder.JwtResponseBuilder;
import ru.gozhan.pronotesapi.test.data.builder.UserBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.dto.JwtRequest;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;
import ru.gozhan.pronotesapi.web.secutiry.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("""
            Method login.
            Given valid JwtRequest.
            Then authenticates and returns JwtResponse with tokens.
            """)
    void givenValidLoginRequest_whenLogin_thenReturnsJwtResponseWithTokens() {
        // given
        Long userId = 1L;
        String username = "sanchellik";
        String password = "123";
        String hashedPassword = "hashed123";

        JwtRequest loginRequest = new JwtRequestBuilder()
                .password(password)
                .build();

        User user = new UserBuilder()
                .password(hashedPassword)
                .passwordConfirmation(null)
                .build();

        when(userService.getByUsername(anyString()))
                .thenReturn(user);

        when(jwtTokenProvider.createAccessToken(anyLong(), anyString(), anySet()))
                .thenReturn(TokenConstant.FRESH_ACCESS_TOKEN);

        when(jwtTokenProvider.createRefreshToken(anyLong(), anyString()))
                .thenReturn(TokenConstant.FRESH_REFRESH_TOKEN);

        // when
        JwtResponse jwtResponse = authServiceImpl.login(loginRequest);

        // then
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, password)
        );
        verify(userService).getByUsername(username);
        verify(jwtTokenProvider).createAccessToken(userId, username, user.getRoles());
        verify(jwtTokenProvider).createRefreshToken(userId, username);

        assertEquals(userId, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(TokenConstant.FRESH_ACCESS_TOKEN, jwtResponse.getAccessToken());
        assertEquals(TokenConstant.FRESH_REFRESH_TOKEN, jwtResponse.getRefreshToken());
    }

    @Test
    @DisplayName("""
            Method login.
            Given invalid JwtRequest (authentication fails).
            Then throws AuthenticationException.
            """)
    void givenInvalidLoginRequest_whenLogin_thenThrowsAuthenticationException() {
        // given
        JwtRequest loginRequest = new JwtRequestBuilder()
                .username("invalidUser")
                .password("wrongPassword")
                .build();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // when-then
        assertThrows(
                AuthenticationException.class,
                () -> authServiceImpl.login(loginRequest)
        );

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("invalidUser", "wrongPassword")
        );
        verify(userService, never()).getByUsername(anyString());
        verify(jwtTokenProvider, never()).createAccessToken(anyLong(), anyString(), anySet());
        verify(jwtTokenProvider, never()).createRefreshToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("""
            Method login.
            Given valid JwtRequest but user not found.
            Then throws IllegalArgumentException.
            """)
    void givenValidLoginRequestButUserNotFound_whenLogin_thenThrowsIllegalArgumentException() {
        // given
        JwtRequest loginRequest = new JwtRequestBuilder()
                .username("nonexistentUser")
                .password("password")
                .build();

        when(userService.getByUsername("nonexistentUser"))
                .thenThrow(new IllegalArgumentException("User not found"));

        // when-then
        assertThrows(IllegalArgumentException.class, () -> authServiceImpl.login(loginRequest));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("nonexistentUser", "password")
        );
        verify(userService).getByUsername("nonexistentUser");

        verify(jwtTokenProvider, never()).createAccessToken(anyLong(), anyString(), anySet());
        verify(jwtTokenProvider, never()).createRefreshToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("""
            Method refresh.
            Given valid refresh token.
            Then returns JwtResponse with new tokens.
            """)
    void givenValidRefreshToken_whenRefresh_thenReturnsJwtResponseWithNewTokens() {
        // given
        String refreshToken = TokenConstant.FRESH_REFRESH_TOKEN;

        JwtResponse expectedResponse = new JwtResponseBuilder().build();

        when(jwtTokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(expectedResponse);

        // when
        JwtResponse jwtResponse = authServiceImpl.refresh(refreshToken);

        // then
        verify(jwtTokenProvider).refreshUserTokens(refreshToken);

        assertEquals(TokenConstant.FRESH_ACCESS_TOKEN, jwtResponse.getAccessToken());
        assertEquals(TokenConstant.FRESH_REFRESH_TOKEN, jwtResponse.getRefreshToken());
    }

    @Test
    @DisplayName("""
            Method refresh.
            Given invalid refresh token.
            Then throws IllegalArgumentException.
            """)
    void givenInvalidRefreshToken_whenRefresh_thenThrowsIllegalArgumentException() {
        // given
        String invalidRefreshToken = "invalid.refresh.token";

        when(jwtTokenProvider.refreshUserTokens(invalidRefreshToken))
                .thenThrow(new IllegalArgumentException("Invalid refresh token"));

        // when-then
        assertThrows(
                IllegalArgumentException.class,
                () -> authServiceImpl.refresh(invalidRefreshToken)
        );

        verify(jwtTokenProvider).refreshUserTokens(invalidRefreshToken);
    }

}
