package com.agorohov.srp_stf.company_service.repository;

import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
