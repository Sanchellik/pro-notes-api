package ru.gozhan.pronotesapi.test;

import org.springframework.boot.SpringApplication;
import ru.gozhan.pronotesapi.ProNotesApiApplication;
import ru.gozhan.pronotesapi.test.e2e.config.TestcontainersConfig;

public class TestProNotesApiApplication {

    public static void main(final String[] args) {
        SpringApplication.from(ProNotesApiApplication::main)
                .with(TestcontainersConfig.class).run(args);
    }

}
