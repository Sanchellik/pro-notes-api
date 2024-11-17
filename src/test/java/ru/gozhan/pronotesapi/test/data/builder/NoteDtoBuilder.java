package ru.gozhan.pronotesapi.test.data.builder;

import ru.gozhan.pronotesapi.web.dto.NoteDto;

public class NoteDtoBuilder {

    private Long id = 1L;
    private Long userId = 1L;
    private String title = "Title of my note";
    private String content = "Content of my note";

    public NoteDtoBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public NoteDtoBuilder userId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public NoteDtoBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public NoteDtoBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public NoteDto build() {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(id);
        noteDto.setUserId(userId);
        noteDto.setTitle(title);
        noteDto.setContent(content);
        return noteDto;
    }

}
