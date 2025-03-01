package com.agorohov.cs_service.user_service.controller;

import com.agorohov.cs_service.user_service.dto.UserDto;
import com.agorohov.cs_service.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-lastname")
    public Page<UserDto> getByLastName(
            @RequestParam(value = "lastname") String lastName,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request received: \"/by-lastname?lastname={}&size={}&page={}\"",
                lastName, pageable.getPageSize(), pageable.getPageNumber());
        return userService.getByLastName(lastName, pageable);
    }
}
