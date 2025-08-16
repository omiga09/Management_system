package com.omar.Management_System.User.UserDto;

import com.omar.Management_System.Document.DocumentDto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private String role;
        private String address;
        private String nationalId;
        private List<DocumentDto> documents;

}
