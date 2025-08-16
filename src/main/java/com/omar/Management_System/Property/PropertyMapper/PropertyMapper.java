package com.omar.Management_System.Property.PropertyMapper;

import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Document.DocumentDto.DocumentDto;
import com.omar.Management_System.Photo.Photo.Photo;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyDto.PropertyDto;
import com.omar.Management_System.location.Location.Location;
import com.omar.Management_System.location.LocationDto.LocationDto;

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
        property.setLocation(toLocationEntity(dto.getLocation()));

        // Photos mapping
        if (dto.getPhotos() != null) {
            List<Photo> photos = dto.getPhotos().stream()
                    .map(filename -> {
                        Photo photo = new Photo();
                        photo.setFilename(filename);
                        photo.setProperty(property);  // Bi-directional link
                        return photo;
                    }).collect(Collectors.toList());
            property.setPhotos(photos);
        }

        // Documents mapping
        if (dto.getDocuments() != null) {
            Set<Document> documents = dto.getDocuments().stream()
                    .map(docDto -> {
                        Document doc = new Document();
                        doc.setUrl(docDto.getUrl());
                        doc.setFileName(docDto.getFileName());
                        doc.setProperty(property);  // Bi-directional link
                        return doc;
                    }).collect(Collectors.toSet());
            property.setDocuments(documents);
        }

        return property;
    }

    public static PropertyDto toDTO(Property property) {
        if (property == null) return null;

        LocationDto locationDto = toLocationDto(property.getLocation());

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
                documents,
                property.isAvailable());
    }

    // 🔧 Helper method to convert LocationDto to Location entity
    private static Location toLocationEntity(LocationDto dto) {
        if (dto == null) return null;

        Location location = new Location();
        location.setCountry(dto.getCountry());
        location.setCity(dto.getCity());
        location.setAddress(dto.getAddress());
        location.setLat(dto.getLat());
        location.setLng(dto.getLng());
        return location;
    }

    // 🔧 Helper method to convert Location entity to LocationDto
    private static LocationDto toLocationDto(Location location) {
        if (location == null) return null;

        return new LocationDto(
                location.getCountry(),
                location.getCity(),
                location.getAddress(),
                location.getLat(),
                location.getLng()
        );
    }
}
