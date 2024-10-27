package ru.gozhan.pronotesapi.web.secutiry;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.service.UserService;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = userService.getByUsername(username);
        return JwtEntityFactory.create(user);
    }

}
