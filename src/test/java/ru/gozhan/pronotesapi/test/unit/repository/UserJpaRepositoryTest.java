package ru.gozhan.pronotesapi.test.unit.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import ru.gozhan.pronotesapi.domain.user.Role;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.repository.UserJpaRepository;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserJpaRepositoryTest extends AbstractUnitTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("Tests for save(..) method.")
    class SaveTest {

        @Test
        @DisplayName("""
                Method save.
                Given correct User.
                Then save him in database.
                """)
        void givenUser_whenSave_thenFoundInDatabase() {
            // given
            User user = new User();
            user.setName("Alexandr");
            user.setUsername("sanchellik");
            user.setPassword("123");
            user.setRoles(Set.of(Role.ROLE_USER));
            userJpaRepository.save(user);

            entityManager.flush();

            // when
            User foundUser = findUserByUsername("sanchellik");

            // then
            assertNotNull(foundUser);
            assertAll(
                    () -> assertEquals(1L, foundUser.getId()),
                    () -> assertEquals("Alexandr", foundUser.getName()),
                    () -> assertEquals("sanchellik", foundUser.getUsername()),
                    () -> assertEquals("123", foundUser.getPassword()),
                    () -> assertTrue(foundUser.getRoles().contains(Role.ROLE_USER))
            );
        }

        @Test
        @DisplayName("""
                Method save.
                Given null User.
                Then save him in database.
                """)
        void givenNullUser_whenSave_thenException() {
            // given
            User user = null;

            // when & then
            assertThrows(
                    InvalidDataAccessApiUsageException.class,
                    () -> userJpaRepository.save(null)
            );
        }

        @Test
        @DisplayName("""
                Method save.
                Given empty User.
                Then save him in database.
                """)
        void givenEmptyUser_whenSave_thenFoundInDatabase() {
            // given
            User user = new User();

            // when & then
            assertThrows(
                    DataIntegrityViolationException.class,
                    () -> userJpaRepository.save(new User())
            );
        }

        @Test
        @DisplayName("""
            Method save (update).
            Given User in database.
            Then update him in database.
            """)
        void givenExistingUser_whenUpdate_thenUpdateInDatabase() {
            // given
            insertUserAndRoles();

            User savedUser = new User();
            savedUser.setId(1L);
            savedUser.setName("Alexandr Updated");
            savedUser.setUsername("sanchellik");
            savedUser.setPassword("newpassword");
            savedUser.setRoles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN));

            // when
            userJpaRepository.save(savedUser);
            entityManager.flush();

            // then
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.id = :id",
                    User.class
            );
            query.setParameter("id", 1L);
            User recievedUser = query.getSingleResult();

            assertNotNull(recievedUser);
            assertAll(
                    () -> assertEquals("Alexandr Updated", recievedUser.getName()),
                    () -> assertEquals("newpassword", recievedUser.getPassword()),
                    () -> assertTrue(recievedUser.getRoles().contains(Role.ROLE_ADMIN)),
                    () -> assertTrue(recievedUser.getRoles().contains(Role.ROLE_USER))
            );
        }

    }

    @Nested
    @DisplayName("Tests for deleteById(..) method.")
    class DeleteByIdTest {

        @Test
        @DisplayName("Delete user by id")
        void givenIdWhenDeleteByIdThenDeleteInDatabase() {
            // given
            insertUserAndRoles();
            Long id = 1L;

            // when
            userJpaRepository.deleteById(id);

            // then
            assertThrows(NoResultException.class, () -> findUserById(1L));
            assertThrows(NoResultException.class,
                    () -> entityManager.createNativeQuery(
                                    "SELECT * FROM users_roles WHERE user_id = :id"
                            ).setParameter("id", id)
                            .getSingleResult()
            );
        }

        // TODO failure tests (invalid id etc.)

    }

    @Nested
    @DisplayName("Tests for findById(..) method.")
    class FindByIdTest {

        @Test
        @DisplayName("Find by id if user exists")
        void givenIdWhenFindByIdThenPresentOptional() {
            // given
            insertUserAndRoles();
            Long id = 1L;

            // when
            Optional<User> foundUser = userJpaRepository.findById(id);

            // then
            assertTrue(foundUser.isPresent());
            assertAll(
                    () -> assertEquals(id, foundUser.get().getId()),
                    () -> assertEquals("Alexandr", foundUser.get().getName()),
                    () -> assertEquals("sanchellik", foundUser.get().getUsername()),
                    () -> assertTrue(foundUser.get().getRoles().contains(Role.ROLE_USER))
            );
        }

        @Test
        @DisplayName("Find by id if user doesn't exist")
        void givenIdWhenFindByIdThenNotPresentOptional() {
            // given
            Long id = 1L;

            // when
            Optional<User> foundUser = userJpaRepository.findById(id);

            // then
            assertTrue(foundUser.isEmpty());
        }

    }

    @Nested
    @DisplayName("Tests for findByUsername(..) method.")
    class FindByUsernameTest {

        @Test
        @DisplayName("Find by username if user exists")
        void givenIdWhenFindByUsernameThenPresentOptional() {
            // given
            insertUserAndRoles();
            String username = "sanchellik";

            // when
            Optional<User> foundUser = userJpaRepository.findByUsername(username);

            // then
            assertTrue(foundUser.isPresent());
            assertAll(
                    () -> assertEquals(1L, foundUser.get().getId()),
                    () -> assertEquals("Alexandr", foundUser.get().getName()),
                    () -> assertEquals(username, foundUser.get().getUsername()),
                    () -> assertTrue(foundUser.get().getRoles().contains(Role.ROLE_USER))
            );
        }

        @Test
        @DisplayName("Find by id username user doesn't exist")
        void givenIdWhenFindByUsernameThenNotPresentOptional() {
            // given
            String username = "sanchellik";

            // when
            Optional<User> foundUser = userJpaRepository.findByUsername(username);

            // then
            assertTrue(foundUser.isEmpty());
        }

    }

    private User findUserByUsername(final String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username",
                User.class
        );
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    private User findUserById(final Long id) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id = :id",
                User.class
        );
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    private void insertUserAndRoles() {
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
                        INSERT INTO users_roles (user_id, role)
                        VALUES (?, ?)
                        """)
                .setParameter(1, 1L)
                .setParameter(2, "ROLE_USER")
                .executeUpdate();

        entityManager.flush();
    }

}
