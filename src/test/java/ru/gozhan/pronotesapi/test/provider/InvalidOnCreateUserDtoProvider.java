package ru.gozhan.pronotesapi.test.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.gozhan.pronotesapi.test.data.builder.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.util.StringGeneratorUtil;

import java.util.stream.Stream;

/**
 * Argument provider for parameterized tests that verify validation of {@link UserDto}
 * with the {@code OnCreate} validation group. This provider generates different configurations
 * of {@code UserDto} objects that violate validation rules, along with the expected data for
 * validating error messages.
 *
 * <p>Each argument provides a test data set containing:</p>
 * <ul>
 *     <li>{@code UserDto userDto} - the {@link UserDto} object that violates validation rules.</li>
 *
 *     <li>{@code String fieldName} - the name of the field expected to trigger a validation
 *     error.</li>
 *
 *     <li>{@code String expectedError} - the expected error message for the specified field.</li>
 *
 *     <li>{@code boolean includeNulls} - a flag indicating whether null values should be included
 *     in JSON (e.g., for testing null handling).</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @ParameterizedTest
 * @ArgumentsSource(InvalidOnCreateUserDtoProvider.class)
 * void givenInvalidUserDtoWhenSthThenValidationFails(
 *         final UserDto userDto,
 *         final String fieldName,
 *         final String expectedError,
 *         final boolean includeNulls
 * ) {
 *     // Test UserDto validation logic
 * }
 * }
 * </pre>
 *
 * @see UserDto
 */
public class InvalidOnCreateUserDtoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(
            final ExtensionContext extensionContext
    ) {
        return Stream.of(
                Arguments.of(
                        new UserDtoBuilder()
                                .name(null)
                                .build(),
                        "name",
                        "Name must be not null.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .name(null)
                                .build(),
                        "name",
                        "Name must be not null.",
                        true
                ),

                Arguments.of(
                        new UserDtoBuilder()
                                .username(null)
                                .build(),
                        "username",
                        "Username must be not null.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .username(null)
                                .build(),
                        "username",
                        "Username must be not null.",
                        true
                ),

                Arguments.of(
                        new UserDtoBuilder()
                                .password(null)
                                .build(),
                        "password",
                        "Password must be not null.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .password(null)
                                .build(),
                        "password",
                        "Password must be not null.",
                        true
                ),

                Arguments.of(
                        new UserDtoBuilder()
                                .passwordConfirmation(null)
                                .build(),
                        "passwordConfirmation",
                        "Password confirmation must be not null.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .passwordConfirmation(null)
                                .build(),
                        "passwordConfirmation",
                        "Password confirmation must be not null.",
                        true
                ),

                Arguments.of(
                        new UserDtoBuilder()
                                .name(StringGeneratorUtil.generateString(256))
                                .build(),
                        "name",
                        "Name length must be smaller than 255 symbols.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .username(StringGeneratorUtil.generateString(256))
                                .build(),
                        "username",
                        "Username length must be smaller than 255 symbols.",
                        false
                )
        );
    }

}
