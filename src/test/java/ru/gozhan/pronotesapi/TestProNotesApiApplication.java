package ru.gozhan.pronotesapi;

import org.springframework.boot.SpringApplication;

public class TestProNotesApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(ProNotesApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
