package com.agorohov.srp_stf.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUser {

    @Size(max = 32)
    @NotBlank
    private String firstName;
    @Size(max = 32)
    @NotBlank
    private String lastName;
    @NotBlank
    @Pattern(regexp = "\\+\\d{1,3}\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phoneNumber;

    public CreateUser() {
    }

    public CreateUser(String phoneNumber, String lastName, String firstName) {
        this.phoneNumber = phoneNumber;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
