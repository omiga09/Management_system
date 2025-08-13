package com.omar.Management_System.Property;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omar.Management_System.Document.Document;
import com.omar.Management_System.Maintenance.Maintenance;
import com.omar.Management_System.Photo.Photo;
import com.omar.Management_System.Reservation.Reservation;
import com.omar.Management_System.User.User;
import com.omar.Management_System.location.Location;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    private String title;
    private String description;
    private String type; // For Rent, For Sale, Maintenance Only
    private Double price;
    private String status; // Available, Reserved, Sold

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "property")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "property")
    private List<Maintenance> maintenanceRecords;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // Add cascade here
    @JoinTable(
            name = "property_documents",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents = new HashSet<>();

    public Long getPriceLong() {
        return price == null ? null : price.longValue();
    }
}
