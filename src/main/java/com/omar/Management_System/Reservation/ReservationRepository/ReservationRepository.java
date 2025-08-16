package com.omar.Management_System.Reservation.ReservationRepository;


import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.User.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBuyer(User buyer);
    List<Reservation> findByProperty(Property property);
    List<Reservation> findByStatus(String status);

    Collection<Object> findByPropertySellerId(Long sellerId);
}
