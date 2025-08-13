package com.omar.Management_System.Document;

public class DocumentMapper {
    public static DocumentDto toDto(Document document) {
            DocumentDto dto = new DocumentDto();
            dto.setUrl(document.getUrl());
            dto.setFileName(document.getFileName());
            return dto;
        }
    }

