package com.agorohov.srp_stf.company_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompany {

    @Size(max = 32)
    @NotBlank
    private String name;
    @PositiveOrZero
    private BigDecimal budget;
}
