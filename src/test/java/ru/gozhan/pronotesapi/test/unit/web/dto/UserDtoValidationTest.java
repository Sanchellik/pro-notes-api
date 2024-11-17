package ru.gozhan.pronotesapi.test.unit.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru.gozhan.pronotesapi.test.data.builder.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.provider.InvalidOnCreateUserDtoProvider;
import ru.gozhan.pronotesapi.test.provider.InvalidOnUpdateUserDtoProvider;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.dto.UserDto;
import ru.gozhan.pronotesapi.web.dto.validation.OnCreate;
import ru.gozhan.pronotesapi.web.dto.validation.OnUpdate;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDtoValidationTest extends AbstractUnitTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        try (ValidatorFactory factory =
                     Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("UserDto validation on create passed")
    void givenValidUserDtoWhenValidateOnCreateThenPassValidation() {
        // given
        UserDto userDtoWithId = new UserDtoBuilder()
                .build();

        UserDto userDtoWithoutId = new UserDtoBuilder()
                .id(null)
                .build();

        // when
        Set<ConstraintViolation<UserDto>> violationsWithId =
                validator.validate(userDtoWithId, OnCreate.class);

        Set<ConstraintViolation<UserDto>> violationsWithoutId =
                validator.validate(userDtoWithoutId, OnCreate.class);

        // then
        Assertions.assertTrue(violationsWithId.isEmpty());
        Assertions.assertTrue(violationsWithoutId.isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidOnCreateUserDtoProvider.class)
    @DisplayName("UserDto validation on create failed")
    void givenInvalidUserDtoWhenValidateOnCreateThenFailValidation(
            final UserDto userDto,
            final String fieldViolationName,
            final String expectedErrorMessage,
            final boolean includeNulls
    ) {
        // given

        // when
        Set<ConstraintViolation<UserDto>> constraintViolations =
                validator.validate(userDto, OnCreate.class);

        // then
        Assertions.assertFalse(
                constraintViolations.isEmpty(),
                "Expected fail validation"
        );

        Set<String> errorMessagesForField = constraintViolations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldViolationName))
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        Assertions.assertTrue(
                errorMessagesForField.contains(expectedErrorMessage),
                String.format(
                        "Expected error message '%s' for field '%s' not found. "
                                + "Actual error messages: %s",
                        expectedErrorMessage, fieldViolationName, errorMessagesForField
                )
        );
    }

    @Test
    @DisplayName("UserDto validation on update passed")
    void givenValidUserDtoWhenValidateOnUpdateThenPassValidation() {
        // given
        UserDto userDto = new UserDtoBuilder()
                .build();

        UserDto userDtoNullPasswordConfirmation = new UserDtoBuilder()
                .passwordConfirmation(null)
                .build();

        // when
        Set<ConstraintViolation<UserDto>> violations =
                validator.validate(userDto, OnUpdate.class);

        Set<ConstraintViolation<UserDto>> violationsNullPasswordConfirmation =
                validator.validate(userDtoNullPasswordConfirmation, OnUpdate.class);

        // then
        Assertions.assertTrue(violations.isEmpty());
        Assertions.assertTrue(violationsNullPasswordConfirmation.isEmpty());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidOnUpdateUserDtoProvider.class)
    @DisplayName("UserDto validation on update failed")
    void givenInvalidUserDtoWhenValidateOnUpdateThenFailValidation(
            final UserDto userDto,
            final String fieldViolationName,
            final String expectedErrorMessage,
            final boolean includeNulls
    ) {
        // given

        // when
        Set<ConstraintViolation<UserDto>> constraintViolations =
                validator.validate(userDto, OnUpdate.class);

        // then
        Assertions.assertFalse(
                constraintViolations.isEmpty(),
                "Expected fail validation"
        );

        Set<String> errorMessagesForField = constraintViolations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldViolationName))
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        Assertions.assertTrue(
                errorMessagesForField.contains(expectedErrorMessage),
                String.format(
                        "Expected error message '%s' for field '%s' not found. "
                                + "Actual error messages: %s",
                        expectedErrorMessage, fieldViolationName, errorMessagesForField
                )
        );
    }

}
