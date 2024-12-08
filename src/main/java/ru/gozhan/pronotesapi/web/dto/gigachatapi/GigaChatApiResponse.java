package ru.gozhan.pronotesapi.web.dto.gigachatapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GigaChatApiResponse {

    private List<Choice> choices;

    @Getter
    @Setter
    @ToString
    public static class Choice {
        private Message message;
    }

    @Getter
    @Setter
    @ToString
    public static class Message {
        private String content;
    }

}
