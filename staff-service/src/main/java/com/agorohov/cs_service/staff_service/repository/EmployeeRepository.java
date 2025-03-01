package com.agorohov.cs_service.staff_service.repository;

import com.agorohov.cs_service.staff_service.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Page<EmployeeEntity> findByLastName(String lastName, Pageable pageable);
}
