package com.capstone.lms_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String auth;
}
