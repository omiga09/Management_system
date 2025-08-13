package com.omar.Management_System.Property;

import com.omar.Management_System.User.User;
import com.omar.Management_System.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
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
        property.setPhotos(updatedProperty.getPhotos());
        property.setDocuments(updatedProperty.getDocuments());
        property.setLocation(updatedProperty.getLocation());

        return propertyRepository.save(property);
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
}
