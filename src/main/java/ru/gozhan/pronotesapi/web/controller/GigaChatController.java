package ru.gozhan.pronotesapi.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gozhan.pronotesapi.service.GigaChatService;
import ru.gozhan.pronotesapi.web.dto.GigaChatRequest;
import ru.gozhan.pronotesapi.web.dto.GigaChatResponse;

@RestController
@RequestMapping("pro-notes/api/v1/chat/giga-chat")
@RequiredArgsConstructor
@Slf4j
public class GigaChatController {

    private final GigaChatService gigaChatService;

    @PostMapping
    public ResponseEntity<GigaChatResponse> createChat(
            @Validated @RequestBody final GigaChatRequest request
    ) {
        return ResponseEntity.ok(new GigaChatResponse()); // TODO do it
    }

    @PostMapping("/mock")
    public ResponseEntity<GigaChatResponse> mock(
            @Validated @RequestBody final GigaChatRequest request
    ) {
        log.info("""
                Request /pro-notes/api/v1/chat/giga-chat/mock, \
                params: GigaChatRequest = {}""",
                request
        );

        GigaChatResponse response = gigaChatService.mock(request);
        return ResponseEntity.ok(response);
    }

}
