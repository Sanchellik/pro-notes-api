package ru.gozhan.pronotesapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gozhan.pronotesapi.service.GigaChatService;
import ru.gozhan.pronotesapi.web.dto.GigaChatRequest;
import ru.gozhan.pronotesapi.web.dto.GigaChatResponse;

@Service
@Slf4j
public class GigaChatServiceImpl implements GigaChatService {

    @Override
    public GigaChatResponse sendRequestAndGetResponse(final GigaChatRequest request) {
        // TODO do it
        return null;
    }

    @Override
    public GigaChatResponse mock(final GigaChatRequest request) {
        GigaChatResponse response = new GigaChatResponse();
        response.setAnswerMessage("Вот несколько вариантов названий для космической станции:" +
                "\n\n1. «Астрономия».\n\n2. «Космос».\n\n3. «Звёздный путь»." +
                "\n\n4. «Созвездие».\n\n5. «Мир».");
        return response;
    }

}
