package com.omar.Management_System.Reservation.Reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.User.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    private LocalDate reservationDate;
    private String status; // Pending, Approved, Rejected
    private double commission;


    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonIgnoreProperties("reservations")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    @JsonIgnoreProperties("reservations")
    private User buyer;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("reservation")
    private Transaction transaction;

    private String reservationCode;

}
