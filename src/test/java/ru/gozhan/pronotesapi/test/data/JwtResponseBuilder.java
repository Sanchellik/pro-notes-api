package ru.gozhan.pronotesapi.test.data;

import ru.gozhan.pronotesapi.test.constant.TokenConstant;
import ru.gozhan.pronotesapi.web.dto.JwtResponse;

public class JwtResponseBuilder {

    private Long id = 1L;
    private String username = "sanchellik";
    private String accessToken = TokenConstant.FRESH_ACCESS_TOKEN;
    private String refreshToken = TokenConstant.FRESH_REFRESH_TOKEN;

    public JwtResponseBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public JwtResponseBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public JwtResponseBuilder accessToken(final String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public JwtResponseBuilder refreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public JwtResponse build() {
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(id);
        jwtResponse.setUsername(username);
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);
        return jwtResponse;
    }

}
