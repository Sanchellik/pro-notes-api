package ru.gozhan.pronotesapi.web.dto.gigachatapi;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class GigaChatApiRequest {

    private String model;

    private Boolean stream;

    private Integer updateInterval;

    private List<Message> messages;

    @Getter
    @Builder
    @ToString
    public static class Message {

        private String role;

        private String content;

    }

}
