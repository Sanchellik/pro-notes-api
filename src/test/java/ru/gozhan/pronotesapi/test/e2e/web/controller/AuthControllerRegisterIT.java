package ru.gozhan.pronotesapi.test.e2e.web.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.data.builder.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.e2e.AbstractE2ETest;
import ru.gozhan.pronotesapi.test.provider.InvalidOnCreateUserDtoProvider;
import ru.gozhan.pronotesapi.test.util.JsonUtil;
import ru.gozhan.pronotesapi.web.dto.UserDto;

public class AuthControllerRegisterIT extends AbstractE2ETest {

    @Nested
    class SuccessScenarios {

        @Test
        @Sql(
                scripts = "/sql/cleanup/truncate_all_tables.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
        @DisplayName("""
            API Endpoint /register.
            Given correct UserDto.
            Then return 200 OK.
            """)
        void givenCorrectUserDtoWhenRegisterThenSuccessRegistration() {
            // given
            UserDto userDto = new UserDtoBuilder().build();

            var requestSpecification = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(JsonUtil.toJsonWithoutNulls(userDto))
                    .log().all();

            // when
            var response = requestSpecification
                    .when()
                    .post(ApiEndpoint.REGISTER.getPath());

            UserDto expectedUserDto = new UserDtoBuilder()
                    .password(null)
                    .passwordConfirmation(null)
                    .build();

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", Matchers.equalTo(Math.toIntExact(expectedUserDto.getId())))
                    .body("name", Matchers.equalTo("Alexandr"))
                    .body("username", Matchers.equalTo("sanchellik"));
        }

    }

    @Nested
    class ErrorScenarios {

        @Test
        @DisplayName("""
            API Endpoint /register.
            Given no http body.
            Then return 400 BAD REQUEST.
            """)
        void givenNoBodyWhenRegisterThenRequestBodyMissingError() {
            // given
            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.REGISTER.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", Matchers.equalTo("Request body is missing."))
                    .body("errors", Matchers.is(Matchers.nullValue()));
        }

        @ParameterizedTest
        @ArgumentsSource(InvalidOnCreateUserDtoProvider.class)
        @DisplayName("""
            API Endpoint /register.
            Given invalid fields.
            Then return 400 BAD REQUEST.
            """)
        void givenInvalidFieldsWhenRegisterThenRequestValidationError(
                final UserDto invalidUserDto,
                final String fieldName,
                final String expectedError,
                final boolean includeNulls
        ) {
            // given
            String invalidBody = includeNulls
                    ? JsonUtil.toJsonWithNulls(invalidUserDto)
                    : JsonUtil.toJsonWithoutNulls(invalidUserDto);

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(invalidBody)
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.REGISTER.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", Matchers.equalTo("Validation failed."))
                    .body("errors", Matchers.notNullValue())
                    .body("errors." + fieldName, Matchers.equalTo(expectedError));
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
            API Endpoint /register.
            Given existing username.
            Then return 400 BAD REQUEST.
            """)
        void givenExistingUsernameWhenRegisterThenIllegalStateError() {
            // given
            UserDto userDto = new UserDtoBuilder().build();

            var request = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(JsonUtil.toJsonWithNulls(userDto))
                    .log().all();

            // when
            var response = request
                    .when()
                    .post(ApiEndpoint.REGISTER.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("message", Matchers.equalTo("User already exists."));
        }

        @Test
        @DisplayName("""
            API Endpoint /register.
            Given not equals passwords.
            Then return 400 BAD REQUEST.
            """)
        void givenNotEqualsPasswordsWhenRegisterThenIllegalStateError() {
            // given
            UserDto userDto = new UserDtoBuilder()
                    .passwordConfirmation("different")
                    .build();

            var requestSpecification = RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(JsonUtil.toJsonWithNulls(userDto))
                    .log().all();

            // when
            var response = requestSpecification
                    .when()
                    .post(ApiEndpoint.REGISTER.getPath());

            // then
            response
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(
                            "message",
                            Matchers.equalTo("Password and password confirmation do not match.")
                    );
        }

    }

}
