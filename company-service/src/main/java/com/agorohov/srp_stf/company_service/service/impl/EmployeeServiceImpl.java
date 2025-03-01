package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.feign.UserServiceFeignClient;
import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
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
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeRepository employeeRepository;
    private final UserServiceFeignClient userServiceFeignClient;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserServiceFeignClient userServiceFeignClient) {
        this.employeeRepository = employeeRepository;
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Override
    public int getNumberOfEmployeesByCompanyId(long companyId) {
        return employeeRepository.getNumberOfEmployeesByCompanyId(companyId);
    }

    @Override
    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
        List<Long> userIds = employeeRepository.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        Page<UserDto> result = new PageImpl<>(userDtos, pageable, userDtos.size());

        log.info("Returned list employees of company with id {}, size = {}",
                companyId, userDtos.size());
        return result;
    }
}
