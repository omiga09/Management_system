package com.omar.Management_System.Document.DocumentMapper;

import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Document.DocumentDto.DocumentDto;

public class DocumentMapper {
    public static DocumentDto toDto(Document document) {
            DocumentDto dto = new DocumentDto();
            dto.setUrl(document.getUrl());
            dto.setFileName(document.getFileName());
            return dto;
        }
    }

