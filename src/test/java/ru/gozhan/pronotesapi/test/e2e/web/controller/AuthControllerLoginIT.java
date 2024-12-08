package ru.gozhan.pronotesapi.test.e2e.web.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.data.builder.JwtRequestBuilder;
import ru.gozhan.pronotesapi.test.e2e.AbstractE2ETest;
import ru.gozhan.pronotesapi.web.dto.JwtRequest;

@Slf4j
public class AuthControllerLoginIT extends AbstractE2ETest {

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
            API Endpoint /login.
            Given correct JwtRequest.
            Then 200 OK, success login.
            """)
        void givenJwtRequestWhenLoginThenSuccessLogin() {
            // given
            JwtRequest jwtRequest = new JwtRequestBuilder().build();

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(jwtRequest)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.LOGIN.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", Matchers.equalTo(Math.toIntExact(1L)))
                    .body("username", Matchers.equalTo(jwtRequest.getUsername()))
                    .body("accessToken", Matchers.notNullValue())
                    .body("refreshToken", Matchers.notNullValue());
        }

    }

    @Nested
    class ErrorScenarios {

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
                API Endpoint /login.
                Given wrong password.
                Then 400 Bad Request.
                """)
        void givenWrongPasswordWhenLoginThenError() {
            // given
            JwtRequest jwtRequest = new JwtRequestBuilder()
                    .password("wrong")
                    .build();

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(jwtRequest)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.LOGIN.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", Matchers.equalTo("Authentication failed."))
                    .body("errors", Matchers.nullValue());
        }

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
                API Endpoint /login.
                Given not existing username.
                Then 400 Bad Request.
                """)
        void givenNotExistingUsernameWhenLoginThenError() {
            // given
            JwtRequest jwtRequest = new JwtRequestBuilder()
                    .username("doesn't exist")
                    .build();

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(jwtRequest)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.LOGIN.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", Matchers.equalTo("Authentication failed."))
                    .body("errors", Matchers.nullValue());
        }

    }

}
