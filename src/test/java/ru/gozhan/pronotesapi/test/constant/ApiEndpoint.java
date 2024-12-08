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
    ),

    GET_NOTES(
            HttpMethod.GET,
            WebConstant.FULL_API_PATH + "/notes"
    ),
    GET_NOTES_BY_ID(
            HttpMethod.GET,
            WebConstant.FULL_API_PATH + "/notes/%s"
    ),
    POST_NOTE(
            HttpMethod.POST,
            WebConstant.FULL_API_PATH + "/notes"
    ),
    PUT_NOTE_BY_ID(
            HttpMethod.PUT,
            WebConstant.FULL_API_PATH + "/notes/%s"
    ),
    DELETE_NOTE_BY_ID(
            HttpMethod.DELETE,
            WebConstant.FULL_API_PATH + "/notes/%s"
    );

    private final HttpMethod method; // TODO think about links with path variables
    private final String path;

}
