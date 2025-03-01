package com.agorohov.srp_stf.company_service.service;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.UserDto;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyDto getById(long id) {
        // получаем компанию по id или выбрасываем исключение
        CompanyEntity companyEntity = companyRepository.findById(id)
                .orElseThrow(() -> {
                    String msg = "There isn't company with id " + id;
                    log.error(msg);
                    return new CompanyNotFoundException(msg);
                });

        List<UserDto> companyEmployees = new ArrayList<>();
        // TODO реализовать получение юзеров из user_service

        // Преобразуем сущности в ДТО
        CompanyDto result = new CompanyDto();
        result.setId(companyEntity.getId());
        result.setName(companyEntity.getName());
        result.setBudget(companyEntity.getBudget());
        result.setEmployees(companyEmployees);

        log.info("Company with id {} returned", id);
        return result;
    }

    @Override
    public CompanyDto getByName(String name) {
        return null;
    }
}
