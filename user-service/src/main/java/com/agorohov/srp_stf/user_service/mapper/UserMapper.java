package com.agorohov.srp_stf.user_service.mapper;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UpdateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "firstName", source = "entity.firstName")
    @Mapping(target = "lastName", source = "entity.lastName")
    @Mapping(target = "phoneNumber", source = "entity.phoneNumber")
    UserDto mapUserEntityToUserDto(UserEntity entity);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "id", ignore = true)
    UserEntity mapCreateUserToUserEntity(CreateUser user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    UserEntity mapUpdateUserToUserEntity(UpdateUser user);
}
