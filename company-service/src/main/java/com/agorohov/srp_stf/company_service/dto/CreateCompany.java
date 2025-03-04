package com.agorohov.srp_stf.company_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreateCompany {

    @Size(max = 32)
    @NotBlank
    private String name;
    @PositiveOrZero
    private BigDecimal budget;

    public CreateCompany() {
    }

    public CreateCompany(String name, BigDecimal budget) {
        this.name = name;
        this.budget = budget;
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
}
