package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.feign.UserServiceFeignClient;
import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
import com.agorohov.srp_stf.company_service.service.CompanyValidator;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeRepository employeeRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final CompanyValidator companyValidator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               UserServiceFeignClient userServiceFeignClient,
                               CompanyValidator companyValidator) {
        this.employeeRepository = employeeRepository;
        this.userServiceFeignClient = userServiceFeignClient;
        this.companyValidator = companyValidator;
    }

    /**
     * Takes a company id and returns the number of employees of this company.
     *
     * @param companyId company id
     * @return number of employees in this company
     */
    @Override
    @Transactional(readOnly = true)
    public int getNumberOfEmployeesByCompanyId(long companyId) {
        return employeeRepository.getNumberOfEmployeesByCompanyId(companyId);
    }

    /**
     * Takes a company id and a Pageable object that contains the size and page parameters,
     * returns a list of employees of this company as a Page object.
     * @param companyId company id
     * @param pageable a Pageable object that contains the size and page parameters
     * @return Page object contains UserDto objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
        // Проверяем существование компании через CompanyValidator
        companyValidator.validateCompanyExists(companyId);

        List<Long> userIds = employeeRepository.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        Page<UserDto> result = new PageImpl<>(userDtos, pageable, userDtos.size());

        log.info("Returned list employees of company with id {}, size = {}",
                companyId, userDtos.size());
        return result;
    }
}
