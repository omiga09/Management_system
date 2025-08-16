package com.omar.Management_System.Reservation.ReservationController;

import com.omar.Management_System.Reservation.ReservationDto.ReservationDto;
import com.omar.Management_System.Reservation.ReservationService.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto dto) {
        ReservationDto created = reservationService.createReservation(dto.getPropertyId(), dto.getBuyerId());

        String code = "RSV" + System.currentTimeMillis(); // au UUID.randomUUID().toString().substring(0, 8)
        created.setReservationCode(code);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationDto> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        ReservationDto updated = reservationService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
