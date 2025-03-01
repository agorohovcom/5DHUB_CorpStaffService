package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import com.agorohov.srp_stf.company_service.service.CompanyService;
import com.agorohov.srp_stf.company_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyRepository companyRepository;
    private final EmployeeService employeeService;

    public CompanyServiceImpl(CompanyRepository companyRepository, EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
    }

    @Override
    public CompanyDto getById(long id) {
        // получаем компанию по id или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "There is no company with id " + id;
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(id);

        // Преобразуем сущность компании в ДТО
        CompanyDto result = mapEntityToDto(companyEntity, numberOfEmployees);

        log.info("Company found by id: {}", result);
        return result;
    }

    @Override
    public CompanyDto getByName(String name) {
        // получаем компанию по имени или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findByNameIgnoreCase(name.trim())
                .orElseThrow(() -> {
                    String msg = "There is no company with name " + name;
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        // получаем количество сотрудников компании отдельным запросом через EmployeeRepository,
        // чтобы не выгружать весь список сотрудников ради из количества
        int numberOfEmployees = employeeService.getNumberOfEmployeesByCompanyId(companyEntity.getId());

        // Преобразуем сущность компании в ДТО и возвращаем результат
        CompanyDto result = mapEntityToDto(companyEntity, numberOfEmployees);
        log.info("Company found by name: {}", result);
        return result;
    }

    private CompanyDto mapEntityToDto(CompanyEntity entity, int numberOfEmployees) {
        CompanyDto result = new CompanyDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setBudget(entity.getBudget());
        result.setNumberOfEmployees(numberOfEmployees);
        return result;
    }
}
