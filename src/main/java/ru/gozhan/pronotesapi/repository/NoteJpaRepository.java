package ru.gozhan.pronotesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gozhan.pronotesapi.domain.note.Note;

import java.util.List;
import java.util.Optional;

public interface NoteJpaRepository extends JpaRepository<Note, Long> {

    @Query("""
            SELECT new ru.gozhan.pronotesapi.domain.note.Note(
                n.id,
                n.userId,
                n.title,
                null
            )
            FROM Note n
            WHERE n.userId = :userId
            """)
    List<Note> findAllByUserIdWithoutContent(@Param("userId") Long userId);

    Optional<Note> findById(Long id);

    Note save(Note note);

    void deleteById(Long id);

    boolean existsById(Long id);

}
