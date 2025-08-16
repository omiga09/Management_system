package com.omar.Management_System.Property.PropertyRepository;


import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.User.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findBySeller(User seller);
    List<Property> findByStatus(String status);
}
