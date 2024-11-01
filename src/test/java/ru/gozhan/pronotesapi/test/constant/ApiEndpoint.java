package ru.gozhan.pronotesapi.test.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@AllArgsConstructor
@Getter
public enum ApiEndpoint {

    LOGIN(
            HttpMethod.POST,
            WebConstant.FULL_API_PATH + "/auth/login"
    ),
    REGISTER(
            HttpMethod.POST,
            WebConstant.FULL_API_PATH + "/auth/register"
    ),
    REFRESH(
            HttpMethod.POST,
            WebConstant.FULL_API_PATH + "/auth/refresh"
    ),;

    private final HttpMethod method; // TODO think about links with path variables
    private final String path;

}
