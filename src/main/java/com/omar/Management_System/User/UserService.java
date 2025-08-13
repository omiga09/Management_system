package com.omar.Management_System.User;

import com.omar.Management_System.Authintication.CustomerUserDetails;
import com.omar.Management_System.Document.Document;
import com.omar.Management_System.Document.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

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
        userRepository.deleteById(id);
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new CustomerUserDetails(getByEmail(email));
    }
}
