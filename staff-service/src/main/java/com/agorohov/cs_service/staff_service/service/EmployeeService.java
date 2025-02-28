package com.agorohov.cs_service.staff_service.service;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto findByLastName(String lastName);
}
