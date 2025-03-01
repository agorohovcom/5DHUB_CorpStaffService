package com.agorohov.srp_stf.company_service.controller;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<CompanyDto> getById(@RequestParam("id") long id) {
        log.info("Request received: \"/by-id/{}\"", id);
        return ResponseEntity.ok().body(companyService.getById(id));
    }

    @GetMapping("/by-name")
    public ResponseEntity<CompanyDto> getByName(@RequestParam("name") String name) {
        log.info("Request received: \"/by-name/{}\"", name);
        return ResponseEntity.ok().body(companyService.getByName(name));
    }
}
