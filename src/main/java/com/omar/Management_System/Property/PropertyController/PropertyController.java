package com.omar.Management_System.Property.PropertyController;

import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyDto.PropertyDto;
import com.omar.Management_System.Property.PropertyMapper.PropertyMapper;
import com.omar.Management_System.Property.PropertyService.PropertyService;
import com.omar.Management_System.User.User.User;
import com.omar.Management_System.User.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        Property property = propertyService.getPropertyById(id);

        if (property == null) {
            return ResponseEntity.notFound().build();
        }

        if (!property.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(PropertyMapper.toDTO(property));
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(Authentication authentication, @RequestBody PropertyDto propertyDto) {
        String email = authentication.getName();
        User seller = userService.getByEmail(email);
        Property property = PropertyMapper.toEntity(propertyDto);
        Property created = propertyService.createProperty(property, seller.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PropertyMapper.toDTO(created));
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("/search")
    public List<PropertyDto> searchProperties(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean available) {
        return propertyService.search(type, minPrice, maxPrice, location, available);
    }


    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveProperty(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        try {
            propertyService.reserveProperty(id, email);  // Logic ya reservation inashughulikiwa kwenye service
            return ResponseEntity.ok("Property reserved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to reserve property: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDto> updateProperty(Authentication authentication, @PathVariable Long id, @RequestBody PropertyDto propertyDto) {
        String email = authentication.getName();
        Property existing = propertyService.getPropertyById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existing.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Property property = PropertyMapper.toEntity(propertyDto);
        Property updated = propertyService.updateProperty(id, property, email);
        return ResponseEntity.ok(PropertyMapper.toDTO(updated));
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(Authentication authentication, @PathVariable Long id) {
        String email = authentication.getName();
        Property existing = propertyService.getPropertyById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existing.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        propertyService.deleteProperty(id, email);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/me")
    public List<PropertyDto> getMyProperties(Authentication authentication) {
        String email = authentication.getName();
        return propertyService.getPropertiesBySellerEmail(email).stream()
                .map(PropertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/{id}/photos")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile photoFile,
            Authentication authentication) {

        String email = authentication.getName();
        Property property = propertyService.getPropertyById(id);

        if (property == null) {
            return ResponseEntity.notFound().build();
        }

        if (!property.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            propertyService.uploadPhoto(id, email, photoFile);
            return ResponseEntity.ok("Photo uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload photo: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/{id}/documents/upload")
    public ResponseEntity<PropertyDto> uploadDocumentToProperty(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String email = authentication.getName();
        Property existing = propertyService.getPropertyById(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existing.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Property updatedProperty = propertyService.uploadDocument(id, email, file);
        PropertyDto propertyDto = PropertyMapper.toDTO(updatedProperty);
        return ResponseEntity.ok(propertyDto);
    }
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}/make-available")
    public ResponseEntity<String> makePropertyAvailable(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        try {
            propertyService.makeAvailable(id, email);
            return ResponseEntity.ok("Property marked as available");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/purchase/{reservationId}")
    public ResponseEntity<String> purchaseProperty(
            @PathVariable Long reservationId,
            @RequestParam String buyerEmail) {

        try {
            propertyService.purchaseProperty(reservationId, buyerEmail);
            return ResponseEntity.ok("Property purchased successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<PropertyDto> getAllPropertiesForAdmin() {
        return propertyService.getAllProperties().stream()
                .map(PropertyMapper::toDTO)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/approve")
    public ResponseEntity<PropertyDto> approveProperty(@PathVariable Long id) {
        Property approved = propertyService.approveProperty(id);
        return ResponseEntity.ok(PropertyMapper.toDTO(approved));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<PropertyDto> updatePropertyByAdmin(@PathVariable Long id, @RequestBody PropertyDto dto) {
        Property updated = propertyService.updatePropertyByAdmin(id, dto);
        return ResponseEntity.ok(PropertyMapper.toDTO(updated));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletePropertyByAdmin(@PathVariable Long id) {
        propertyService.deletePropertyByAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // 📌 Check verification status
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}/verification-status")
    public ResponseEntity<Boolean> checkVerificationStatus(@PathVariable Long id, Authentication authentication) {
        Property property = propertyService.getPropertyById(id);

        if (property == null) {
            return ResponseEntity.notFound().build();
        }

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !property.getSeller().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(property.isDocumentsVerified());
    }
}
