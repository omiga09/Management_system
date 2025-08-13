package com.omar.Management_System.Commission;

import com.omar.Management_System.Reservation.Reservation;
import com.omar.Management_System.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CommissionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.10"); // 10%

    /**
     * Creates a transaction and calculates the commission based on the amount.
     * Saves both entities and links commission to the transaction.
     *
     * @param reservation Reservation related to the transaction
     * @param amount      Transaction amount
     * @return saved Transaction with linked Commission
     */
    public Transaction createTransactionWithCommission(Reservation reservation, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setReservation(reservation);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());

        // Save transaction first to generate ID
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Calculate commission amount
        BigDecimal commissionAmount = amount.multiply(COMMISSION_RATE);

        Commission commission = new Commission();
        commission.setRate(COMMISSION_RATE);
        commission.setAmount(commissionAmount);
        commission.setTransaction(savedTransaction);
        commission.setCalculatedDate(LocalDate.now());

        // Save commission entity
        commissionRepository.save(commission);

        // Link commission to transaction and update
        savedTransaction.setCommission(commission);
        return transactionRepository.save(savedTransaction);
    }
}
