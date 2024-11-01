package ru.gozhan.pronotesapi.test.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.gozhan.pronotesapi.test.data.JwtRequestBuilder;

import java.util.stream.Stream;

public class InvalidJwtRequestProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(
            final ExtensionContext extensionContext
    ) {
        return Stream.of(
                Arguments.of(
                        new JwtRequestBuilder()
                                .username(null)
                                .build(),
                        "username",
                        "Username must be not null.",
                        false
                ),
                Arguments.of(
                        new JwtRequestBuilder()
                                .username(null)
                                .build(),
                        "username",
                        "Username must be not null.",
                        true
                ),

                Arguments.of(
                        new JwtRequestBuilder()
                                .password(null)
                                .build(),
                        "password",
                        "Password must be not null.",
                        false
                ),
                Arguments.of(
                        new JwtRequestBuilder()
                                .password(null)
                                .build(),
                        "password",
                        "Password must be not null.",
                        true
                )
        );
    }

}
