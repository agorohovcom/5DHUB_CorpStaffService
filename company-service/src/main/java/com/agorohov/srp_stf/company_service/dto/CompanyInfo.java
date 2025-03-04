package com.agorohov.srp_stf.company_service.dto;

import java.math.BigDecimal;

public class CompanyInfo {

    private long id;
    private String name;
    private BigDecimal budget;
    private int numberOfEmployees;

    public CompanyInfo() {
    }

    public CompanyInfo(long id, String name, BigDecimal budget, int numberOfEmployees) {
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.numberOfEmployees = numberOfEmployees;
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

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    @Override
    public String toString() {
        return "CompanyInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                ", numberOfEmployees=" + numberOfEmployees +
                '}';
    }
}
