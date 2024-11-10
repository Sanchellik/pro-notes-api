package ru.gozhan.pronotesapi.service;

import ru.gozhan.pronotesapi.domain.note.Note;

import java.util.List;

public interface NoteService {

    List<Note> getAllByUserIdWithoutContent(Long userId);

    Note getById(Long id);

    Note save(Note note);

    Note update(Note note);

    void delete(Long id);

}
