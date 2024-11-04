package ru.gozhan.pronotesapi.test.unit.web.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.gozhan.pronotesapi.domain.user.User;
import ru.gozhan.pronotesapi.test.data.UserBuilder;
import ru.gozhan.pronotesapi.test.data.UserDtoBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.web.dto.UserDto;
import ru.gozhan.pronotesapi.web.mapper.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest extends AbstractUnitTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("""
            Method toDto.
            Given UserEntity.
            Then maps to UserDTO.
            """)
    void givenUserEntity_whenToDto_thenReturnDto() {
        // given
        User user = new UserBuilder().build();

        // when
        UserDto userDto = userMapper.toDto(user);

        // then
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getPasswordConfirmation(), userDto.getPasswordConfirmation());
    }

    @Test
    @DisplayName("""
            Method toDto.
            Given List of User entities.
            Then maps to List of UserDTOs.
            """)
    void givenUserEntityList_whenToDto_thenReturnDtoList() {
        // given
        User user0 = new UserBuilder()
                .id(1L)
                .username("user0")
                .build();

        User user1 = new UserBuilder()
                .id(2L)
                .username("user1")
                .build();

        List<User> users = List.of(user0, user1);

        // when
        List<UserDto> userDtos = userMapper.toDto(users);

        // then
        assertEquals(2, userDtos.size());

        assertEquals(user0.getId(), userDtos.get(0).getId());
        assertEquals(user0.getUsername(), userDtos.get(0).getUsername());

        assertEquals(user1.getId(), userDtos.get(1).getId());
        assertEquals(user1.getUsername(), userDtos.get(1).getUsername());
    }

    @Test
    @DisplayName("""
            Method toEntity.
            Given UserDTO.
            Then maps to User entity with roles ignored.
            """)
    void givenUserDto_whenToEntity_thenReturnEntity() {
        // given
        UserDto userDto = new UserDtoBuilder().build();

        // when
        User user = userMapper.toEntity(userDto);

        // then
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getPasswordConfirmation(), user.getPasswordConfirmation());
        assertNull(user.getRoles(), "Roles should be ignored and not set in User entity");
    }

}
