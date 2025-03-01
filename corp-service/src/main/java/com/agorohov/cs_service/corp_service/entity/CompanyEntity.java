package com.agorohov.cs_service.corp_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "companies")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "budget")
    private BigDecimal budget;
    @OneToMany(mappedBy = "company")
    private List<Employee> employees;
}
