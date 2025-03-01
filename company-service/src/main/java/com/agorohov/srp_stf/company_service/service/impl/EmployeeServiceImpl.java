package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.EmployeeDto;
import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public int getNumberOfEmployeesByCompanyId(long companyId) {
        return employeeRepository.getNumberOfEmployeesByCompanyId(companyId);
    }

    @Override
    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
        List<EmployeeDto> employeeDtos = employeeRepository.findByCompanyId(companyId, pageable).stream()
                .map(e -> new EmployeeDto(e.getUserId(), companyId))
                .toList();

        // FIXME реализовать получение юзеров из микросервиса user_service
        Page<UserDto> result = new PageImpl<>(List.of());

        log.info("Returned list employees of company with id {}, size = {}",
                companyId, result.getContent().size());
        return result;
    }
}
