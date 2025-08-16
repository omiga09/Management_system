package com.omar.Management_System.Authintication.JwtResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String role;
    private String fullName;

    public JwtResponse(String token, String role, String fullName) {
        this.token = token;
        this.role = role;
        this.fullName = fullName;
    }
}

