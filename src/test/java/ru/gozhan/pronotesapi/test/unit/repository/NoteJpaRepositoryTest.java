package ru.gozhan.pronotesapi.test.unit.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.repository.NoteJpaRepository;
import ru.gozhan.pronotesapi.test.data.builder.NoteBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NoteJpaRepositoryTest extends AbstractUnitTest {

    @Autowired
    private NoteJpaRepository noteJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("Tests for save(..) method.")
    class SaveTest {

        @Test
        @DisplayName("""
                Method save.
                Given correct Note.
                Then save it in database.
                """)
        void givenNote_whenSave_thenFoundInDatabase() {
            // given
            insertUserAndNotes();

            Note note = new NoteBuilder()
                    .id(null)
                    .build();
            noteJpaRepository.save(note);

            entityManager.flush();

            // when
            Note foundNote = findNoteById(1L);

            // then
            assertNotNull(foundNote);
            assertAll(
                    () -> assertEquals(1L, foundNote.getId()),
                    () -> assertEquals(1L, foundNote.getUserId()),
                    () -> assertEquals("Title of my note", foundNote.getTitle()),
                    () -> assertEquals("Content of my note", foundNote.getContent())
            );
        }

    }

    // TODO finish it

    private Note findNoteById(final Long id) {
        TypedQuery<Note> query = entityManager.createQuery(
                "SELECT n FROM Note n WHERE n.id = :id",
                Note.class
        );
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    private void insertUserAndNotes() {
        entityManager.createNativeQuery("""
                        INSERT INTO users (id, name, username, password)
                        VALUES (?, ?, ?, ?)
                        """)
                .setParameter(1, 1L)
                .setParameter(2, "Alexandr")
                .setParameter(3, "sanchellik")
                .setParameter(4, "123")
                .executeUpdate();

        entityManager.createNativeQuery("""
                        INSERT INTO notes (id, user_id, title, content)
                        VALUES (?, ?, ?, ?)
                        """)
                .setParameter(1, 999L)
                .setParameter(2, 1L)
                .setParameter(3, "Title")
                .setParameter(4, "Content")
                .executeUpdate();

        entityManager.flush();
    }

}
