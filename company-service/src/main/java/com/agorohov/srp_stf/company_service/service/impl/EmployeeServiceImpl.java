package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.repository.EmployeeRepository;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

//    @Override
//    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
//        List<Long> userIds = employeeRepository.findEmployeeIdsByCompanyId(companyId);
//        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);
//
//        Page<UserDto> result = new PageImpl<>(userDtos, pageable, userDtos.size());
//
//        log.info("Returned list employees of company with id {}, size = {}",
//                companyId, result.getContent().size());
//        return result;
//    }
}
