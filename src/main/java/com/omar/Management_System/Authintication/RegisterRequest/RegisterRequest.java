package com.omar.Management_System.Authintication.RegisterRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String nationalId;
    private String role;
}
