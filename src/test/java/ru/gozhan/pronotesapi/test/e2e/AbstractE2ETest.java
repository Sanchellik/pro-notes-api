package ru.gozhan.pronotesapi.test.e2e;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.gozhan.pronotesapi.test.e2e.config.TestcontainersConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({
        TestcontainersConfig.class,
})
@ActiveProfiles("test")
@Tag("e2e")
public abstract class AbstractE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.config = RestAssured.config().logConfig(
                new LogConfig()
                        .enableLoggingOfRequestAndResponseIfValidationFails(
                                LogDetail.ALL
                        )
        );
    }

}
