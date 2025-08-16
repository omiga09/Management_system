package com.omar.Management_System.location.Location;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String address;
    private Double lat;
    private Double lng;

    public boolean equalsIgnoreCase(String location) {
        String fullLocation = String.join(", ",
                country != null ? country : "",
                city != null ? city : "",
                address != null ? address : "");

        return fullLocation.toLowerCase().contains(location.toLowerCase());
    }
}


