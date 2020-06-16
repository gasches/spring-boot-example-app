package cc.gasches.testassignment.mapper;

import org.mapstruct.Mapper;

import cc.gasches.testassignment.dto.UserDto;
import cc.gasches.testassignment.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);
}
