package com.omar.Management_System.Commission.Commission;

import com.omar.Management_System.Transaction.Transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "commissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal rate; // e.g. 0.10 for 10%
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    private LocalDate calculatedDate;

}

