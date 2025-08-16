package com.omar.Management_System.Transaction.TransactionController;

import com.omar.Management_System.Reservation.ReservationRepository.ReservationRepository;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionDto.TransactionDto;
import com.omar.Management_System.Transaction.TransactionService.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long reservationId,
            @RequestBody Map<String, Object> payload) {

        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        Transaction transaction = transactionService.createTransaction(reservationId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }


    private TransactionDto convertToDto(Transaction transaction) {
        BigDecimal commissionAmount = transaction.getCommission() != null
                ? transaction.getCommission().getAmount()
                : BigDecimal.ZERO;

        return new TransactionDto(
                transaction.getId(),
                transaction.getReservation().getReservationId(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                commissionAmount
        );
    }
}
