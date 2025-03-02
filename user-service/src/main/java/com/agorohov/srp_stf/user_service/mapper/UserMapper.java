package com.agorohov.srp_stf.user_service.mapper;

import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;

public class UserMapper {

    public static UserDto mapUserEntityToUserDto(UserEntity e) {
        return new UserDto(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getPhoneNumber());
    }

    private UserMapper() {
    }
}
