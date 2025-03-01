package com.agorohov.srp_stf.company_service.repository;

import com.agorohov.srp_stf.company_service.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByNameIgnoreCase(String name);
}
