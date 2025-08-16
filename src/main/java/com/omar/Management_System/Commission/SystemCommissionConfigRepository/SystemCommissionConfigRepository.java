package com.omar.Management_System.Commission.SystemCommissionConfigRepository;

import com.omar.Management_System.Commission.SystemCommissionConfig.SystemCommissionConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemCommissionConfigRepository extends JpaRepository<SystemCommissionConfig, Long> {
    SystemCommissionConfig findTopByOrderByIdDesc();
}


