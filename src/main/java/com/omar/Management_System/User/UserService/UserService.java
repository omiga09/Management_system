package com.omar.Management_System.User.UserService;

import com.omar.Management_System.Authintication.CustomerUserDetails.CustomerUserDetails;
import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Document.DocumentRepository.DocumentRepository;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyRepository.PropertyRepository;
import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Reservation.ReservationDto.ReservationDto;
import com.omar.Management_System.Reservation.ReservationRepository.ReservationRepository;
import com.omar.Management_System.Reservation.ReservationMapper.ReservationMapper;
import com.omar.Management_System.User.User.User;
import com.omar.Management_System.User.UsetrRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            user.setRole(updatedUser.getRole());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserByEmail(String email, User updatedUser) {
        User user = getByEmail(email);
        user.setFullName(updatedUser.getFullName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Property> properties = propertyRepository.findBySeller(user);

        for (Property property : properties) {
            property.getDocuments().clear();
            propertyRepository.save(property);

            List<Document> documents = documentRepository.findByProperty(property);
            documentRepository.deleteAll(documents);
        }

        propertyRepository.deleteAll(properties);
        userRepository.delete(user);
    }

    public User updateUserById(Long id, User updatedData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFullName(updatedData.getFullName());
        existingUser.setEmail(updatedData.getEmail());
        existingUser.setPhone(updatedData.getPhone());
        existingUser.setAddress(updatedData.getAddress());
        existingUser.setNationalId(updatedData.getNationalId());
        existingUser.setRole(updatedData.getRole());
        existingUser.setPassword(passwordEncoder.encode(updatedData.getPassword()));

        return userRepository.save(existingUser);
    }

    public void deleteUserByEmail(String email) {
        User user = getByEmail(email);
        userRepository.delete(user);
    }

    public User addDocumentsToUser(Long userId, List<Long> documentIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Document> documents = documentRepository.findAllById(documentIds);
        user.getDocuments().addAll(documents);
        return userRepository.save(user);
    }

    public User addDocumentsToUserByEmail(String email, List<Long> documentIds) {
        User user = getByEmail(email);
        List<Document> documents = documentRepository.findAllById(documentIds);
        user.getDocuments().addAll(documents);
        return userRepository.save(user);
    }

    // 🏠 Reserve property by buyer
    public ReservationDto reserveProperty(String buyerEmail, Long propertyId) {
        User buyer = getByEmail(buyerEmail);
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.isAvailable()) {
            throw new RuntimeException("Property is not available for reservation");
        }

        Reservation reservation = new Reservation();
        reservation.setBuyer(buyer);
        reservation.setProperty(property);
        reservation.setReservationCode(UUID.randomUUID().toString());

        property.setAvailable(false); // mark as reserved
        propertyRepository.save(property);

        Reservation saved = reservationRepository.save(reservation);
        return ReservationMapper.toDto(saved);
    }

    // 📋 Get reservations by buyer
    public List<ReservationDto> getReservationsByBuyer(String buyerEmail) {
        User buyer = getByEmail(buyerEmail);
        List<Reservation> reservations = reservationRepository.findByBuyer(buyer);
        return reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomerUserDetails(getByEmail(email));
    }
}
