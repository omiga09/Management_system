package com.omar.Management_System.Transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omar.Management_System.Commission.Commission;
import com.omar.Management_System.Reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDate transactionDate;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    @JsonManagedReference
    private Reservation reservation;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Commission commission;
}
