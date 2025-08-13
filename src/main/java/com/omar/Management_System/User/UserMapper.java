package com.omar.Management_System.User;

import com.omar.Management_System.Document.DocumentDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDTO(User user) {
        List<DocumentDto> documentDtos = user.getDocuments() != null
                ? user.getDocuments().stream()
                .map(doc -> new DocumentDto(doc.getUrl(), doc.getFileName()))
                .collect(Collectors.toList())
                : Collections.emptyList();

        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.getAddress(),
                user.getNationalId(),
                documentDtos
        );
    }
}
