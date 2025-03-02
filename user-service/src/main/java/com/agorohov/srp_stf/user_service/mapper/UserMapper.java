package com.agorohov.srp_stf.user_service.mapper;

import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;
import com.agorohov.srp_stf.user_service.exception.MapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMapper {

    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);

    public static UserDto mapUserEntityToUserDto(UserEntity entity) {
        try {
            return new UserDto(
                    entity.getId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getPhoneNumber());
        } catch (Exception ex) {
            log.error("Failed mapping UserEntity to UserDto: {}", ex.getLocalizedMessage());
            throw new MapperException(ex);
        }
    }

    private UserMapper() {
    }
}
