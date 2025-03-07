package com.agorohov.srp_stf.company_service.dto;

import java.math.BigDecimal;

public class CompanyDto {

    private long id;
    private String name;
    private BigDecimal budget;

    public CompanyDto() {
    }

    public CompanyDto(long id, String name, BigDecimal budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
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

    @Override
    public String toString() {
        return "CompanyDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }
}
