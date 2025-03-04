package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.CompanyInfo;
import com.agorohov.srp_stf.company_service.dto.CreateCompany;
import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.CompanyAlreadyExistsException;
import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.feign.UserServiceFeignClient;
import com.agorohov.srp_stf.company_service.mapper.CompanyMapper;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import com.agorohov.srp_stf.company_service.service.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyRepository companyRepository;
    private final EmployeeService employeeService;
    private final UserServiceFeignClient userServiceFeignClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, EmployeeService employeeService, UserServiceFeignClient userServiceFeignClient) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyInfo getById(long id) {
        // получаем компанию по id или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "Company with id " + id + " not found";
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(id);

        // Преобразуем сущность компании в ДТО
        CompanyInfo result = CompanyMapper.mapCompanyEntityToCompanyInfo(companyEntity, numberOfEmployees);

        log.info("Company found by id: {}", result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyInfo getByName(String name) {
        // получаем компанию по имени или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> {
                    String msg = "Company with name " + name + " not found";
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(companyEntity.getId());

        // Преобразуем сущность компании в ДТО и возвращаем результат
        CompanyInfo result = CompanyMapper.mapCompanyEntityToCompanyInfo(companyEntity, numberOfEmployees);
        log.info("Company found by name: {}", result);
        return result;
    }

    /**
     * Takes a company id and a Pageable object that contains the size and page parameters,
     * returns a list of employees of this company as a Page object.
     *
     * @param companyId company id
     * @param pageable  a Pageable object that contains the size and page parameters
     * @return Page object contains UserDto objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByCompanyId(long companyId, Pageable pageable) {
        // Проверяем существование компании
        if (!companyRepository.existsById(companyId)) {
            String msg = "No employees, because company with id " + companyId + " not found";
            log.error(msg);
            throw new CompanyNotFoundException(msg);
        }

        List<Long> userIds = employeeService.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        Page<UserDto> result = new PageImpl<>(userDtos, pageable, userDtos.size());

        log.info("Returned page of employees by company id = {}, number of employees: {}",
                companyId, userDtos.size());
        return result;
    }

    /**
     * Takes a company name and a Pageable object that contains the size and page parameters,
     * returns a list of employees of this company as a Page object.
     *
     * @param companyName company name
     * @param pageable    a Pageable object that contains the size and page parameters
     * @return Page object contains UserDto objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByCompanyName(String companyName, Pageable pageable) {
        // Проверяем существование компании
        long companyId = companyRepository.findByNameIgnoreCase(companyName.trim())
                .orElseThrow(() -> {
                    String msg = "No employees, because company with name " + companyName + " not found";
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                })
                .getId();

        List<Long> userIds = employeeService.findEmployeeIdsByCompanyId(companyId);
        List<UserDto> userDtos = userServiceFeignClient.getUsersByIds(userIds);

        Page<UserDto> result = new PageImpl<>(userDtos, pageable, userDtos.size());

        log.info("Returned page of employees by company name = {}, number of employees: {}",
                companyName, userDtos.size());
        return result;
    }

    @Override
    @Transactional
    public CompanyDto create(CreateCompany company) {
        // Проверяем, нет ли уже компании с таким именем
        if (companyRepository.existsByNameIgnoreCase(company.getName().trim())) {
            throw new CompanyAlreadyExistsException("There is already a company with the name" + company.getName());
        }
        CompanyEntity entity = CompanyMapper.mapCreateCompanyToCompanyEntity(company);
        // Сохраняем компанию в БД и маппим в CompanyDto, чтобы вернуть созданную компанию уже с ID
        // (можно обойтись без этого и просто возвращать статус 201 Created вместо CompanyDto)
        CompanyDto result = CompanyMapper.mapCompanyEntityToCompanyDto(companyRepository.save(entity));
        log.info("Created company: {}", result);
        return result;
    }

    @Override
    public CompanyDto get(long id) {
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "No company with id " + id;
                    log.error("Fail get company: {}", msg);
                    return new CompanyNotFoundException(msg);
                });
        CompanyDto result = CompanyMapper.mapCompanyEntityToCompanyDto(companyEntity);
        log.info("Got company: {}", result);
        return result;
    }
}
