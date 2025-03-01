package com.agorohov.srp_stf.company_service.repository;

import com.agorohov.srp_stf.company_service.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    Page<EmployeeEntity> findByCompanyId(long companyId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.company.id = :companyId")
    int getNumberOfEmployeesByCompanyId(@Param("companyId") long companyId);
}
