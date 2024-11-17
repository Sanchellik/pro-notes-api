package ru.gozhan.pronotesapi.test.unit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@ActiveProfiles("test")
public class AbstractUnitTest {
}
