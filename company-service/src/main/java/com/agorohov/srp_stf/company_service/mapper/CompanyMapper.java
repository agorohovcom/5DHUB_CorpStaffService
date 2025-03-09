package com.agorohov.srp_stf.company_service.mapper;

import com.agorohov.srp_stf.company_service.dto.*;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "budget", source = "entity.budget")
    CompanyDto mapCompanyEntityToCompanyDto(CompanyEntity entity);

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "budget", source = "entity.budget")
    @Mapping(target = "numberOfEmployees", source = "numberOfEmployees")
    CompanyInfo mapCompanyEntityToCompanyInfo(CompanyEntity entity, int numberOfEmployees);

    @Mapping(target = "name", source = "company.name")
    @Mapping(target = "budget", source = "company.budget")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employees", ignore = true)
    CompanyEntity mapCreateCompanyToCompanyEntity(CreateCompany company);

    @Mapping(target = "id", source = "company.id")
    @Mapping(target = "name", source = "company.name")
    @Mapping(target = "budget", source = "company.budget")
    @Mapping(target = "employees", ignore = true)
    CompanyEntity mapUpdateCompanyToCompanyEntity(UpdateCompany company);

    @Mapping(target = "id", source = "companyDto.id")
    @Mapping(target = "name", source = "companyDto.name")
    @Mapping(target = "budget", source = "companyDto.budget")
    @Mapping(target = "employees", ignore = true)
    CompanyEntity mapCompanyDtoToCompanyEntityWithoutEmployees(CompanyDto companyDto);

    @Mapping(target = "userId", source = "entity.userId")
    @Mapping(target = "companyId", source = "entity.company.id")
    EmployeeDto mapEmployeeEntityToEmployeeDto(EmployeeEntity entity);
}
