package com.omar.Management_System.Maintenance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByProperty_PropertyId(Long propertyId);
}
