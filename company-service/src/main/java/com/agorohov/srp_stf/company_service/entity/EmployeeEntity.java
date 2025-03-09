package com.agorohov.srp_stf.company_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @Column(name = "user_id")
    private long userId;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
