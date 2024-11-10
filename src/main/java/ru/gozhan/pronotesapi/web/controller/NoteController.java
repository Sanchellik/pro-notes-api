package ru.gozhan.pronotesapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.service.NoteService;
import ru.gozhan.pronotesapi.web.dto.NoteDto;
import ru.gozhan.pronotesapi.web.mapper.NoteMapper;
import ru.gozhan.pronotesapi.web.secutiry.JwtEntity;

import java.util.List;

@RestController
@RequestMapping("pro-notes/api/v1/notes")
@RequiredArgsConstructor
@Tag(
        name = "Note Controller",
        description = "Note API"
)
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping
    @Operation(summary = "Get all notes of the authenticated user without content")
    public ResponseEntity<List<NoteDto>> getAllNotes(
            final Authentication authentication
    ) {
        Long userId = ((JwtEntity) authentication.getPrincipal()).getId();

        List<Note> notes = noteService.getAllByUserIdWithoutContent(userId);
        List<NoteDto> noteDtos = noteMapper.toDto(notes);

        return ResponseEntity.ok(noteDtos);
    }

    @GetMapping("/{noteId}")
    @Operation(summary = "Get a specific note by noteId with content")
    public ResponseEntity<NoteDto> getNoteById(
            @PathVariable final Long noteId
    ) {
        Note note = noteService.getById(noteId);
        NoteDto noteDto = noteMapper.toDto(note);

        return ResponseEntity.ok(noteDto);
    }

    @PostMapping
    @Operation(summary = "Create a new note for the authenticated user")
    public ResponseEntity<NoteDto> createNote(
            @Validated @RequestBody final NoteDto noteDto,
            final Authentication authentication
    ) {
        Long userId = ((JwtEntity) authentication.getPrincipal()).getId();
        noteDto.setUserId(userId);

        Note note = noteMapper.toEntity(noteDto);
        Note createdNote = noteService.save(note);

        NoteDto createdNoteDto = noteMapper.toDto(createdNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNoteDto);
    }

    @PutMapping("/{noteId}")
    @Operation(summary = "Update a specific note by noteId")
    public ResponseEntity<NoteDto> updateNote(
            @PathVariable final Long noteId,
            @Validated @RequestBody final NoteDto noteDto,
            final Authentication authentication
    ) {
        Long userId = ((JwtEntity) authentication.getPrincipal()).getId();
        noteDto.setId(noteId);
        noteDto.setUserId(userId);

        Note note = noteMapper.toEntity(noteDto);

        Note updatedNote = noteService.update(note);

        NoteDto updatedNoteDto = noteMapper.toDto(updatedNote);
        return ResponseEntity.ok(updatedNoteDto);
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Delete a specific note by noteId")
    public ResponseEntity<Void> deleteNoteById(
            @PathVariable final Long noteId
    ) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }

}
