package com.omar.Management_System.Transaction.TransactionRepository;

import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReservation(Reservation reservation);
    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);


}
