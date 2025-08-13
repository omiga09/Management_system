package com.omar.Management_System.Photo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.omar.Management_System.Property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
    public class Photo {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String filename;

        @ManyToOne
        @JoinColumn(name = "property_id")
        @JsonBackReference
        private Property property;

    }


