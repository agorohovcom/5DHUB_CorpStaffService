package com.agorohov.srp_stf.user_service.service.impl;

import com.agorohov.srp_stf.user_service.dto.UserDto;
import com.agorohov.srp_stf.user_service.entity.UserEntity;
import com.agorohov.srp_stf.user_service.exception.PageNotFoundException;
import com.agorohov.srp_stf.user_service.exception.UserNotFoundException;
import com.agorohov.srp_stf.user_service.repository.UserRepository;
import com.agorohov.srp_stf.user_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDto> getByLastName(String lastName, Pageable pageable) {
        // Приводим полученный lastName к формату с большой буквы
        String formattedLastName = Character.toUpperCase(lastName.charAt(0))
                + lastName.substring(1).toLowerCase();

        // Получаем страницу с юзерами
        Page<UserEntity> employeePage = userRepository.findByLastName(formattedLastName, pageable);

        // Проверяем, есть ли юзеры с такой фамилией
        if (employeePage.getTotalElements() == 0) {
            String msg = "There aren't any users with lastname " + formattedLastName;
            log.error(msg);
            throw new UserNotFoundException(msg);
        }

        // Проверяем, существует ли запрашиваемая страница
        if (pageable.getPageNumber() > employeePage.getTotalPages() - 1) {
            String msg = "Page " + pageable.getPageNumber()
                    + " doesn't exists, total pages: " + employeePage.getTotalPages();
            log.error(msg);
            throw new PageNotFoundException(msg);
        }

        // Преобразуем сущности в ДТО
        List<UserDto> employeeDtos = employeePage.getContent().stream()
                // TODO вынести в маппер
                .map(e -> new UserDto(
                        e.getId(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPhoneNumber()
                )).toList();

        PageImpl<UserDto> result = new PageImpl<>(
                employeeDtos, pageable, employeePage.getTotalElements());
        log.info("Page with users with lastname {} returned: {}", formattedLastName, result);
        return result;
    }

    @Override
    public List<UserDto> getUsersByIds(List<Long> ids) {
        List<UserEntity> userEntities = userRepository.findAllById(ids);
        // TODO вынести в маппер
        return userEntities.stream()
                .map(e -> new UserDto(
                        e.getId(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPhoneNumber()
                )).toList();
    }
}
