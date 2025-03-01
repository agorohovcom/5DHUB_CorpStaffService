package com.agorohov.srp_stf.company_service.controller;

import com.agorohov.srp_stf.company_service.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/by-id/{id}")
    public String getById(@PathVariable(value = "id") long id) {
        return "Вызван getById с id=" + id;
    }

    @GetMapping("/by-name/{name}")
    public String getByName(@PathVariable(value = "name") String name) {
        return "Вызван getByName с name=" + name;
    }
}
