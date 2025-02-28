package com.agorohov.cs_service.staff_service.repository;

import com.agorohov.cs_service.staff_service.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByLastName(String lastName);
}
