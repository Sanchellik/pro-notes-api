package ru.gozhan.pronotesapi.test.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.gozhan.pronotesapi.test.data.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.util.StringGeneratorUtil;

import java.util.stream.Stream;

public class InvalidOnUpdateUserDtoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(
            final ExtensionContext extensionContext
    ) {
        return Stream.of(
                Arguments.of(
                        new UserDtoBuilder()
                                .id(null)
                                .build(),
                        "id",
                        "Id must be not null.",
                        false
                ),
                Arguments.of(
                        new UserDtoBuilder()
                                .id(null)
                                .build(),
                        "id",
                        "Id must be not null.",
                        true
                ),

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
