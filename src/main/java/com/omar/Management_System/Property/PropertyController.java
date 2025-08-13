package com.omar.Management_System.Property;

import com.omar.Management_System.User.User;
import com.omar.Management_System.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService;

    // Admin & Buyer: View all properties
    @GetMapping
    public List<PropertyDto> getAllProperties() {
        return propertyService.getAllProperties().stream()
                .map(PropertyMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Anyone: View property by ID
    @GetMapping("/{id}")
    public ResponseEntity<PropertyDto> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.getPropertyById(id);
        if (property != null) {
            return ResponseEntity.ok(PropertyMapper.toDTO(property));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Seller: Create property using JWT email
    @PreAuthorize("hasRole('SELLER')")
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(Authentication authentication,
                                                      @RequestBody PropertyDto propertyDto) {
        String email = authentication.getName();
        User seller = userService.getByEmail(email);
        Property property = PropertyMapper.toEntity(propertyDto);
        Property created = propertyService.createProperty(property, seller.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PropertyMapper.toDTO(created));
    }

    // Seller: Update own property
    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<PropertyDto> updateProperty(Authentication authentication,
                                                      @PathVariable Long id,
                                                      @RequestBody PropertyDto propertyDto) {
        String email = authentication.getName();
        Property property = PropertyMapper.toEntity(propertyDto);
        Property updated = propertyService.updateProperty(id, property, email);
        return ResponseEntity.ok(PropertyMapper.toDTO(updated));
    }

    // Seller: Delete own property
    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(Authentication authentication,
                                               @PathVariable Long id) {
        String email = authentication.getName();
        propertyService.deleteProperty(id, email);
        return ResponseEntity.noContent().build();
    }

    // Seller: View own properties
    @PreAuthorize("hasRole('SELLER')")
    @GetMapping("/me")
    public List<PropertyDto> getMyProperties(Authentication authentication) {
        String email = authentication.getName();
        return propertyService.getPropertiesBySellerEmail(email).stream()
                .map(PropertyMapper::toDTO)
                .collect(Collectors.toList());
    }
}
