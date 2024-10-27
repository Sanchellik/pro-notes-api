package ru.gozhan.pronotesapi.web.mapper;

import org.mapstruct.Mapper;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.web.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
