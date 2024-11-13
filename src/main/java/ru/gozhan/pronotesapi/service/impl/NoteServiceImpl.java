package ru.gozhan.pronotesapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gozhan.pronotesapi.domain.exception.ResourceNotFoundException;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.repository.NoteJpaRepository;
import ru.gozhan.pronotesapi.service.NoteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteJpaRepository noteJpaRepository;

    @Override
    public List<Note> getAllByUserIdWithoutContent(final Long userId) {
        return noteJpaRepository.findAllByUserIdWithoutContent(userId);
    }

    @Override
    public Note getById(final Long id) {
        return noteJpaRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Note note found.")
                );
    }

    @Override
    @Transactional
    public Note save(final Note note) {
        return noteJpaRepository.save(note);
    }

    @Override
    @Transactional
    public Note update(final Note note) {
        Note existingNote = noteJpaRepository.findById(note.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());

        return noteJpaRepository.save(existingNote);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        if (!noteJpaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found");
        }
        noteJpaRepository.deleteById(id);
    }

}
