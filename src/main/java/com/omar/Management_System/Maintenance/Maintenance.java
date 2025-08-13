package com.omar.Management_System.Maintenance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omar.Management_System.Property.Property;
import com.omar.Management_System.enums.MaintenanceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    private String description;
    private Double cost;
    private String contractorName;

    private LocalDate requestDate;
    private LocalDate completionDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;
}
