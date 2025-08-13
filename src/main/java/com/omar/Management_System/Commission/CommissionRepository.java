package com.omar.Management_System.Commission;


import com.omar.Management_System.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    Optional<Commission> findByTransaction(Transaction transaction);
}

