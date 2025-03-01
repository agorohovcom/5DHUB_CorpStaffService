package com.agorohov.cs_service.staff_service.controller;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;
import com.agorohov.cs_service.staff_service.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/by-lastname")
    public Page<EmployeeDto> getByLastName(
            @RequestParam(value = "lastname") String lastName,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return employeeService.getByLastName(lastName, pageable);
    }
}
