package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.EmployeeDto;
import com.agorohov.srp_stf.company_service.entity.EmployeeEntity;
import com.agorohov.srp_stf.company_service.exception.EmployeeAlreadyExistsException;
import com.agorohov.srp_stf.company_service.mapper.CompanyMapper;
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
    private final CompanyMapper mapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.mapper = mapper;
    }

    /**
     * Returns the number of employees by company ID.
     *
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
     *
     * @param companyId company id
     * @return list of employees' ids by company id
     */
    @Override
    public List<Long> findEmployeeIdsByCompanyId(long companyId) {
        return employeeRepository.findEmployeeIdsByCompanyId(companyId);
    }

    /**
     * @param id employee ID
     * @return true if exists
     */
    @Override
    public boolean existsById(long id) {
        return employeeRepository.existsById(id);
    }

    /**
     * Takes the CompanyDto object where the employee and the employee ID will be added.
     * Checks if an employee with such ID already exists, throws EmployeeAlreadyExistsException.
     * Returns the EmployeeDto object.
     *
     * @param companyDto company ID where is adding employee
     * @param employeeId employee ID
     * @return EmployeeDto object
     */
    @Override
    @Transactional
    public EmployeeDto addEmployee(CompanyDto companyDto, long employeeId) {
        // Проверяем нет ли уже такого работника. Если есть, при сохранении мы рискуем перезаписать его связь
        // с другой компанией, поэтому сохраняем только если такого пока нет
        if (this.existsById(employeeId)) {
            String msg = String.format("Employee with id %d already exists", employeeId);
            log.error(msg);
            throw new EmployeeAlreadyExistsException(msg);
        }

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setUserId(employeeId);
        employeeEntity.setCompany(mapper.mapCompanyDtoToCompanyEntityWithoutEmployees(companyDto));

        EmployeeDto result = mapper.mapEmployeeEntityToEmployeeDto(employeeRepository.save(employeeEntity));
        log.info("Added new employee: {}", result);
        return result;
    }

    /**
     * @param employeeId employee ID
     */
    @Override
    public void deleteEmployee(long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}