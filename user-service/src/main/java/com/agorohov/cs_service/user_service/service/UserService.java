package com.agorohov.cs_service.user_service.service;

import com.agorohov.cs_service.user_service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> getByLastName(String lastName, Pageable pageable);
}
