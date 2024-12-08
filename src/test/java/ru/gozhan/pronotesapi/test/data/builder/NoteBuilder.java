package ru.gozhan.pronotesapi.test.data.builder;

import ru.gozhan.pronotesapi.domain.note.Note;

public class NoteBuilder {

    private Long id = 1L;
    private Long userId = 1L;
    private String title = "Title of my note";
    private String content = "Content of my note";

    public NoteBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    public NoteBuilder userId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public NoteBuilder title(final String title) {
        this.title = title;
        return this;
    }

    public NoteBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public Note build() {
        Note note = new Note();
        note.setId(id);
        note.setUserId(userId);
        note.setTitle(title);
        note.setContent(content);
        return note;
    }

}
