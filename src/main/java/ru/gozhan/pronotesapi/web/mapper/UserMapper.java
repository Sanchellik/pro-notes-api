package ru.gozhan.pronotesapi.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.web.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {

    @Override
    UserDto toDto(User entity);

    @Override
    List<UserDto> toDto(List<User> entity);

    @Override
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserDto dto);

}
