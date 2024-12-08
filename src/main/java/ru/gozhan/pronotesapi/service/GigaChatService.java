package ru.gozhan.pronotesapi.service;

import ru.gozhan.pronotesapi.web.dto.GigaChatRequest;
import ru.gozhan.pronotesapi.web.dto.GigaChatResponse;

public interface GigaChatService {

    GigaChatResponse sendRequestAndGetResponse(GigaChatRequest request);

    GigaChatResponse mock(GigaChatRequest request);

}
