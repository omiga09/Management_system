package com.omar.Management_System.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omar.Management_System.Document.Document;
import com.omar.Management_System.Reservation.Reservation;
import com.omar.Management_System.enums.Role;
import com.omar.Management_System.Property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String email;
    private String phone;
    private String address;

    @Column(nullable = false, unique = true)
    private String nationalId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Property> properties;

    @JsonIgnore
    @OneToMany(mappedBy = "buyer")
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "user_documents",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private Set<Document> documents = new HashSet<>();
}
