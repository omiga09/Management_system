package com.omar.Management_System.Report.ReportController;

import com.omar.Management_System.Report.ReportService.ReportService;
import com.omar.Management_System.Transaction.Transaction.Transaction;
import com.omar.Management_System.User.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sales")
    public ResponseEntity<BigDecimal> getTotalSales() {
        BigDecimal totalSales = reportService.getTotalSales();
        return ResponseEntity.ok(totalSales.setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions/count")
    public ResponseEntity<Integer> getTransactionCount() {
        return ResponseEntity.ok(reportService.getTransactionCount());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/commission/monthly")
    public ResponseEntity<BigDecimal> getMonthlyCommissionReport(@RequestParam int year, @RequestParam int month) {
        BigDecimal monthlyCommission = reportService.getMonthlyCommission(year, month);
        return ResponseEntity.ok(monthlyCommission.setScale(2, BigDecimal.ROUND_HALF_UP));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(reportService.getAllTransactions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-activity")
    public ResponseEntity<List<User>> getUserActivityReport() {
        return ResponseEntity.ok(reportService.getUserActivityReport());
    }
    }


