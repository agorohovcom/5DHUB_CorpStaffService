package com.agorohov.srp_stf.user_service.service;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserDto> getByLastName(String lastName, Pageable pageable);

    List<UserDto> getUsersByIds(List<Long> ids);

    UserDto create(CreateUser user);

    UserDto get(long id);
}
