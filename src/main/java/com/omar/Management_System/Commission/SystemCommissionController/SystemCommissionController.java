package com.omar.Management_System.Commission.SystemCommissionController;

import com.omar.Management_System.Commission.SystemCommissionConfig.SystemCommissionConfig;
import com.omar.Management_System.Commission.SytemCommissionConfigService.SystemCommissionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/system-commission")
public class SystemCommissionController {

    @Autowired
    private SystemCommissionConfigService configService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rate")
    public ResponseEntity<Double> getCurrentRate() {
        return ResponseEntity.ok(configService.getCurrentRate());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/rate")
    public ResponseEntity<SystemCommissionConfig> updateRate(@RequestParam double rate) {
        if (rate < 0 || rate > 1) {
            return ResponseEntity.badRequest().body(null); // Optional: validate range
        }
        SystemCommissionConfig updated = configService.updateRate(rate);
        return ResponseEntity.ok(updated);
    }
}
