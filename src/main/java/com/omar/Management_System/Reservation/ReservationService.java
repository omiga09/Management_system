package com.omar.Management_System.Reservation;

import com.omar.Management_System.Property.PropertyRepository;
import com.omar.Management_System.User.User;
import com.omar.Management_System.User.UserRepository;
import com.omar.Management_System.Property.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public Optional<ReservationDto> getReservationById(Long id) {
        return reservationRepository.findById(id).map(this::mapToDto);
    }

    public ReservationDto createReservation(Long propertyId, Long buyerId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Reservation reservation = new Reservation();
        reservation.setProperty(property);
        reservation.setBuyer(buyer);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("Pending");

        return mapToDto(reservationRepository.save(reservation));
    }

    public ReservationDto updateStatus(Long reservationId, String status) {
        return reservationRepository.findById(reservationId).map(reservation -> {
            reservation.setStatus(status);
            return mapToDto(reservationRepository.save(reservation));
        }).orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getReservationId(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getProperty().getPropertyId(),
                reservation.getBuyer().getId()
        );
    }
}
