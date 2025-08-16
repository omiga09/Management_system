package com.omar.Management_System.Commission.CommissionController;

import com.omar.Management_System.Commission.Commission.Commission;
import com.omar.Management_System.Commission.CommissionDto.CommissionDto;
import com.omar.Management_System.Commission.CommissionRepository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    @Autowired
    private CommissionRepository commissionRepository;

    @GetMapping
    public List<CommissionDto> getAllCommissions() {
        return commissionRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<CommissionDto> getCommissionByTransaction(@PathVariable Long transactionId) {
        return commissionRepository.findById(transactionId)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CommissionDto convertToDto(Commission commission) {
        return new CommissionDto(
                commission.getId(),
                commission.getRate(),
                commission.getAmount(),
                commission.getTransaction().getId(),
                commission.getCalculatedDate()
        );
    }
}
