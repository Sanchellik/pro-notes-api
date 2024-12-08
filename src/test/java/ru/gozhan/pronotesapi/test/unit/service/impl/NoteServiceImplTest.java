package ru.gozhan.pronotesapi.test.unit.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.gozhan.pronotesapi.domain.exception.ResourceNotFoundException;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.repository.NoteJpaRepository;
import ru.gozhan.pronotesapi.service.impl.NoteServiceImpl;
import ru.gozhan.pronotesapi.test.data.builder.NoteBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NoteServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private NoteServiceImpl noteServiceImpl;

    @Mock
    private NoteJpaRepository noteJpaRepository;

    @Nested
    @DisplayName("Tests for getAllByUserIdWithoutContent(..) method.")
    class GetAllByUserIdWithoutContentTests {

        @Test
        @DisplayName("""
                Method getById.
                Given notes in database.
                Then returns Note.
                """)
        void givenNotesInDatabase_whenGetById_thenReturnsNote() {
            // given
            Note note1 = new NoteBuilder()
                    .id(1L)
                    .content(null)
                    .build();
            Note note2 = new NoteBuilder()
                    .id(2L)
                    .content(null)
                    .build();

            when(noteJpaRepository.findAllByUserIdWithoutContent(1L))
                    .thenReturn(List.of(note1, note2));

            // when
            List<Note> result = noteServiceImpl.getAllByUserIdWithoutContent(1L);

            // then
            assertEquals(2, result.size());
            assertEquals(note1, result.getFirst());
            assertEquals(note2, result.getLast());
        }

        @Test
        @DisplayName("""
                Method getById.
                Given Note in database.
                Then returns Note.
                """)
        void givenNoNotesInDatabase_whenGetById_thenReturnsEmptyList() {
            // given
            when(noteJpaRepository.findAllByUserIdWithoutContent(1L))
                    .thenReturn(List.of());

            // when
            List<Note> result = noteServiceImpl.getAllByUserIdWithoutContent(1L);

            // then
            assertEquals(0, result.size());
        }

    }

    @Nested
    @DisplayName("Tests for getById(..) method.")
    class GetByIdTests {

        @Test
        @DisplayName("""
                Method getById.
                Given note by id.
                Then returns note.
                """)
        void givenNote_whenGetById_thenReturnsNote() {
            // given
            Note note = new NoteBuilder()
                    .build();

            when(noteJpaRepository.findById(1L))
                    .thenReturn(Optional.of(note));

            // when
            Note result = noteServiceImpl.getById(1L);

            // then
            assertEquals(note, result);
        }

        @Test
        @DisplayName("""
                Method getById.
                Not given note by id.
                Then throws ResourceNotFoundException.
                """)
        void notGivenNote_whenGetById_thenThrowsResourceNotFoundException() {
            // given
            when(noteJpaRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // when & then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> noteServiceImpl.getById(1L)
            );
            assertEquals("Note not found.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Tests for save(..) method.")
    class SaveTests {

        @Test
        @DisplayName("""
                Method save.
                Given Note.
                Then save note.
                """)
        void givenNote_whenSave_thenSaveNote() {
            // given
            Note note = new NoteBuilder()
                    .id(null)
                    .build();

            Note savedNote = new NoteBuilder().build();

            when(noteJpaRepository.save(note))
                .thenReturn(savedNote);

            // when
            Note result = noteServiceImpl.save(note);

            // then
            assertEquals(savedNote, result);
        }

    }

    @Nested
    @DisplayName("Tests for update(..) method.")
    class UpdateTests {

        @Test
        @DisplayName("""
                Method update.
                Given exising note.
                Then update note.
                """)
        void givenExistingNote_whenUpdate_thenUpdateNote() {
            // given
            Note existingNote = new NoteBuilder()
                    .id(1L)
                    .build();

            Note noteToUpdate = new NoteBuilder()
                    .id(1L)
                    .title("new title")
                    .content("new content")
                    .build();

            when(noteJpaRepository.findById(1L))
                    .thenReturn(Optional.of(existingNote));

            when(noteJpaRepository.save(any(Note.class)))
                    .thenReturn(noteToUpdate);

            // when
            Note result = noteServiceImpl.update(existingNote);

            // then
            assertAll(
                    () -> assertEquals(noteToUpdate.getId(), result.getId()),
                    () -> assertEquals(noteToUpdate.getUserId(), result.getUserId()),
                    () -> assertEquals(noteToUpdate.getTitle(), result.getTitle()),
                    () -> assertEquals(noteToUpdate.getContent(), result.getContent())
            );
        }

        @Test
        @DisplayName("""
                Method update.
                Given not exising note.
                Then throws ResourceNotFoundException.
                """)
        void givenNotExistingNote_whenUpdate_thenThrowsResourceNotFoundException() {
            // given
            Note noteToUpdate = new NoteBuilder()
                    .id(1L)
                    .title("new title")
                    .content("new content")
                    .build();

            when(noteJpaRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // when & then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> noteServiceImpl.update(noteToUpdate)
            );
            assertEquals("Note not found.", exception.getMessage());
        }

    }

    @Nested
    @DisplayName("Tests for delete(..) method.")
    class DeleteTests {

        @Test
        @DisplayName("""
                Method delete.
                Given id of existing note.
                Then delete note.
                """)
        void givenIdOfExistingNote_whenDelete_thenDeleteNote() {
            // given
            Long noteId = 1L;

            when(noteJpaRepository.existsById(noteId))
                    .thenReturn(Boolean.TRUE);

            // when
            noteServiceImpl.delete(noteId);

            // then
            verify(noteJpaRepository).deleteById(noteId);
        }

        @Test
        @DisplayName("""
                Method delete.
                Given id of not existing note.
                Then delete note.
                """)
        void givenIdOfNotExistingNote_whenDelete_thenDeleteNote() {
            // given
            Long noteId = 1L;

            when(noteJpaRepository.existsById(noteId))
                    .thenReturn(Boolean.FALSE);

            // when & then
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> noteServiceImpl.delete(noteId)
            );
            assertEquals("Note not found.", exception.getMessage());
            verify(noteJpaRepository, never()).deleteById(noteId);
        }

    }

}
