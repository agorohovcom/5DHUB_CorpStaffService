package com.agorohov.cs_service.staff_service.service;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;
import com.agorohov.cs_service.staff_service.entity.EmployeeEntity;
import com.agorohov.cs_service.staff_service.exception.EmployeeNotFoundException;
import com.agorohov.cs_service.staff_service.exception.PageNotFoundException;
import com.agorohov.cs_service.staff_service.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Page<EmployeeDto> getByLastName(String lastName, Pageable pageable) {
        // Приводим полученный lastName к формату с большой буквы
        String formattedLastName = Character.toUpperCase(lastName.charAt(0))
                + lastName.substring(1).toLowerCase();

        // Получаем страницу с сотрудниками
        Page<EmployeeEntity> employeePage = employeeRepository.findByLastName(formattedLastName, pageable);

        // Проверяем, есть ли сотрудники с такой фамилией
        if (employeePage.getTotalElements() == 0) {
            String msg = "There aren't any employee with lastname " + formattedLastName;
            log.error(msg);
            throw new EmployeeNotFoundException(msg);
        }

        // Проверяем, существует ли запрашиваемая страница
        if (pageable.getPageNumber() > employeePage.getTotalPages() - 1) {
            String msg = "Page " + pageable.getPageNumber()
                    + " doesn't exists, total pages: " + employeePage.getTotalPages();
            log.error(msg);
            throw new PageNotFoundException(msg);
        }

        // Преобразуем сущности в ДТО
        List<EmployeeDto> employeeDtos = employeePage.getContent().stream()
                .map(e -> new EmployeeDto(
                        e.getId(),
                        e.getFirstName(),
                        e.getLastName(),
                        e.getPhoneNumber()
                )).toList();

        PageImpl<EmployeeDto> result = new PageImpl<>(
                employeeDtos, pageable, employeePage.getTotalElements());
        log.info("Page with employees with lastname {} returned: {}", formattedLastName, result);
        return result;
    }
}
