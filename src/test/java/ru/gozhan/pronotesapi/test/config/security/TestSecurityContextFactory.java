package ru.gozhan.pronotesapi.test.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import ru.gozhan.pronotesapi.web.secutiry.JwtEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TestSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtEntity> {

    @Override
    public SecurityContext createSecurityContext(final WithMockJwtEntity annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        var authorities = Arrays.stream(annotation.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtEntity principal = new JwtEntity(
                annotation.userId(),
                annotation.username(),
                annotation.name(),
                annotation.password(),
                authorities
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        Collections.emptyList()
                );

        context.setAuthentication(authentication);
        return context;
    }

}
