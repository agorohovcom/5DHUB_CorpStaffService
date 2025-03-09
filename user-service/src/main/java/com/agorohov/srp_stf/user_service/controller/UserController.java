package com.agorohov.srp_stf.user_service.controller;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UpdateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/by-lastname")
    public Page<UserDto> getByLastName(
            @RequestParam("lastname") String lastName,
            @PageableDefault(size = 10, page = 0) @Parameter(hidden = true) Pageable pageable) {
        log.info("Entering getByLastName method. Lastname: {}, Pageable: {}", lastName, pageable);
        return userService.getByLastName(lastName, pageable);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getByIds(@RequestParam("ids") List<Long> ids) {
        log.info("Entering getByIds method. IDs: {}", ids);
        return userService.getUsersByIds(ids);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUser user) {
        log.info("Entering create method. CreateUser: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable("id") long id) {
        log.info("Entering get method. ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.get(id));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UpdateUser user) {
        log.info("Entering update method. UpdateUser: {}", user);
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("Entering delete method. ID: {}", id);
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public Page<UserDto> getAll(@PageableDefault(size = 10, page = 0) @Parameter(hidden = true) Pageable pageable) {
        log.info("Entering getAll method. Pageable: {}", pageable);
        return userService.getAll(pageable);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable("id") long id) {
        log.info("Entering existsById method. ID: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.existsById(id));
    }
}
