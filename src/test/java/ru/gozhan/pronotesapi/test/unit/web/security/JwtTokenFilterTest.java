package ru.gozhan.pronotesapi.test.unit.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.secutiry.JwtTokenFilter;
import ru.gozhan.pronotesapi.web.secutiry.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtTokenFilterTest extends AbstractUnitTest {

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletResponse servletResponse;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given valid token in Authorization header.
            Then sets authentication.
            """)
    @SneakyThrows
    void givenValidToken_whenDoFilter_thenSetsAuthentication() {
        // given
        String validToken = "Bearer valid.jwt.token";
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(validToken);

        when(jwtTokenProvider.isValid("valid.jwt.token"))
                .thenReturn(true);

        when(jwtTokenProvider.getAuthentication("valid.jwt.token"))
                .thenReturn(authentication);

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider).isValid("valid.jwt.token");
        verify(jwtTokenProvider).getAuthentication("valid.jwt.token");
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertEquals(
                authentication,
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given no token in Authorization header.
            Then does not set authentication.
            """)
    @SneakyThrows
    void givenNoToken_whenDoFilter_thenDoesNotSetAuthentication() {
        // given
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(null);

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider, never()).isValid(anyString());
        verify(jwtTokenProvider, never()).getAuthentication(anyString());
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given token in Authorization header without prefix Bearer.
            Then does not set authentication.
            """)
    @SneakyThrows
    void givenTokenWithoutPrefix_whenDoFilter_thenDoesNotSetAuthentication() {
        // given
        String tokenWithoutPrefix = "valid.jwt.token";

        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(tokenWithoutPrefix);

        when(jwtTokenProvider.isValid(tokenWithoutPrefix))
                .thenReturn(true);

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider).isValid(anyString());
        verify(jwtTokenProvider).getAuthentication(anyString());
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given invalid token in Authorization header.
            Then does not set authentication.
            """)
    @SneakyThrows
    void givenInvalidToken_whenDoFilter_thenDoesNotSetAuthentication() {
        // given
        String invalidToken = "Bearer invalid.jwt.token";

        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(invalidToken);

        when(jwtTokenProvider.isValid("invalid.jwt.token"))
                .thenReturn(false);

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider).isValid("invalid.jwt.token");
        verify(jwtTokenProvider, never()).getAuthentication(anyString());
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given valid token but null authentication.
            Then does not set authentication.
            """)
    @SneakyThrows
    void givenValidTokenButNullAuthentication_whenDoFilter_thenDoesNotSetAuthentication() {
        // given
        String validToken = "Bearer valid.jwt.token";

        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(validToken);

        when(jwtTokenProvider.isValid("valid.jwt.token"))
                .thenReturn(true);

        when(jwtTokenProvider.getAuthentication("valid.jwt.token"))
                .thenReturn(null);

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider).isValid("valid.jwt.token");
        verify(jwtTokenProvider).getAuthentication("valid.jwt.token");
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("""
            Method doFilter.
            Given valid token.
            When exception is thrown.
            Then does not set authentication and continues filter chain.
            """)
    @SneakyThrows
    void givenValidToken_whenExceptionThrown_thenDoesNotSetAuthenticationAndContinuesFilterChain() {
        // given
        String validToken = "Bearer valid.jwt.token";

        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(validToken);

        when(jwtTokenProvider.isValid("valid.jwt.token"))
                .thenThrow(new RuntimeException("Token validation failed"));

        // when
        jwtTokenFilter.doFilter(httpServletRequest, servletResponse, filterChain);

        // then
        verify(jwtTokenProvider).isValid("valid.jwt.token");
        verify(jwtTokenProvider, never()).getAuthentication(anyString());
        verify(filterChain).doFilter(httpServletRequest, servletResponse);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
