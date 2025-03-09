package com.agorohov.srp_stf.user_service.service.impl;

import com.agorohov.srp_stf.user_service.dto.CreateUser;
import com.agorohov.srp_stf.user_service.dto.UpdateUser;
import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;
import com.agorohov.srp_stf.user_service.exception.UserNotFoundException;
import com.agorohov.srp_stf.user_service.mapper.UserMapper;
import com.agorohov.srp_stf.user_service.repository.UserRepository;
import com.agorohov.srp_stf.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    /**
     * Takes a last name to search for users and a Pageable with page size and number,
     * returns a Page with UserDto objects.
     * If there is no user with the requested last name, throws UserNotFoundException.
     * @param lastName user's lastname
     * @param pageable Pageable object with size (default = 10) and page (default = 0)
     * @return Page object with UserDto objects found by lastname
     */
    @Override
    public Page<UserDto> getByLastName(String lastName, Pageable pageable) {
        // Получаем страницу с юзерами, игнорируя case и вызывая trim(), чтобы убрать лишние пробелы
        Page<UserEntity> employeePage = userRepository.findByLastNameIgnoreCase(lastName.trim(), pageable);

        // Проверяем, есть ли юзеры с такой фамилией
        if (employeePage.getTotalElements() == 0) {
            String msg = "There aren't any users with lastname " + lastName;
            log.error(msg);
            throw new UserNotFoundException(msg);
        }

        // Преобразуем сущности в ДТО
        List<UserDto> employeeDtos = employeePage.getContent().stream()
                .map(mapper::mapUserEntityToUserDto)
                .toList();

        PageImpl<UserDto> result = new PageImpl<>(
                employeeDtos, pageable, employeePage.getTotalElements());

        log.info("Page with users with lastname {} returned: {}", lastName, result);
        return result;
    }

    /**
     * Takes user ids and returns a list of UserDto objects.
     * @param ids list of user ids
     * @return UserDto list
     */
    @Override
    public List<UserDto> getUsersByIds(List<Long> ids) {
        List<UserEntity> userEntities = userRepository.findAllById(ids);
        List<UserDto> result = userEntities.stream()
                .map(mapper::mapUserEntityToUserDto)
                .toList();
        log.info("Returned list with {} instances of UserDto", result.size());
        return result;
    }

    /**
     * Takes CreateUser object, saves user id DB and returns UserDto object with new user ID.
     * @param user CreateUser object
     * @return UserDto object
     */
    @Override
    public UserDto create(CreateUser user) {
        UserEntity entity = mapper.mapCreateUserToUserEntity(user);
        // Сохраняем юзера в БД и маппим в UserDto, чтобы вернуть созданного юзера уже с ID
        // (можно обойтись без этого и просто возвращать статус 201 Created вместо UserDto)
        UserDto result = mapper.mapUserEntityToUserDto(userRepository.save(entity));
        log.info("Created user: {}", result);
        return result;
    }

    /**
     * Takes user ID and return found user in UserDto object.
     * If there is no user with the requested ID, throws UserNotFoundException.
     * @param id user ID
     * @return UserDto object
     */
    @Override
    public UserDto get(long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "No user with id " + id;
                    log.error("Fail get user: {}", msg);
                    return new UserNotFoundException(msg);
                });
        UserDto result = mapper.mapUserEntityToUserDto(userEntity);
        log.info("Got user: {}", result);
        return result;
    }

    /**
     * Takes UpdateUser to update the user.
     * If there is no user with the same ID, throws UserNotFoundException.
     * If there is a user with the same ID as in UpdateUser,
     * updates the user in the DB and returns UserDto with the updated data.
     * @param user UpdateUser object
     * @return UserDto object with updated fields
     */
    @Override
    @Transactional
    public UserDto update(UpdateUser user) {
        // Проверяем есть ли юзер с таким ID, чтобы не создавать нового при попытке отредактировать несуществующего
        userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    String msg = "No user with id " + user.getId();
                    log.error("Fail update user: {}", msg);
                    return new UserNotFoundException(msg);
                });
        // Маппим в UserEntity и сохраняем
        UserEntity userEntity = mapper.mapUpdateUserToUserEntity(user);
        userRepository.save(userEntity);
        // Маппим обновленного юзера в UserDto и возвращаем результат
        // (можно было вернуть 200 OK или тот же UpdateUser, но хочется побыть дотошным)
        UserDto result = mapper.mapUserEntityToUserDto(userEntity);
        log.info("Updated user: {}", result);
        return result;
    }

    /**
     * Deletes user with taking ID, returns no data.
     * @param id user ID
     */
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
        log.info("User with id {} deleted", id);
    }

    /**
     * Returns all existing users is Pages with UserDto objects.
     * @param pageable Pageable object with size (default = 10) and page (default = 0)
     * @return Page object with all existing UserDto objects
     */
    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        Page<UserEntity> employeePage = userRepository.findAll(pageable);

        // Преобразуем сущности в ДТО
        List<UserDto> employeeDtos = employeePage.getContent().stream()
                .map(mapper::mapUserEntityToUserDto)
                .toList();

        PageImpl<UserDto> result = new PageImpl<>(
                employeeDtos, pageable, employeePage.getTotalElements());
        log.info("Page with users returned: {}", result);
        return result;
    }

    /**
     * @param id user ID
     * @return exists by ID
     */
    @Override
    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }
}
