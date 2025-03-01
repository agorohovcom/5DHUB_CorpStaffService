package com.agorohov.srp_stf.company_service.service;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;

public interface CompanyService {
    CompanyDto getById(long id);

    CompanyDto getByName(String name);
}
