package com.SaludPlus.v_1.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}