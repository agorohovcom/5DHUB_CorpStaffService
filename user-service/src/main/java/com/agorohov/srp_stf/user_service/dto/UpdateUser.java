package com.agorohov.srp_stf.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

    @Positive
    private long id;
    @Size(max = 32)
    @NotBlank
    private String firstName;
    @Size(max = 32)
    @NotBlank
    private String lastName;
    @Pattern(regexp = "\\+\\d{1,3}\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phoneNumber;
}
