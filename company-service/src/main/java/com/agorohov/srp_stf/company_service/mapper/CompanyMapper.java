package com.agorohov.srp_stf.company_service.mapper;

import com.agorohov.srp_stf.company_service.dto.CompanyDto;
import com.agorohov.srp_stf.company_service.dto.CompanyInfo;
import com.agorohov.srp_stf.company_service.dto.CreateCompany;
import com.agorohov.srp_stf.company_service.dto.UpdateCompany;
import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import com.agorohov.srp_stf.company_service.exception.MapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyMapper {

    private static final Logger log = LoggerFactory.getLogger(CompanyMapper.class);

    public static CompanyDto mapCompanyEntityToCompanyDto(CompanyEntity entity) {
        try {
            CompanyDto result = new CompanyDto();
            result.setId(entity.getId());
            result.setName(entity.getName());
            result.setBudget(entity.getBudget());
            return result;
        } catch (Exception ex) {
            log.error("Failed mapping CompanyEntity to CompanyDto: {}", ex.getLocalizedMessage());
            throw new MapperException(ex);
        }
    }

    public static CompanyInfo mapCompanyEntityToCompanyInfo(CompanyEntity entity, int numberOfEmployees) {
        try {
            CompanyInfo result = new CompanyInfo();
            result.setId(entity.getId());
            result.setName(entity.getName());
            result.setBudget(entity.getBudget());
            result.setNumberOfEmployees(numberOfEmployees);
            return result;
        } catch (Exception ex) {
            log.error("Failed mapping CompanyEntity to CompanyInfo: {}", ex.getLocalizedMessage());
            throw new MapperException(ex);
        }
    }

    private CompanyMapper() {
    }

    public static CompanyEntity mapCreateCompanyToCompanyEntity(CreateCompany company) {
        try {
            CompanyEntity result = new CompanyEntity();
            result.setName(company.getName().trim());
            result.setBudget(company.getBudget());
            return result;
        } catch (Exception ex) {
            log.error("Failed mapping CreateCompany to CompanyEntity: {}", ex.getLocalizedMessage());
            throw new MapperException(ex);
        }
    }

    public static CompanyEntity mapUpdateCompanyToCompanyEntity(UpdateCompany company) {
        try {
            CompanyEntity result = new CompanyEntity();
            result.setId(company.getId());
            result.setName(company.getName());
            result.setBudget(company.getBudget());
            return result;
        } catch (Exception ex) {
            log.error("Failed mapping UpdateCompany to CompanyEntity: {}", ex.getLocalizedMessage());
            throw new MapperException(ex);
        }
    }

}
