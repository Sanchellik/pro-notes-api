package ru.gozhan.pronotesapi.test.it.web.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.constant.TokenConstant;
import ru.gozhan.pronotesapi.test.it.AbstractIntegrationTest;
import ru.gozhan.pronotesapi.web.dto.JwtRefreshRequest;

public class AuthControllerRefreshIT extends AbstractIntegrationTest {

    @Nested
    class SuccessScenarios {

        @Test
        @Sql(
                scripts = {
                        "/sql/setup/insert_users.sql",
                        "/sql/setup/insert_users_roles.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        )
        @Sql(
                scripts = "/sql/cleanup/truncate_all_tables.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
        @DisplayName("""
            API Endpoint /refresh.
            Given valid refreshToken.
            Then return 200 OK.
            """)
        void givenValidRefreshTokenWhenRefreshThenSuccessRefresh() {
            // given
            JwtRefreshRequest jwtRefreshRequest = new JwtRefreshRequest();
            jwtRefreshRequest.setRefreshToken(TokenConstant.FRESH_REFRESH_TOKEN);

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(jwtRefreshRequest)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.REFRESH.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", Matchers.equalTo(Math.toIntExact(1L)))
                    .body("username", Matchers.equalTo("sanchellik"))
                    .body("accessToken", Matchers.notNullValue())
                    .body("refreshToken", Matchers.notNullValue());
        }

    }

    @Nested
    class FailureScenarios {

        @Test
        @Sql(
                scripts = {
                        "/sql/setup/insert_users.sql",
                        "/sql/setup/insert_users_roles.sql"
                },
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        )
        @Sql(
                scripts = "/sql/cleanup/truncate_all_tables.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
        @DisplayName("""
            API Endpoint /refresh.
            Given expired refreshToken.
            Then return 401 UNAUTHORIZED.
            """)
        void givenValidRefreshTokenWhenRefreshThenUnauthorized() {
            // given
            JwtRefreshRequest jwtRefreshRequest = new JwtRefreshRequest();
            jwtRefreshRequest.setRefreshToken(TokenConstant.EXPIRED_REFRESH_TOKEN);

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(jwtRefreshRequest)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.REFRESH.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("message", Matchers.equalTo("Token expired. Please log in again."))
                    .body("errors", Matchers.nullValue());
        }

    }

}
