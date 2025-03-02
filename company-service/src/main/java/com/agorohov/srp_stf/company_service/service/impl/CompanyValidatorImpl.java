package com.agorohov.srp_stf.company_service.service.impl;

import com.agorohov.srp_stf.company_service.exception.CompanyNotFoundException;
import com.agorohov.srp_stf.company_service.repository.CompanyRepository;
import com.agorohov.srp_stf.company_service.service.CompanyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyValidatorImpl implements CompanyValidator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CompanyRepository companyRepository;

    public CompanyValidatorImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void validateCompanyExists(long companyId) {
        if (!companyRepository.existsById(companyId)) {
            String msg = "Company with id " + companyId + " not found";
            log.error(msg);
            throw new CompanyNotFoundException(msg);
        }
    }
}
