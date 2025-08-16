package com.omar.Management_System.Commission.CommissionService;

import com.omar.Management_System.Commission.Commission.Commission;
import com.omar.Management_System.Commission.CommissionRepository.CommissionRepository;
import com.omar.Management_System.Commission.SytemCommissionConfigService.SystemCommissionConfigService;

import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionRepository.TransactionRepository;
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

    @Autowired
    private SystemCommissionConfigService configService;


    public Transaction createTransactionWithCommission(Reservation reservation, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setReservation(reservation);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDate.now());


        Transaction savedTransaction = transactionRepository.save(transaction);


        BigDecimal commissionRate = BigDecimal.valueOf(configService.getCurrentRate());
        BigDecimal commissionAmount = amount.multiply(commissionRate).setScale(2, BigDecimal.ROUND_HALF_UP);

        Commission commission = new Commission();
        commission.setRate(commissionRate);
        commission.setAmount(commissionAmount);
        commission.setTransaction(savedTransaction);
        commission.setCalculatedDate(LocalDate.now());


        commissionRepository.save(commission);


        savedTransaction.setCommission(commission);
        return transactionRepository.save(savedTransaction);
    }
}
