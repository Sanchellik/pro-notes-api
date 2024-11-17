package ru.gozhan.pronotesapi.test.unit.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gozhan.pronotesapi.domain.exception.AccessDeniedException;
import ru.gozhan.pronotesapi.domain.user.Role;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.test.constant.TokenConstant;
import ru.gozhan.pronotesapi.test.data.builder.UserBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;
import ru.gozhan.pronotesapi.web.secutiry.JwtProperties;
import ru.gozhan.pronotesapi.web.secutiry.JwtTokenProvider;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest extends AbstractUnitTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    private SecretKey key;

    @BeforeEach
    public void setUp() {
        when(jwtProperties.getSecret()).thenReturn(TokenConstant.SECRET);

        jwtTokenProvider.init();
        key = Keys.hmacShaKeyFor(TokenConstant.SECRET.getBytes());
    }

    @Nested
    @DisplayName("Tests for createAccessToken(..) method.")
    class CreateAccessToken {

        @Test
        @DisplayName("""
                Method createAccessToken.
                Given correct input.
                Then returns access token.
                """)
        void givenCorrectInput_whenCreateAccessToken_thenReturnToken() {
            // given
            Long userId = 1L;
            String username = "sanchellik";
            Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_USER));

            long accessDuration = 2L;

            Instant expectedExpiration = Instant.now()
                    .plus(accessDuration, ChronoUnit.HOURS);

            // when
            when(jwtProperties.getAccess())
                    .thenReturn(accessDuration);

            String accessToken = jwtTokenProvider.createAccessToken(userId, username, roles);

            // then
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();

            assertAll("Claims in the access token",
                    () -> assertEquals(username, claims.getSubject()),
                    () -> assertEquals(userId, claims.get("id", Long.class)),
                    () -> assertTrue(claims.get("roles", List.class).contains("ROLE_USER")),
                    () -> assertTrue(claims.getExpiration().after(new Date())),
                    () -> assertExpirationWithinRange(claims, expectedExpiration)
            );
        }

    }

    @Nested
    @DisplayName("Tests for createRefreshToken(..) method.")
    class CreateRefreshToken {

        @Test
        @DisplayName("""
                Method createRefreshToken.
                Given correct input.
                Then returns refresh token.
                """)
        void givenCorrectInput_whenCreateRefreshToken_thenReturnToken() {
            // given
            Long userId = 1L;
            String username = "sanchellik";

            long refreshDuration = 30;

            Instant expectedExpiration = Instant.now()
                    .plus(refreshDuration, ChronoUnit.DAYS);

            // when
            when(jwtProperties.getRefresh())
                    .thenReturn(refreshDuration);

            String refreshToken = jwtTokenProvider.createRefreshToken(userId, username);

            // then
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            assertAll("Claims in the access token",
                    () -> assertEquals(username, claims.getSubject()),
                    () -> assertEquals(userId, claims.get("id", Long.class)),
                    () -> assertTrue(claims.getExpiration().after(new Date())),
                    () -> assertExpirationWithinRange(claims, expectedExpiration)
            );
        }

    }

    @Nested
    @DisplayName("Tests for isValid(..) method.")
    class IsValid {

        @Test
        @DisplayName("""
                Method isValid.
                Given valid token.
                Then returns true.
                """)
        void givenValidToken_whenIsValid_thenReturnTrue() {
            // given
            String accessToken = TokenConstant.FRESH_ACCESS_TOKEN;

            // when
            boolean isValid = jwtTokenProvider.isValid(accessToken);

            // then
            assertTrue(isValid);
        }

        @Disabled("""
                This test is temporarily disabled because the JwtTokenProvider class does not
                currently handle expired tokens correctly.
                The isValid method should be enhanced to either
                return false or throw an appropriate exception when a token is expired.
                Steps to fix:
                1. Update JwtTokenProvider.isValid method to handle expired tokens properly.
                2. Ensure that it returns false or throws a specific
                exception when the token is expired.
                Once JwtTokenProvider is updated,
                please re-enable this test and verify its correctness.
                """)
        @Test
        @DisplayName("""
                Method isValid.
                Given invalid token.
                Then returns false.
                """)
        void givenInvalidToken_whenIsValid_thenReturnFalse() {
            // given
            String accessToken = TokenConstant.EXPIRED_ACCESS_TOKEN;

            // when & then
            assertThrows(
                    ExpiredJwtException.class,
                    () -> jwtTokenProvider.isValid(accessToken)
            );
        }

    }

    @Nested
    @DisplayName("Tests for refreshUserTokens(..) method.")
    class RefreshUserTokens {

        @Test
        @DisplayName("""
                Method refreshUserTokens.
                Given valid refreshToken.
                Then returns JwtResponse.
                """)
        void givenValidRefreshToken_whenRefreshUserTokens_thenReturnToken() {
            // given
            Long userId = 1L;
            User user = new UserBuilder().build();

            String refreshToken = TokenConstant.FRESH_REFRESH_TOKEN;

            // when
            when(userService.getById(userId))
                    .thenReturn(user);

            when(jwtProperties.getAccess())
                    .thenReturn(3L);

            when(jwtProperties.getRefresh())
                    .thenReturn(30L);

            JwtResponse jwtResponse = jwtTokenProvider.refreshUserTokens(refreshToken);

            // then
            assertAll(
                    () -> assertEquals(userId, jwtResponse.getId()),
                    () -> assertEquals("sanchellik", jwtResponse.getUsername())
            );
        }

        @Disabled("""
                This test is temporarily disabled because the JwtTokenProvider class
                does not currently handle expired tokens properly.
                The isValid method needs to be modified to detect
                and throw an AccessDeniedException when a token is expired.
                Once JwtTokenProvider is updated to handle this case,
                please re-enable and verify this test.
                """)
        @Test
        @DisplayName("""
                Method refreshUserTokens.
                Given invalid refreshToken.
                Then throws AccessDeniedException.
                """)
        void givenInvalidRefreshToken_whenRefreshUserTokens_thenThrowsAccessDeniedException() {
            // given
            Long userId = 1L;
            String username = "sanchellik";

            String expiredToken = Jwts.builder()
                    .subject(username)
                    .claim("id", userId)
                    .expiration(Date.from(Instant.now().minus(10, SECONDS)))
                    .signWith(key)
                    .compact();

            JwtResponse jwtResponse = jwtTokenProvider.refreshUserTokens(expiredToken);

            // when & then
            assertThrows(
                    AccessDeniedException.class,
                    () -> assertEquals("sanchellik", jwtResponse.getUsername())
            );
        }

    }

    @Nested
    @DisplayName("Tests for getAuthentication(..) method.")
    class GetAuthentication {

        @Test
        @DisplayName("""
                Method getAuthentication.
                Given accessToken.
                Then returns authentication.
                """)
        void givenAccessToken_whenGetAuthentication_thenReturnAuthentication() {
            // given
            String accessToken = TokenConstant.FRESH_ACCESS_TOKEN;
            UserDetails userDetails = mock(UserDetails.class);

            // when
            when(userDetailsService.loadUserByUsername("sanchellik"))
                    .thenReturn(userDetails);

            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // then
            assertEquals(UsernamePasswordAuthenticationToken.class, authentication.getClass());
            assertEquals(userDetails, authentication.getPrincipal());
            assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        }

        @Test
        @DisplayName("""
                Method getAuthentication.
                Given refreshToken.
                Then returns authentication.
                """)
        void givenRefreshToken_whenGetAuthentication_thenReturnAuthentication() {
            // given
            String accessToken = TokenConstant.FRESH_REFRESH_TOKEN;
            UserDetails userDetails = mock(UserDetails.class);

            // when
            when(userDetailsService.loadUserByUsername("sanchellik"))
                    .thenReturn(userDetails);

            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // then
            assertEquals(UsernamePasswordAuthenticationToken.class, authentication.getClass());
            assertEquals(userDetails, authentication.getPrincipal());
            assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        }

    }

    private void assertExpirationWithinRange(
            final Claims claims,
            final Instant expectedExpiration
    ) {
        Instant expirationInstant = claims.getExpiration().toInstant();

        int tolerance = 2;
        Instant minExpectedExpiration = expectedExpiration
                .minus(tolerance, SECONDS);

        Instant maxExpectedExpiration = expectedExpiration
                .plus(tolerance, SECONDS);

        assertTrue(
                expirationInstant.isAfter(minExpectedExpiration)
                        && expirationInstant.isBefore(maxExpectedExpiration),
                "Expiration time should be within the expected range."
        );
    }

}
