package com.agorohov.cs_service.staff_service.controller;

import com.agorohov.cs_service.staff_service.dto.EmployeeDto;
import com.agorohov.cs_service.staff_service.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/by-last-name/{last_name}")
    public ResponseEntity<EmployeeDto> findByLastName(@PathVariable(value = "last_name") String lastName) {
        return ResponseEntity.ok(employeeService.findByLastName(lastName));
    }
}
