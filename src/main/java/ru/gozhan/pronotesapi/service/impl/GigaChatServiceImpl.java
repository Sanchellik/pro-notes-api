package ru.gozhan.pronotesapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gozhan.pronotesapi.service.GigaChatService;
import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatApiRequest;
import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatApiResponse;
import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatRequest;
import ru.gozhan.pronotesapi.web.dto.gigachatapi.GigaChatResponse;

import java.util.List;

@Service
@Slf4j
public class GigaChatServiceImpl implements GigaChatService {

    private final WebClient webClient;

    public GigaChatServiceImpl(
            @Qualifier("webClientGigaChat") final WebClient gigaChatWebClient
    ) {
        this.webClient = gigaChatWebClient;
    }

    @Override
    public GigaChatResponse sendRequestAndGetResponse(
            final GigaChatRequest request
    ) {
        GigaChatApiRequest apiRequest = GigaChatApiRequest.builder()
                .model("GigaChat")
                .stream(false)
                .updateInterval(0)
                .messages(List.of(
                        GigaChatApiRequest.Message.builder()
                                .role("system")
                                .content("Отвечай как научный сотрудник")
                                .build(),
                        GigaChatApiRequest.Message.builder()
                                .role("user")
                                .content(request.getQuestionMessage())
                                .build()
                ))
                .build();

        log.info("Отправка запроса в GigaChat API: {}", apiRequest);

        GigaChatApiResponse apiResponse = webClient.post()
                .bodyValue(apiRequest)
                .retrieve()
                .bodyToMono(GigaChatApiResponse.class)
                .block();

        log.info("Ответ от GigaChat API: {}", apiResponse);

        GigaChatResponse response = new GigaChatResponse();
        if (apiResponse != null && !apiResponse.getChoices().isEmpty()) {
            response.setAnswerMessage(apiResponse.getChoices().getFirst().getMessage().getContent());
        }
        return response;
    }

    @Override
    public GigaChatResponse mock(
            final GigaChatRequest request
    ) {
        GigaChatResponse response = new GigaChatResponse();
        response.setAnswerMessage("Вот несколько вариантов названий для космической станции:" +
                "\n\n1. «Астрономия».\n\n2. «Космос».\n\n3. «Звёздный путь»." +
                "\n\n4. «Созвездие».\n\n5. «Мир».");
        return response;
    }

}
