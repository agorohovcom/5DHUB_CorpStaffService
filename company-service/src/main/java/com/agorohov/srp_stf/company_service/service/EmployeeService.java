package com.agorohov.srp_stf.company_service.service;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    int getNumberOfEmployeesByCompanyId(long companyId);

    List<Long> findEmployeeIdsByCompanyId(long companyId);

    boolean existsById(long id);

    EmployeeDto addEmployee(CompanyDto companyDto, long employeeId);
}
