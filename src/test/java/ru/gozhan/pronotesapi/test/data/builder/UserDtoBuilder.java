package ru.gozhan.pronotesapi.test.data.builder;

import ru.gozhan.pronotesapi.web.dto.UserDto;

public class UserDtoBuilder {

    private Long id = 1L;
    private String name = "Alexandr";
    private String username = "sanchellik";
    private String password = "123";
    private String passwordConfirmation = "123";

    public UserDtoBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public UserDtoBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public UserDtoBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public UserDtoBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public UserDtoBuilder passwordConfirmation(
            final String passwordConfirmation
    ) {
        this.passwordConfirmation = passwordConfirmation;
        return this;
    }

    public UserDto build() {
        UserDto userDto = new UserDto();
        userDto.setId(this.id);
        userDto.setName(this.name);
        userDto.setUsername(this.username);
        userDto.setPassword(this.password);
        userDto.setPasswordConfirmation(this.passwordConfirmation);
        return userDto;
    }

}
