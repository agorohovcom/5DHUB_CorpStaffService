package com.agorohov.srp_stf.company_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @Column(name = "user_id")
    private long userId;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    public EmployeeEntity() {
    }

    public EmployeeEntity(long userId, CompanyEntity company) {
        this.userId = userId;
        this.company = company;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
