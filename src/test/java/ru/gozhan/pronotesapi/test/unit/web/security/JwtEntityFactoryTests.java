package ru.gozhan.pronotesapi.test.unit.web.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import ru.gozhan.pronotesapi.domain.user.Role;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.secutiry.JwtEntity;
import ru.gozhan.pronotesapi.web.secutiry.JwtEntityFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class JwtEntityFactoryTests extends AbstractUnitTest {

    @Test
    @DisplayName("""
            Method create().
            Test creating JwtEntity from User.""")
    public void givenUserWhenCreateThenJwtEntity() {
        // given
        User user = new User();
        user.setId(1L);
        user.setName("Alexandr Gozhan");
        user.setUsername("sanchellik");
        user.setPassword("123");
        user.setRoles(Set.of(Role.ROLE_ADMIN));

        // when
        JwtEntity jwtEntity = JwtEntityFactory.create(user);

        // then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(jwtEntity),
                () -> Assertions.assertEquals(user.getId(), jwtEntity.getId()),
                () -> Assertions.assertEquals(
                        user.getUsername(),
                        jwtEntity.getUsername()
                ),
                () -> Assertions.assertEquals(
                        user.getPassword(),
                        jwtEntity.getPassword()
                ),
                () -> Assertions.assertEquals(
                        user.getRoles().stream()
                                .map(Role::name)
                                .collect(Collectors.toSet()),
                        jwtEntity.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toSet()),
                        "GrantedAuthorities don't match"
                )

        );
    }

}
