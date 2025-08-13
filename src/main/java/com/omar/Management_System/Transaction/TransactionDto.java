package com.omar.Management_System.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

        private Long id;
        private Long reservationId;
        private BigDecimal amount;
        private LocalDate transactionDate;
        private BigDecimal commissionAmount;
    }


