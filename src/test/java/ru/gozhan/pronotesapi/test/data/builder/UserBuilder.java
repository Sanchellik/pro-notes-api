package ru.gozhan.pronotesapi.test.data.builder;

import ru.gozhan.pronotesapi.domain.user.Role;
import ru.gozhan.pronotesapi.domain.user.User;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {

    private Long id = 1L;
    private String name = "Alexandr";
    private String username = "sanchellik";
    private String password = "123";
    private String passwordConfirmation;
    private Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_USER));

    public UserBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public UserBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public UserBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public UserBuilder passwordConfirmation(final String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
        return this;
    }

    public UserBuilder roles(final Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        user.setRoles(roles);
        return user;
    }

}
