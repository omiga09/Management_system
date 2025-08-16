package com.omar.Management_System.Document.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.User.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String fileName;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)  // Optional: cascade persist to save document when property is saved
    @JoinColumn(name = "property_id")
    private Property property;

    @JsonIgnore
    @ManyToMany(mappedBy = "documents")
    private Set<User> users = new HashSet<>();
}
