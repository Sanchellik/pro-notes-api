package ru.gozhan.pronotesapi.service;

import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatRequest;
import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatResponse;

public interface GigaChatService {

    GigaChatResponse sendRequestAndGetResponse(GigaChatRequest request);

    GigaChatResponse mock(GigaChatRequest request);

}
