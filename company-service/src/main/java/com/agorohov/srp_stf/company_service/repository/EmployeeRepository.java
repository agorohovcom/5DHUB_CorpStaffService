package com.agorohov.srp_stf.company_service.repository;

import com.agorohov.srp_stf.company_service.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT COUNT(e) FROM EmployeeEntity e WHERE e.company.id = :companyId")
    int getNumberOfEmployeesByCompanyId(@Param("companyId") long companyId);

    @Query("SELECT e.userId FROM EmployeeEntity e WHERE e.company.id = :companyId")
    List<Long> findEmployeeIdsByCompanyId(long companyId);
}
