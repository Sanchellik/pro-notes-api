package ru.gozhan.pronotesapi.test.unit.web.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.UserService;
import ru.gozhan.pronotesapi.test.data.builder.UserBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.secutiry.JwtEntity;
import ru.gozhan.pronotesapi.web.secutiry.JwtUserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JwtUserDetailsServiceTest extends AbstractUnitTest {

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("""
            Method loadUserByUsername.
            Given valid username.
            Then returns JwtEntity with correct details.
            """)
    void givenValidUsername_whenLoadUserByUsername_thenReturnsJwtEntityWithCorrectDetails() {
        // given
        String username = "sanchellik";
        User user = new UserBuilder().build();

        when(userService.getByUsername(username)).thenReturn(user);

        // when
        JwtEntity jwtEntity = (JwtEntity) jwtUserDetailsService.loadUserByUsername(username);

        // then
        verify(userService).getByUsername(username);

        assertEquals(user.getId(), jwtEntity.getId());
        assertEquals(user.getUsername(), jwtEntity.getUsername());
        assertEquals(user.getPassword(), jwtEntity.getPassword());

        assertEquals(1, jwtEntity.getAuthorities().size());

        assertEquals(
                "ROLE_USER",
                jwtEntity.getAuthorities().iterator().next().getAuthority()
        );
    }

    @Test
    @DisplayName("""
            Method loadUserByUsername.
            Given non-existent username.
            Then throws IllegalArgumentException.
            """)
    void givenNonExistentUsername_whenLoadUserByUsername_thenThrowsIllegalArgumentException() {
        // given
        String nonExistentUsername = "unknownuser";

        when(userService.getByUsername(nonExistentUsername))
                .thenThrow(new IllegalArgumentException("User not found"));

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jwtUserDetailsService.loadUserByUsername(nonExistentUsername)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userService).getByUsername(nonExistentUsername);
    }
}
