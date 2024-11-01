package ru.gozhan.pronotesapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gozhan.pronotesapi.domain.user.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    User save(User user);

    void deleteById(Long id);

}
