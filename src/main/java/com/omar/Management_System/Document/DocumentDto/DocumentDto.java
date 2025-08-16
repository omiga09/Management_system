package com.omar.Management_System.Document.DocumentDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto {
        private String url;
        private String fileName;


    public DocumentDto() {
        // default constructor
    }

    public DocumentDto(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }
}

