package com.agorohov.srp_stf.user_service.service.impl;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UpdateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;
import com.agorohov.srp_stf.user_service.exception.UserNotFoundException;
import com.agorohov.srp_stf.user_service.mapper.UserMapper;
import com.agorohov.srp_stf.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl out;

    private static final long ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Travolta";
    private static final String PHONE_NUMBER = "+12223334455";

    private static final long UNKNOWN_ID = 101L;
    private static final String UNKNOWN_FIRST_NAME = "Vitaliy";

    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity(ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        userDto = new UserDto(ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER);
    }

    @Test
    void getByLastName_test_success() {
        Page<UserEntity> page = new PageImpl<>(List.of(userEntity));
        when(userRepository.findByLastNameIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        Page<UserDto> actual = out.getByLastName(FIRST_NAME, PageRequest.of(0, 10));

        assertEquals(1, actual.getContent().size());
        assertEquals(ID, actual.getContent().get(0).getId());
        assertEquals(userDto, actual.getContent().get(0));
    }

    @Test
    void getByLastName_test_notFound() {
        when(userRepository.findByLastNameIgnoreCase(anyString(), any(Pageable.class))).thenReturn(Page.empty());

        assertThrows(UserNotFoundException.class,
                () -> out.getByLastName(UNKNOWN_FIRST_NAME, PageRequest.of(0, 10)));
    }

    @Test
    void getUsersByIds_test_success() {
        when(userRepository.findAllById(List.of(ID))).thenReturn(List.of(userEntity));
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        List<UserDto> actual = out.getUsersByIds(List.of(1L));

        assertEquals(1, actual.size());
        assertEquals(ID, actual.get(0).getId());
        assertEquals(userDto, actual.get(0));
    }

    @Test
    void getUsersByIds_test_emptyList() {
        when(userRepository.findAllById(List.of(ID))).thenReturn(List.of());

        List<UserDto> actual = out.getUsersByIds(List.of(1L));

        assertTrue(actual.isEmpty());
    }

    @Test
    void create_test_success() {
        CreateUser createUser = new CreateUser(FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        when(mapper.mapCreateUserToUserEntity(any(CreateUser.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto actual = out.create(createUser);

        assertEquals(userDto, actual);
    }

    @Test
    void get_test_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        UserDto actual = out.get(1L);

        assertEquals(userDto, actual);
        assertEquals(ID, actual.getId());
    }

    @Test
    void get_test_notFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> out.get(UNKNOWN_ID));
    }

    @Test
    void update_test_success() {
        UpdateUser updateUser = new UpdateUser(ID, UNKNOWN_FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        UserEntity updatedUserEntity = new UserEntity(ID, UNKNOWN_FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        UserDto updatedUserDto = new UserDto(ID, UNKNOWN_FIRST_NAME, LAST_NAME, PHONE_NUMBER);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(mapper.mapUpdateUserToUserEntity(any(UpdateUser.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUserEntity);
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(updatedUserDto);

        UserDto actual = out.update(updateUser);

        assertEquals(updatedUserDto, actual);
    }

    @Test
    void update_test_notFound() {
        UpdateUser updateUser = new UpdateUser(ID, UNKNOWN_FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> out.update(updateUser));
    }

    @Test
    void delete_test_success() {
        out.delete(1L);

        verify(userRepository, times(1)).deleteById(ID);
    }

    @Test
    void getAll_test_success() {
        Page<UserEntity> page = new PageImpl<>(List.of(userEntity));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.mapUserEntityToUserDto(any(UserEntity.class))).thenReturn(userDto);

        Page<UserDto> actual = out.getAll(PageRequest.of(0, 10));

        assertEquals(1, actual.getContent().size());
        assertEquals(ID, actual.getContent().get(0).getId());
        assertEquals(userDto, actual.getContent().get(0));
    }

    @Test
    void existsById_test_true() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertTrue(out.existsById(1L));
    }

    @Test
    void existsById_test_false() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertFalse(out.existsById(1L));
    }
}