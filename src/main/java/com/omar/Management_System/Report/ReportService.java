package com.omar.Management_System.Report;
import com.omar.Management_System.Transaction.Transaction;
import com.omar.Management_System.Transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ReportService {
    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal getTotalSales() {
        return transactionRepository.findAll().stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTransactionCount() {
        return transactionRepository.findAll().size();
    }

    // Add more dynamic calculations as needed
}
