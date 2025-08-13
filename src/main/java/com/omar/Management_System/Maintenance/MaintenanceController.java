package com.omar.Management_System.Maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<Maintenance> create(@RequestBody MaintenanceDto dto) {
        Maintenance created = maintenanceService.createMaintenance(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<MaintenanceDto>> getByProperty(@PathVariable Long propertyId) {
        List<MaintenanceDto> dtos = maintenanceService.getMaintenanceByProperty(propertyId);
        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Maintenance> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(maintenanceService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
