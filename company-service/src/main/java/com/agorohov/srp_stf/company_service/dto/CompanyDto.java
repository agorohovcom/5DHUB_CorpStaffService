package com.agorohov.srp_stf.company_service.dto;

import java.math.BigDecimal;
import java.util.List;

public class CompanyDto {

    private long id;
    private String name;
    private BigDecimal budget;
    private List<UserDto> employees;

    public CompanyDto() {
    }

    public CompanyDto(long id, String name, BigDecimal budget, List<UserDto> employees) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.employees = employees;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public List<UserDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<UserDto> employees) {
        this.employees = employees;
    }
}
