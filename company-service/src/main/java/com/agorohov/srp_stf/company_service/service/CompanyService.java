package com.agorohov.srp_stf.company_service.service;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyDto getById(long id);

    CompanyDto getByName(String name);

    Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable);

    Page<UserDto> getUsersByCompanyName(String companyName, Pageable pageable);
}
