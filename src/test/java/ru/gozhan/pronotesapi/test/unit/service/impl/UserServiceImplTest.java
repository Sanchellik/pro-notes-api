package ru.gozhan.pronotesapi.test.unit.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gozhan.pronotesapi.domain.exception.ResourceNotFoundException;
import ru.gozhan.pronotesapi.domain.user.Role;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.repository.UserJpaRepository;
import ru.gozhan.pronotesapi.service.impl.UserServiceImpl;
import ru.gozhan.pronotesapi.test.data.UserBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest extends AbstractUnitTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("Tests for getById(..) and getByUsername(..) methods.")
    class GetByIdAndGetByUsernameTest {

        @Test
        @DisplayName("""
                Method getById.
                Given User in database.
                Then returns User.
                """)
        void givenUserInDatabase_whenGetById_thenReturnsUser() {
            // given
            User user = new UserBuilder().build();

            // when
            when(userJpaRepository.findById(1L))
                    .thenReturn(Optional.of(user));
            User result = userServiceImpl.getById(1L);

            // then
            assertEquals(user, result);
        }

        @Test
        @DisplayName("""
                Method getById.
                Not Given User in database.
                Then throws ResourceNotFoundException.
                """)
        void givenNothingInDb_whenGetById_thenThrowResourceNotFoundException() {
            // given
            Long id = 1L;

            // when & then
            when(userJpaRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userServiceImpl.getById(id)
            );
        }


        @Test
        @DisplayName("""
                Method getByUsername.
                Given User in database.
                Then returns User.
                """)
        void givenUserInDatabase_whenGetByUsername_thenReturnsUser() {
            // given
            User user = new UserBuilder().build();

            // when
            when(userJpaRepository.findByUsername(user.getUsername()))
                    .thenReturn(Optional.of(user));
            User result = userServiceImpl.getByUsername(user.getUsername());

            // then
            assertEquals(user, result);
        }

        @Test
        @DisplayName("""
                Method getByUsername.
                Not Given User in database.
                Then throws ResourceNotFoundException.
                """)
        void givenNothingInDb_whenGetByUsername_thenThrowResourceNotFoundException() {
            // given
            String username = "sanchellik";

            // when & then
            when(userJpaRepository.findByUsername(username))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userServiceImpl.getByUsername(username)
            );
        }

    }

    @Nested
    @DisplayName("Tests for update(..) and delete(..) methods.")
    class UpdateAndDeleteByIdTest {

        @Test
        @DisplayName("""
                Method update.
                Given existing User in database and User to update.
                Then update.
                """)
        void givenExistingUser_whenUpdate_thenUpdate() {
            // given
            User existingUser = new UserBuilder().build();
            User user = new UserBuilder().build();

            // when
            when(userJpaRepository.findById(1L))
                    .thenReturn(Optional.of(existingUser));

            User result = userServiceImpl.update(user);

            // then
            assertEquals(user, result);
        }

        @Test
        @DisplayName("""
                Method update.
                Given not existing User in database.
                Then throws ResourceNotFoundException.
                """)
        void givenNotExistingUser_whenUpdate_thenThrowResourceNotFoundException() {
            // given
            User user = new UserBuilder().build();

            // when & then
            when(userJpaRepository.findById(1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> userServiceImpl.update(user)
            );
        }

        @Test
        @DisplayName("""
                Method delete.
                Given id.
                Then delete user.
                """)
        void given_when_then() {
            // given
            Long id = 1L;

            // when
            doNothing().when(userJpaRepository).deleteById(id);
            userServiceImpl.delete(id);

            // then
            verify(userJpaRepository).deleteById(id);
        }

    }

    @Nested
    @DisplayName("Tests for create(..) method.")
    class SaveTest {

        @Test
        @DisplayName("""
                Method create.
                Given no user in database and same passwords.
                Then create user.
                """)
        void givenCorrectUser_whenCreate_thenCreateUserInDatabase() {
            // given
            User userToCreate = new UserBuilder()
                    .password("123")
                    .passwordConfirmation("123")
                    .roles(null)
                    .build();

            String encodedPassword = "encoded123";

            // when
            when(userJpaRepository.findByUsername(userToCreate.getUsername()))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.encode(userToCreate.getPassword()))
                    .thenReturn(encodedPassword);

            doAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                savedUser.setId(1L);
                return savedUser;
            }).when(userJpaRepository).save(any(User.class));

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

            User createdUser = userServiceImpl.create(userToCreate);

            // then
            verify(userJpaRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();

            assertAll(
                    () -> assertEquals(1L, savedUser.getId()),
                    () -> assertEquals("Alexandr", createdUser.getName()),
                    () -> assertEquals("sanchellik", createdUser.getUsername()),
                    () -> assertEquals(encodedPassword, createdUser.getPassword()),
                    () -> assertTrue(createdUser.getRoles().contains(Role.ROLE_USER))
            );
        }

        @Test
        @DisplayName("""
                Method create.
                Given User presented by username.
                Then throws IllegalStateException.
                """)
        void givenNotPresentedUser_whenCreate_thenThrowIllegalStateException() {
            // given
            User userInDb = new UserBuilder()
                    .passwordConfirmation("123")
                    .build();

            User creatingUser = new UserBuilder()
                    .passwordConfirmation("123")
                    .build();

            // when & then
            when(userJpaRepository.findByUsername(creatingUser.getUsername()))
                    .thenReturn(Optional.of(userInDb));

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> userServiceImpl.create(creatingUser)
            );
            assertEquals("User already exists.", exception.getMessage());
        }

        @Test
        @DisplayName("""
                Method create.
                Given not equals passwords.
                Then throws IllegalStateException.
                """)
        void givenNotEqualsPasswords_whenCreate_thenThrowIllegalStateException() {
            // given
            User userInDb = new UserBuilder()
                    .passwordConfirmation("123")
                    .build();

            User creatingUser = new UserBuilder()
                    .password("123")
                    .passwordConfirmation("wrongPassword")
                    .build();

            // when & then
            when(userJpaRepository.findByUsername(userInDb.getUsername()))
                    .thenReturn(Optional.empty());

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> userServiceImpl.create(creatingUser)
            );
            assertEquals(
                    "Password and password confirmation do not match.",
                    exception.getMessage()
            );
        }

    }

}
