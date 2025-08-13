package com.omar.Management_System.User;

import com.omar.Management_System.Document.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Admin only: View all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Admin only: View any user by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = UserMapper.toDTO(userOpt.get());
        return ResponseEntity.ok(userDto);
    }

    // Admin only: Create user manually
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PreAuthorize("hasAnyRole('SELLER', 'BUYER')")
    @PutMapping("/me")
    public ResponseEntity<User> updateOwnProfile(Authentication authentication, @RequestBody User user) {
        String email = authentication.getName();
        User updated = userService.updateUserByEmail(email, user);
        return ResponseEntity.ok(updated);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('SELLER', 'BUYER')")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteOwnAccount(Authentication authentication) {
        String email = authentication.getName();
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('SELLER', 'BUYER')")
    @PutMapping("/me/documents")
    public ResponseEntity<UserDto> attachDocumentsToOwnProfile(
            Authentication authentication,
            @RequestBody List<Long> documentIds) {
        String email = authentication.getName();
        User updatedUser = userService.addDocumentsToUserByEmail(email, documentIds);
        UserDto userDto = UserMapper.toDTO(updatedUser);
        return ResponseEntity.ok(userDto);
    }


    @PreAuthorize("hasAnyRole('SELLER', 'BUYER')")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        UserDto userDto = UserMapper.toDTO(user);
        return ResponseEntity.ok(userDto);
    }
}
