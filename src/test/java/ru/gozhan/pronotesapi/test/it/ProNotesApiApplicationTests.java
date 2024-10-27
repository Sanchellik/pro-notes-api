package ru.gozhan.pronotesapi.test.it;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.gozhan.pronotesapi.test.it.config.TestcontainersConfiguration;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class ProNotesApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
