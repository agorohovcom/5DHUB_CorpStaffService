package com.agorohov.srp_stf.company_service.controller;

import com.agorohov.srp_stf.company_service.dto.*;
import com.agorohov.srp_stf.company_service.service.CompanyService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/info-by-id/{id}")
    public ResponseEntity<CompanyInfo> getInfoById(@PathVariable("id") long id) {
        log.info("GET /companies/info-by-id/{} called", id);
        return ResponseEntity.ok().body(companyService.getById(id));
    }

    @GetMapping("/info-by-name/{name}")
    public ResponseEntity<CompanyInfo> getInfoByName(@PathVariable("name") String name) {
        log.info("GET /companies/info-by-name/{} called", name);
        return ResponseEntity.ok().body(companyService.getByName(name));
    }

    @GetMapping("/employees-by-company-id")
    public Page<UserDto> getUsersByCompanyId(
            @RequestParam("company-id") long companyId,
            @PageableDefault(size = 10, page = 0) @Parameter(hidden = true) Pageable pageable) {
        log.info("GET /companies/employees-by-company-id called with companyId={}, pageable={}", companyId, pageable);
        return companyService.getUsersByCompanyId(companyId, pageable);
    }

    @GetMapping("/employees-by-company-name")
    public Page<UserDto> getUsersByCompanyName(
            @RequestParam("company-name") String companyName,
            @PageableDefault(size = 10, page = 0) @Parameter(hidden = true) Pageable pageable) {
        log.info("GET /companies/employees-by-company-name called with companyName={}, pageable={}",
                companyName, pageable);
        return companyService.getUsersByCompanyName(companyName, pageable);
    }

    @PostMapping
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CreateCompany company) {
        log.info("POST /companies called with company={}", company);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(company));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> get(@PathVariable("id") long id) {
        log.info("GET /companies/{} called", id);
        return ResponseEntity.status(HttpStatus.OK).body(companyService.get(id));
    }

    @PutMapping
    public ResponseEntity<CompanyDto> update(@Valid @RequestBody UpdateCompany company) {
        log.info("PUT /companies called with company={}", company);
        return ResponseEntity.status(HttpStatus.OK).body(companyService.update(company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("DELETE /companies/{} called", id);
        companyService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public Page<CompanyDto> getAll(
            @PageableDefault(size = 10, page = 0) @Parameter(hidden = true) Pageable pageable) {
        log.info("GET /companies called with pageable={}", pageable);
        return companyService.getAll(pageable);
    }

    @PostMapping("/add-employee")
    public ResponseEntity<EmployeeDto> addEmployee(
            @RequestParam("company-id") long companyId, @RequestParam("employee-id") long employeeId) {
        log.info("POST /companies/add-employee called with companyId={}, employeeId={}", companyId, employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(companyService.addEmployee(companyId, employeeId));
    }

    @DeleteMapping("/delete-employee")
    public ResponseEntity<Void> deleteEmployee(
            @RequestParam("company-id") long companyId, @RequestParam("employee-id") long employeeId) {
        log.info("DELETE /companies/delete-employee called with companyId={}, employeeId={}", companyId, employeeId);
        companyService.deleteEmployee(companyId, employeeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
