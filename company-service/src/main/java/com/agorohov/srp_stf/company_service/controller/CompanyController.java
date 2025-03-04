package com.agorohov.srp_stf.company_service.controller;

import com.agorohov.srp_stf.company_service.dto.CompanyInfo;
import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/by-id")
    public ResponseEntity<CompanyInfo> getInfoById(@RequestParam("id") long id) {
        log.info("Request received: \"/by-id/{}\"", id);
        return ResponseEntity.ok().body(companyService.getById(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<CompanyInfo> getInfoByName(@RequestParam("name") String name) {
        log.info("Request received: \"/by-name/{}\"", name);
        return ResponseEntity.ok().body(companyService.getByName(name));
    }

    @GetMapping("/employees-by-company-id")
    public Page<UserDto> getUsersByCompanyId(
            @RequestParam("company-id") long companyId,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request received: \"/users-by-company-id?company-id={}&size={}&page={}\"",
                companyId, pageable.getPageSize(), pageable.getPageNumber());
        return companyService.getUsersByCompanyId(companyId, pageable);
    }

    @GetMapping("/employees-by-company-name")
    public Page<UserDto> getUsersByCompanyName(
            @RequestParam("company-name") String companyName,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Request received: \"/users-by-company-name?company-name={}&size={}&page={}\"",
                companyName, pageable.getPageSize(), pageable.getPageNumber());
        return companyService.getUsersByCompanyName(companyName, pageable);
    }

//    @PostMapping
//    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CreateCompany company) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(company));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CompanyDto> get(@PathVariable("id") long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(companyService.get(id));
//    }
//
//    @PutMapping
//    public ResponseEntity<CompanyDto> update(@Valid @RequestBody UpdateCompany company) {
//        return ResponseEntity.status(HttpStatus.OK).body(companyService.update(company));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
//        companyService.delete(id);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @GetMapping
//    public Page<CompanyDto> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
//        return companyService.getAll(pageable);
//    }
}
