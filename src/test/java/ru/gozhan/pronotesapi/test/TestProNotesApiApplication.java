package ru.gozhan.pronotesapi.test;

import org.springframework.boot.SpringApplication;
import ru.gozhan.pronotesapi.ProNotesApiApplication;
import ru.gozhan.pronotesapi.test.it.config.TestcontainersConfiguration;

public class TestProNotesApiApplication {

    public static void main(final String[] args) {
        SpringApplication.from(ProNotesApiApplication::main)
                .with(TestcontainersConfiguration.class).run(args);
    }

}
