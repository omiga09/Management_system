package com.omar.Management_System.location.LocationRepository;

import com.omar.Management_System.location.Location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}