package com.agorohov.cs_service.staff_service.service;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;
import com.agorohov.cs_service.staff_service.entity.EmployeeEntity;
import com.agorohov.cs_service.staff_service.exception.EmployeeNotFoundException;
import com.agorohov.cs_service.staff_service.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeDto findByLastName(String lastName) {
        EmployeeEntity employeeEntity = employeeRepository.findByLastName(lastName)
                .orElseThrow(() -> new EmployeeNotFoundException("There isn't employee with lastname " + lastName));

        // TODO маппер
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employeeEntity.getId());
        employeeDto.setFirstName(employeeEntity.getFirstName());
        employeeDto.setLastName(employeeEntity.getLastName());
        employeeDto.setPhoneNumber(employeeEntity.getPhoneNumber());

        return employeeDto;
    }
}
