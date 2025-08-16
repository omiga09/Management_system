package com.omar.Management_System.Property.PropertyService;

import com.omar.Management_System.Commission.SytemCommissionConfigService.SystemCommissionConfigService;
import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Document.DocumentDto.DocumentDto;
import com.omar.Management_System.Photo.Photo.Photo;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyDto.PropertyDto;
import com.omar.Management_System.Property.PropertyRepository.PropertyRepository;
import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Reservation.ReservationRepository.ReservationRepository;
import com.omar.Management_System.User.User.User;
import com.omar.Management_System.User.UsetrRepository.UserRepository;
import com.omar.Management_System.enums.Role.Role;
import com.omar.Management_System.location.Location.Location;
import com.omar.Management_System.location.LocationDto.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    private SystemCommissionConfigService commissionService;


    private String generateReservationCode() {
        return "RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


    public List<PropertyDto> search(String type, Double minPrice, Double maxPrice, String location, Boolean available) {
        List<Property> allProperties = propertyRepository.findAll();

        return allProperties.stream()
                .filter(p -> type == null || p.getType().equalsIgnoreCase(type))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .filter(p -> location == null || (p.getLocation() != null && p.getLocation().equalsIgnoreCase(location)))
                .filter(p -> available == null || Boolean.valueOf(p.isAvailable()).equals(available))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public PropertyDto mapToDto(Property property) {
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
                property.isAvailable()
        );
    }


    private LocationDto toLocationDto(Location location) {
        if (location == null) return null;

        return new LocationDto(
                location.getCountry(),
                location.getCity(),
                location.getAddress(),
                location.getLat(),
                location.getLng()
        );
    }


    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
    }


    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }


    public Property createProperty(Property property, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
        property.setSeller(seller);
        return propertyRepository.save(property);
    }


    public Property updateProperty(Long id, Property updatedProperty, String sellerEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        if (!property.getSeller().getEmail().equals(sellerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this property");
        }

        property.setTitle(updatedProperty.getTitle());
        property.setDescription(updatedProperty.getDescription());
        property.setPrice(updatedProperty.getPrice());
        property.setType(updatedProperty.getType());
        property.setStatus(updatedProperty.getStatus());
        property.setLocation(updatedProperty.getLocation());
        property.setDocuments(updatedProperty.getDocuments());


        List<Photo> existingPhotos = new ArrayList<>(property.getPhotos());
        for (Photo photo : existingPhotos) {
            property.removePhoto(photo);
        }
        if (updatedProperty.getPhotos() != null) {
            for (Photo newPhoto : updatedProperty.getPhotos()) {
                property.addPhoto(newPhoto);
            }
        }

        return propertyRepository.save(property);
    }

    public void reserveProperty(Long propertyId, String buyerEmail) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Ikiwa status si AVAILABLE, weka iwe AVAILABLE
        if (!"AVAILABLE".equalsIgnoreCase(property.getStatus())) {
            property.setStatus("AVAILABLE");
            property.setAvailable(true);
        }

        if (!property.isAvailable()) {
            throw new RuntimeException("Property is not available for reservation");
        }

        User buyer = userRepository.findByEmail(buyerEmail)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        if (buyer.getRole() != Role.BUYER) {
            throw new RuntimeException("Unauthorized access: User is not a BUYER");
        }

        Reservation reservation = new Reservation();
        reservation.setBuyer(buyer);
        reservation.setProperty(property);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("Pending");
        reservation.setReservationCode(generateReservationCode());

        property.addReservation(reservation);
        property.setAvailable(false);
        property.setStatus("Reserved");

        propertyRepository.save(property);
    }

    public void deleteProperty(Long id, String sellerEmail) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        if (!property.getSeller().getEmail().equals(sellerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this property");
        }

        propertyRepository.deleteById(id);
    }


    public List<Property> getPropertiesBySellerEmail(String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
        return propertyRepository.findBySeller(seller);
    }


    public Property uploadPhoto(Long propertyId, String sellerEmail, MultipartFile file) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        if (!property.getSeller().getEmail().equals(sellerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to upload photo to this property");
        }

        if (property.getPhotos() == null) {
            property.setPhotos(new ArrayList<>());
        }

        String url = saveFileToStorage(file);

        Photo photo = new Photo();
        photo.setFilename(file.getOriginalFilename());
        photo.setUrl(url);
        photo.setProperty(property);

        property.addPhoto(photo);

        return propertyRepository.save(property);
    }


    public Property uploadDocument(Long propertyId, String sellerEmail, MultipartFile file) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        if (!property.getSeller().getEmail().equals(sellerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to upload document to this property");
        }

        if (property.getDocuments() == null) {
            property.setDocuments(new HashSet<>());
        }

        String url = saveFileToStorage(file);

        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setUrl(url);
        document.setProperty(property);

        property.getDocuments().add(document);

        boolean verified = verifyOwnershipDocuments(property);
        property.setDocumentsVerified(verified);

        return propertyRepository.save(property);
    }


    private String saveFileToStorage(MultipartFile file) {
        String folderPath = System.getProperty("user.dir") + File.separator + "uploads";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new RuntimeException("Failed to create upload directory");
            }
        }

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(folder, uniqueName);

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        return "/uploads/" + uniqueName;
    }


    private boolean verifyOwnershipDocuments(Property property) {
        if (property.getDocuments() == null || property.getDocuments().isEmpty()) {
            return false;
        }

        for (Document doc : property.getDocuments()) {
            if (doc.getUrl() == null || doc.getFileName() == null) {
                return false;
            }
        }

        return true;
    }


    public void purchaseProperty(Long reservationId, String buyerEmail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Property property = reservation.getProperty();

        if (!reservation.getBuyer().getEmail().equalsIgnoreCase(buyerEmail)) {
            throw new RuntimeException("Unauthorized: Not your reservation");
        }

        if (!"Approved".equalsIgnoreCase(reservation.getStatus()) || !property.isAvailable()) {
            throw new RuntimeException("Property not available for purchase");
        }


        BigDecimal price = BigDecimal.valueOf(property.getPrice());
        BigDecimal commission = commissionService.calculateCommission(price);


        reservation.setCommission(commission.doubleValue());

        // Update property status
        property.setAvailable(false);
        property.setStatus("SOLD");

        reservationRepository.save(reservation);
        propertyRepository.save(property);
    }



    public Property approveProperty(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));

        if (!property.isDocumentsVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Documents not verified");
        }

        property.setApproved(true);
        return propertyRepository.save(property);
    }

    public Property updatePropertyByAdmin(Long id, PropertyDto dto) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));


        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setPrice(dto.getPrice());
        property.setType(dto.getType());
        property.setStatus(dto.getStatus());
        property.setAvailable(dto.isAvailable());


        if (dto.getLocation() != null) {
            Location location = property.getLocation();
            if (location == null) {
                location = new Location();
            }
            location.setCountry(dto.getLocation().getCountry());
            location.setCity(dto.getLocation().getCity());
            location.setAddress(dto.getLocation().getAddress());
            location.setLat(dto.getLocation().getLat());
            location.setLng(dto.getLocation().getLng());
            property.setLocation(location);
        }


        if (dto.getPhotos() != null) {
            if (property.getPhotos() == null) {
                property.setPhotos(new ArrayList<>());
            } else {
                property.getPhotos().clear();
            }
            for (String filename : dto.getPhotos()) {
                Photo photo = new Photo();
                photo.setFilename(filename);
                photo.setProperty(property);
                property.getPhotos().add(photo);
            }
        }


        if (dto.getDocuments() != null) {
            if (property.getDocuments() == null) {
                property.setDocuments(new HashSet<>());
            } else {
                property.getDocuments().clear();
            }
            for (DocumentDto docDto : dto.getDocuments()) {
                Document doc = new Document();
                doc.setFileName(docDto.getFileName());
                doc.setUrl(docDto.getUrl());
                doc.setProperty(property);
                property.getDocuments().add(doc);
            }
        }

        return propertyRepository.save(property);
    }


    public void deletePropertyByAdmin(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
        propertyRepository.delete(property);

    }

    public void makeAvailable(Long propertyId, String sellerEmail) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getSeller().getEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this property");
        }

        property.setAvailable(true);
        property.setStatus("AVAILABLE");

        propertyRepository.save(property);
    }

    public void decideOnReservation(Long reservationId, String sellerEmail, boolean approve) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            throw new RuntimeException("Reservation not found");
        }

        Reservation reservation = reservationOpt.get();
        Property property = reservation.getProperty();

        if (!property.getSeller().getEmail().equalsIgnoreCase(sellerEmail)) {
            throw new RuntimeException("Unauthorized: Not your property");
        }

        reservation.setStatus(approve ? "Approved" : "Rejected");

        if (!approve) {
            property.setAvailable(true);
            property.setStatus("AVAILABLE");
        }

        reservationRepository.save(reservation);
        propertyRepository.save(property);
    }
}