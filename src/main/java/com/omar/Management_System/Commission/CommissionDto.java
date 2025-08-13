package com.omar.Management_System.Commission;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionDto {

        private Long id;
        private BigDecimal rate;
        private BigDecimal amount;
        private Long transactionId;
        private LocalDate calculatedDate;
    }

