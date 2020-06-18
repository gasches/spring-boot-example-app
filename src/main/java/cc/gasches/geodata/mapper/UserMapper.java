package cc.gasches.geodata.mapper;

import org.mapstruct.Mapper;

import cc.gasches.geodata.dto.UserDto;
import cc.gasches.geodata.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);
}
