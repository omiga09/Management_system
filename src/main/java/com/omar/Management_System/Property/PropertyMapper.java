package com.omar.Management_System.Property;
import com.omar.Management_System.Document.Document;
import com.omar.Management_System.Document.DocumentDto;
import com.omar.Management_System.Photo.Photo;
import com.omar.Management_System.location.Location;

import com.omar.Management_System.location.LocationDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertyMapper {

    public static Property toEntity(PropertyDto dto) {
        if (dto == null) return null;

        Property property = new Property();
        property.setPropertyId(dto.getId());
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setType(dto.getType());
        property.setPrice(dto.getPrice());
        property.setStatus(dto.getStatus());

        // Location mapping
        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setCountry(dto.getLocation().getCountry());
            location.setCity(dto.getLocation().getCity());
            location.setAddress(dto.getLocation().getAddress());
            location.setLat(dto.getLocation().getLat());
            location.setLng(dto.getLocation().getLng());
            property.setLocation(location);
        }

        // Photos mapping
        if (dto.getPhotos() != null) {
            List<Photo> photos = dto.getPhotos().stream()
                    .map(filename -> {
                        Photo photo = new Photo();
                        photo.setFilename(filename);
                        photo.setProperty(property);  // Set bi-directional link
                        return photo;
                    }).collect(Collectors.toList());
            property.setPhotos(photos);
        }

        if (dto.getDocuments() != null) {
            Set<Document> documents = dto.getDocuments().stream()
                    .map(docDto -> {
                        Document doc = new Document();
                        doc.setUrl(docDto.getUrl());
                        doc.setFileName(docDto.getFileName());
                        doc.setProperty(property); // Set bi-directional link (important)
                        return doc;
                    }).collect(Collectors.toSet());
            property.setDocuments(documents);
        }

        return property;
    }

    public static PropertyDto toDTO(Property property) {
        if (property == null) return null;

        LocationDto locationDto = null;
        if (property.getLocation() != null) {
            locationDto = new LocationDto(
                    property.getLocation().getCountry(),
                    property.getLocation().getCity(),
                    property.getLocation().getAddress(),
                    property.getLocation().getLat(),
                    property.getLocation().getLng()
            );
        }

        List<String> photos = property.getPhotos() == null ? null :
                property.getPhotos().stream()
                        .map(Photo::getFilename)
                        .collect(Collectors.toList());

        List<DocumentDto> documents = property.getDocuments() == null ? null :
                property.getDocuments().stream()
                        .map(doc -> {
                            DocumentDto docDto = new DocumentDto();
                            docDto.setUrl(doc.getUrl());
                            docDto.setFileName(doc.getFileName());
                            return docDto;
                        }).collect(Collectors.toList());

        return new PropertyDto(
                property.getPropertyId(),
                property.getTitle(),
                property.getDescription(),
                property.getType(),
                property.getPrice(),
                locationDto,
                property.getStatus(),
                photos,
                documents
        );
    }
}
