package com.agorohov.srp_stf.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
