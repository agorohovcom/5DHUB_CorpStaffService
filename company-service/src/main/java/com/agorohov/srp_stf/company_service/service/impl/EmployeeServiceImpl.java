package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Returns the number of employees by company ID.
     * @param companyId company ID
     * @return number of employees by company
     */
    @Override
    @Transactional(readOnly = true)
    public int getNumberOfEmployeesByCompanyId(long companyId) {
        return employeeRepository.getNumberOfEmployeesByCompanyId(companyId);
    }

    /**
     * Returns a list of employee IDs by company ID.
     * @param companyId company id
     * @return list of employees' ids by company id
     */
    @Override
    public List<Long> findEmployeeIdsByCompanyId(long companyId) {
        return employeeRepository.findEmployeeIdsByCompanyId(companyId);
    }
}
