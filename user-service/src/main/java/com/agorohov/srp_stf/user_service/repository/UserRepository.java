package com.agorohov.srp_stf.user_service.repository;

import com.agorohov.srp_stf.user_service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findByLastNameIgnoreCase(String lastName, Pageable pageable);
}
