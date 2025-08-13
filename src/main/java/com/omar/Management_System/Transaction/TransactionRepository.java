package com.omar.Management_System.Transaction;

import com.omar.Management_System.Reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);
    Optional<Transaction> findByReservation(Reservation reservation);
}
