package com.omar.Management_System.Property;
import com.omar.Management_System.Document.DocumentDto;
import com.omar.Management_System.location.LocationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PropertyDto {
        private Long id;
        private String title;
        private String description;
        private String type;
        private Double price;
        private LocationDto location;
        private String status;
        private List<String> photos;
        private List<DocumentDto> documents;


    public PropertyDto(Long id, String title, String description, String type, Double price,
                       LocationDto location, String status, List<String> photos, List<DocumentDto> documents) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.price = price;
        this.location = location;
        this.status = status;
        this.photos = photos;
        this.documents = documents;
    }

}

