package com.agorohov.srp_stf.company_service.service;

import com.agorohov.srp_stf.company_service.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    int getNumberOfEmployeesByCompanyId(long companyId);

    Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable);
}
