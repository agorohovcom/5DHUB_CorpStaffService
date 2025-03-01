package com.agorohov.cs_service.staff_service.service;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    Page<EmployeeDto> getByLastName(String lastName, Pageable pageable);
}
