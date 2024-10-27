package ru.gozhan.pronotesapi.service;

import ru.gozhan.pronotesapi.domain.user.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    void delete(Long id);

}
