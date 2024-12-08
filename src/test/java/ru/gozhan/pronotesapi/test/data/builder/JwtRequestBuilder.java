package ru.gozhan.pronotesapi.test.data.builder;

import ru.gozhan.pronotesapi.web.dto.JwtRequest;

public class JwtRequestBuilder {

    private String username = "sanchellik";
    private String password = "123";

    public JwtRequestBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public JwtRequestBuilder password(final String password) {
        this.password = password;
        return this;
    }

    public JwtRequest build() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername(this.username);
        jwtRequest.setPassword(this.password);
        return jwtRequest;
    }

}
