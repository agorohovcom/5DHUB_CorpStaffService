package com.agorohov.srp_stf.company_service.mapper;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;

public class CompanyMapper {

    public static CompanyDto mapEntityToDto(CompanyEntity entity, int numberOfEmployees) {
        CompanyDto result = new CompanyDto();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setBudget(entity.getBudget());
        result.setNumberOfEmployees(numberOfEmployees);
        return result;
    }

    private CompanyMapper() {
    }
}
