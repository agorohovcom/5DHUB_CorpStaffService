package com.agorohov.srp_stf.company_service.controller;

import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/users-by-company-id")
    public Page<UserDto> getUsersByCompanyId(
            @RequestParam(value = "company-id") long companyId,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request received: \"/users-by-company-id?company-id={}&size={}&page={}\"",
                companyId, pageable.getPageSize(), pageable.getPageNumber());
        return employeeService.getUsersByCompanyId(companyId, pageable);
    }
}
