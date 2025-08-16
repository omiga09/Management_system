package com.omar.Management_System.Property.Property;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omar.Management_System.Document.Document.Document;
import com.omar.Management_System.Maintenance.Maintenance.Maintenance;
import com.omar.Management_System.Photo.Photo.Photo;
import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.User.User.User;
import com.omar.Management_System.location.Location.Location;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    private String title;
    private String description;
    private String type; // For Rent, For Sale, Maintenance Only
    private Double price;
    private String status;// Available, Reserved, Sold
    private boolean approved;
    private boolean documentsVerified;
    private String ownerIdDocument;

    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Photo> photos = new ArrayList<>();

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> maintenanceRecords = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "property_documents",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id"))
    private Set<Document> documents = new HashSet<>();

    public Long getPriceLong() {
        return price == null ? null : price.longValue();
    }


    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setProperty(this);
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
        photo.setProperty(null);
    }


    public void addDocument(Document document) {
        documents.add(document);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
    }


    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setProperty(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setProperty(null);
    }


    public void addMaintenance(Maintenance maintenance) {
        maintenanceRecords.add(maintenance);
        maintenance.setProperty(this);
    }

    public void removeMaintenance(Maintenance maintenance) {
        maintenanceRecords.remove(maintenance);
        maintenance.setProperty(null);
    }
}
