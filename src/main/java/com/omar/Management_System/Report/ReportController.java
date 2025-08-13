package com.omar.Management_System.Report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/sales")
    public ResponseEntity<BigDecimal> getTotalSales() {
        return ResponseEntity.ok(reportService.getTotalSales());
    }

    @GetMapping("/transactions/count")
    public ResponseEntity<Integer> getTransactionCount() {
        return ResponseEntity.ok(reportService.getTransactionCount());
    }
}
