package com.omar.Management_System.Transaction.TransactionService;

import com.omar.Management_System.Commission.CommissionService.CommissionService;
import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Reservation.ReservationRepository.ReservationRepository;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionRepository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CommissionService commissionService;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }


    public Transaction createTransactionWithCommission(Reservation reservation, BigDecimal amount) {
        return commissionService.createTransactionWithCommission(reservation, amount);
    }


    public Transaction createTransaction(Long reservationId, BigDecimal amount) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return createTransactionWithCommission(reservation, amount);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
