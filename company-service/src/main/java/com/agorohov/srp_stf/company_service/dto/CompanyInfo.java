package com.agorohov.srp_stf.company_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfo {

    private long id;
    private String name;
    private BigDecimal budget;
    private int numberOfEmployees;
}
