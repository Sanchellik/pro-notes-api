package ru.gozhan.pronotesapi.test.config.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContextFactory.class)
public @interface WithMockJwtEntity {
    long userId() default 1L;
    String username() default "sanchellik";
    String name() default "Alexandr";
    String password() default "123";
    String[] roles() default {"ROLE_USER"};
}
