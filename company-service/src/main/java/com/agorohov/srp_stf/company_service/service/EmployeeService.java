package com.agorohov.srp_stf.company_service.service;

import java.util.List;

public interface EmployeeService {
    int getNumberOfEmployeesByCompanyId(long companyId);

    List<Long> findEmployeeIdsByCompanyId(long companyId);
}
