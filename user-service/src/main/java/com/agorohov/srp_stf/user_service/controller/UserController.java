package com.agorohov.srp_stf.user_service.controller;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam("lastname") String lastName,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request received: \"/by-lastname?lastname={}&size={}&page={}\"",
                lastName, pageable.getPageSize(), pageable.getPageNumber());
        return userService.getByLastName(lastName, pageable);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getByIds(@RequestParam("ids") List<Long> ids) {
        log.info("Request received: \"/by-ids\" with request param \"ids\": {}", ids);
        return userService.getUsersByIds(ids);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateUser user) {
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping
//    public ResponseEntity<UserDto> get(@RequestParam("id") long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.get(id));
//    }
//
//    @PatchMapping
//    public ResponseEntity<UserDto> update(@Valid @RequestBody UpdateUser user) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Void> delete(@RequestParam("id") long id) {
//        userService.delete(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
}
